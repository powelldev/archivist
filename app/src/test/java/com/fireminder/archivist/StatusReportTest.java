package com.fireminder.archivist;

import com.fireminder.archivist.model.IvyContentProvider;
import com.fireminder.archivist.model.PodcastTable;
import com.fireminder.archivist.sync.EpisodeSyncManager;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class StatusReportTest {

  @Test
  public void info() {

    PodcastTable.Podcast podcast = new PodcastTable.Podcast(new UUID(0, 1), "title", "description", "feed", "imgUrl");
    IvyApplication.getAppContext().getContentResolver().insert(IvyContentProvider.Table.Podcasts.uri, podcast.toContentValues());

    EpisodeSyncManager syncManager = new EpisodeSyncManager(podcast);
    syncManager.addEpisodesFromResponse(EpisodeSyncManagerTest.response, podcast.id);

    String reportJson = new Gson().toJson(new StatusReport());

    Assert.assertNotNull(reportJson);
  }

}