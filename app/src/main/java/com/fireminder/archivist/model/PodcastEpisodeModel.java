package com.fireminder.archivist.model;

import android.content.ContentValues;

import com.fireminder.archivist.components.NetworkApi;
import com.fireminder.archivist.utils.Logger;
import com.fireminder.archivist.utils.TimingLogger;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.fireminder.archivist.model.EpisodeTable.Contract.*;

public class PodcastEpisodeModel {

  private static final String TAG = "PodcastEpisodeModel";

  NetworkApi networkApi;
  PodcastDao podcastDao;
  EpisodeDao episodeDao;

  PodcastEpisodeModel(NetworkApi networkApi, PodcastDao podcastDao, EpisodeDao episodeDao) {
    this.networkApi = networkApi;
    this.podcastDao = podcastDao;
    this.episodeDao = episodeDao;
  }

  public void subscribe(String feedUrl) {
    networkApi.get(feedUrl, new NetworkApi.Callback() {
      @Override
      public void onSuccess(String response) {
        parseAndInsert(response);
      }

      @Override
      public void onFailure(String reason, Throwable tr) {
        Logger.e(TAG, "subscribe failure for: " + reason, tr);
      }
    });
  }

  private void parseAndInsert(final String response) {
    final PodcastTable.Podcast podcast = parsePodcastInfo(response);
    podcastDao.insert(podcast);

    List<ContentValues> episodes = null;

    try {
      episodes = parseEpisodeInfo(response, podcast);
    } catch (DocumentException e) {
      Logger.e(TAG, "Document error for podcast: " + podcast.title, e);
    }

    if (episodes != null) {
      episodeDao.insert(episodes);
    }
  }

  PodcastTable.Podcast parsePodcastInfo(String response) {
    return null;
  }

  private List<ContentValues> parseEpisodeInfo(String response,
                                               PodcastTable.Podcast podcastInfo) throws DocumentException {

    SimpleDateFormat formatter = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.US);

    TimingLogger logger = new TimingLogger(TAG, "parseEpisodesFromResponse");
    List<ContentValues> contentValuesList = new ArrayList<>();

    response = validateXml(response);

    logger.addSplit("validateXml");

    Element channel = DocumentHelper.parseText(response).getRootElement().element("channel");

    for (Element item : channel.elements("item")) {
      ContentValues cv = parseEpisodeContentValueFromElement(item, formatter);
      if (cv != null) {
        cv.put(PODCAST_UUID, podcastInfo.id.toString());
        contentValuesList.add(cv);
      }
    }

    logger.dumpToLog();
    return contentValuesList;
  }

  private ContentValues parseEpisodeContentValueFromElement(Element item, SimpleDateFormat formatter) {
    ContentValues contentValues = new ContentValues();

    contentValues.put(TITLE, item.elementText("title"));
    contentValues.put(DESCRIPTION, item.elementText("description"));

    try {
      contentValues.put(PUBLICATION_DATE, formatter.parse(item.elementText("pubDate").trim()).getTime());
    } catch (ParseException e) {
      Logger.e(TAG, "Episode item: " + contentValues.getAsString(TITLE) + " date format parse exception. Skipping", e);
      return null;
    }

    try {
      String uri = item.element("enclosure").attributeValue("url");
      contentValues.put(STREAM_URI, uri);
    } catch (NullPointerException e) {
      // Item lacks media, skip
      Logger.e(TAG, "Episode item: " + contentValues.getAsString(TITLE) + " lacks a enclosure url. Skipping");
      return null;
    }

    // TODO: "enclosure.length" is very often wrong. Delaying implementation.
    //episode.media_length = item.element("enclosure").attributeValue("length");

    contentValues.put(EPISODE_UUID, UUID.randomUUID().toString());
    return contentValues;
  }


  public String validateXml(String xmlResponse) {
    // Some feeds can contain a Byte Order mark before xml begins
    // this regex removes those before parsing.
    return xmlResponse.replaceAll("^[^<]*<", "<");
  }

}
