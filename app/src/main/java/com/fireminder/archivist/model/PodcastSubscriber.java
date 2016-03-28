package com.fireminder.archivist.model;

import android.content.ContentValues;

import com.fireminder.archivist.components.NetworkApi;
import com.fireminder.archivist.utils.Logger;

import org.dom4j.DocumentException;

import java.util.List;

public class PodcastSubscriber {

  private static final String TAG = "PodcastSubscriber";

  private final NetworkApi networkApi;
  private final PodcastDao podcastDao;
  private final EpisodeDao episodeDao;
  private final EpisodeXmlParser episodeXmlParser;
  private final PodcastXmlParser podcastXmlParser;

  PodcastSubscriber(NetworkApi networkApi,
                    PodcastDao podcastDao,
                    EpisodeDao episodeDao,
                    EpisodeXmlParser eXmlParser,
                    PodcastXmlParser pXmlParser) {
    this.networkApi = networkApi;
    this.podcastDao = podcastDao;
    this.episodeDao = episodeDao;
    this.episodeXmlParser = eXmlParser;
    this.podcastXmlParser = pXmlParser;
  }

  public void subscribe(final String feedUrl) {
    networkApi.get(feedUrl, new NetworkApi.Callback() {
      @Override
      public void onSuccess(final String feedUrl, final String response) {
        parseAndInsert(feedUrl, response);
      }

      @Override
      public void onFailure(String reason, Throwable tr) {
        Logger.e(TAG, "subscribe failure for: " + reason, tr);
      }
    });
  }

  private void parseAndInsert(final String feedUrl, final String response) {
    try {
      final ContentValues podcast = parsePodcastInfo(response, feedUrl);
      podcastDao.insert(podcast);

      List<ContentValues> episodes =
          episodeXmlParser.parseEpisodeInfo(response,
              podcast.getAsString(PodcastTable.Contract.PODCAST_UUID));

      if (episodes == null) {
        Logger.e(TAG, "Episodes list was null for podcast:" + feedUrl);
        return;
      }

      episodeDao.insert(episodes);

    } catch (DocumentException e) {
      Logger.e(TAG, "Document error for podcast: " + feedUrl);
    }

  }

  private ContentValues parsePodcastInfo(String response, String feedUrl) throws DocumentException {
    ContentValues cv = podcastXmlParser.parse(response);
    cv.put(PodcastTable.Contract.FEED, feedUrl);
    return cv;
  }


}
