package com.fireminder.archivist;

import com.fireminder.archivist.model.IvyContentProvider;
import com.fireminder.archivist.model.PodcastTable;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class StatusReportTest {

  @Test
  public void info() {

    PodcastTable.Podcast podcast = new PodcastTable.Podcast(new UUID(0, 1), "title", "description", "feed", "imgUrl");
    IvyApplication.getAppContext().getContentResolver().insert(IvyContentProvider.Table.Podcasts.uri, podcast.toContentValues());

    String reportJson = new Gson().toJson(new StatusReport());

    Assert.assertNotNull(reportJson);
  }

}