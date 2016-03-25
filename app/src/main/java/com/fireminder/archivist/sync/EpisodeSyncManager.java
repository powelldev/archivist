package com.fireminder.archivist.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.text.TextUtils;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.model.EpisodeUtils;
import com.fireminder.archivist.model.IvyContentProvider;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.fireminder.archivist.model.EpisodeTable.Contract.DESCRIPTION;
import static com.fireminder.archivist.model.EpisodeTable.Contract.EPISODE_UUID;
import static com.fireminder.archivist.model.EpisodeTable.Contract.PODCAST_UUID;
import static com.fireminder.archivist.model.EpisodeTable.Contract.PUBLICATION_DATE;
import static com.fireminder.archivist.model.EpisodeTable.Contract.STREAM_URI;
import static com.fireminder.archivist.model.EpisodeTable.Contract.TITLE;
import static com.fireminder.archivist.model.EpisodeTable.Episode;
import static com.fireminder.archivist.model.PodcastTable.Podcast;

public class EpisodeSyncManager {

  private static final String TAG = "EpisodeSyncManager";

  private final Podcast mPodcast;

  private static SimpleDateFormat pubDateFormatter = new SimpleDateFormat(
      "EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.US);

  public static int subscribe(Podcast podcast) {
    EpisodeSyncManager manager = new EpisodeSyncManager(podcast);
    String response = manager.get(podcast.feed);
    if (TextUtils.isEmpty(response)) {
      Logger.e(TAG, "subscribe() " + podcast.title + " response is empty!");
    }
    return manager.addEpisodesFromResponse(response, podcast.id);
  }

  private String get(String url) {
    Logger.v(TAG, "get() " + mPodcast.title + " url: " + url);
    RequestFuture<String> future = RequestFuture.newFuture();
    StringRequest request = new StringRequest(url, future, future);
    IvyRequestQueue.getInstance().addToRequestQueue(request);
    try {
      return future.get(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Logger.e(TAG, "get() " + mPodcast.title + " url: " + url + " Interrupted: " + e);
    } catch (ExecutionException e) {
      Logger.e(TAG, "get() " + mPodcast.title + " url: " + url + " ExecutionException: " + e);
    } catch (TimeoutException e) {
      Logger.e(TAG, "get() " + mPodcast.title + " url: " + url + " Timeout: " + e);
    }
    return null;
  }

  public EpisodeSyncManager(Podcast podcast) {
    mPodcast = podcast;
  }

  public String validateXml(String xmlResponse) {
    // Some feeds can contain a Byte Order mark before xml begins
    // this regex removes those before parsing.
    return xmlResponse.replaceAll("^[^<]*<", "<");
  }

  public int addEpisodesFromResponse(String response, UUID podcastUuid) {
    List<ContentValues> episodes = new ArrayList<>();
    try {
      episodes = parseEpisodesFromResponse(response);
    } catch (DocumentException e) {
      Logger.e(TAG, "addEpisodesFromResponse(): Error parsing " + e);
    } catch (ParseException e) {
      Logger.e(TAG, "addEpisodesFromResponse(): Error parsing " + e);
    }

    addPodcastUuid(episodes, podcastUuid);
    ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    for (ContentValues cv : episodes) {
      contentResolver.insert(IvyContentProvider.Table.Episodes.uri, cv);
    }
    return episodes.size();
  }

  /**
   * Parses episodes from an RSS string. These episodes will need to have the PODCAST_UUID attribute added.
   */
  protected List<ContentValues> parseEpisodesFromResponse(String response) throws DocumentException, java.text.ParseException {
    TimingLogger logger = new TimingLogger(TAG, "parseEpisodesFromResponse"); int i = 0;

    List<ContentValues> contentValuesList = new ArrayList<>();
    response = validateXml(response);
    logger.addSplit("validateXml");

    Element channel = DocumentHelper.parseText(response).getRootElement().element("channel");
    for (Element item : channel.elements("item")) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(TITLE, item.elementText("title"));
      contentValues.put(DESCRIPTION, item.elementText("description"));
      contentValues.put(PUBLICATION_DATE, pubDateFormatter.parse(item.elementText("pubDate").trim()).getTime());

      try {
        String uri = item.element("enclosure").attributeValue("url");
        contentValues.put(STREAM_URI, uri);
      } catch (NullPointerException e) {
        // Item lacks media, skip
        Logger.d(TAG, "Episode item: " + contentValues.getAsString(TITLE) + " lacks a enclosure url. Skipping");
        continue;
      }

      // TODO: "enclosure.length" is very often wrong. Delaying implementation.
      //episode.media_length = item.element("enclosure").attributeValue("length");

      contentValues.put(EPISODE_UUID, UUID.randomUUID().toString());
      contentValuesList.add(contentValues);
      logger.addSplit("addItem i: " + ++i);
    }
    logger.dumpToLog();
    return contentValuesList;
  }

  protected void addPodcastUuid(List<ContentValues> episodes, UUID podcastUuid) {
    for(ContentValues cv : episodes) {
      cv.put(PODCAST_UUID, podcastUuid.toString());
    }
  }

  protected List<Episode> getEpisodesToDownload() {
    return EpisodeUtils.getEpisodesMarkedForDownload();
  }
}
