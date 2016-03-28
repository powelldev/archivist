package com.fireminder.archivist.model;


import android.content.ContentValues;

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

import static com.fireminder.archivist.model.EpisodeTable.Contract.DESCRIPTION;
import static com.fireminder.archivist.model.EpisodeTable.Contract.EPISODE_UUID;
import static com.fireminder.archivist.model.EpisodeTable.Contract.PODCAST_UUID;
import static com.fireminder.archivist.model.EpisodeTable.Contract.PUBLICATION_DATE;
import static com.fireminder.archivist.model.EpisodeTable.Contract.STREAM_URI;
import static com.fireminder.archivist.model.EpisodeTable.Contract.TITLE;

public class EpisodeXmlParser {

  private static final String TAG = "EpisodeXmlParser";

  private static SimpleDateFormat formatter = new SimpleDateFormat(
      "EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.US);

  public List<ContentValues> parseEpisodeInfo(String response,
      String podcastUuid) throws DocumentException {


    TimingLogger logger = new TimingLogger(TAG, "parseEpisodesFromResponse");
    List<ContentValues> contentValuesList = new ArrayList<>();

    response = validateXml(response);

    logger.addSplit("validateXml");

    Element channel = DocumentHelper.parseText(response).getRootElement().element("channel");

    for (Element item : channel.elements("item")) {
      ContentValues cv = parseEpisodeContentValueFromElement(item, formatter);
      if (cv != null) {
        cv.put(PODCAST_UUID, podcastUuid);
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
      contentValues.put(PUBLICATION_DATE,
          formatter.parse(item.elementText("pubDate").trim()).getTime());
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
