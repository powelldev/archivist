package com.fireminder.archivist.model;

import com.fireminder.archivist.TestUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

import static com.fireminder.archivist.model.EpisodeTable.*;

@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class EpisodeUtilsTest extends TestCase {

  static final Episode episode = new Episode("title",
      "description", "streamuri", "localuri", TestUtil.currentTimeMillis(),
      TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

  @Test
  public void testUpdateElapsed() throws Exception {
    EpisodeUtils.insert(episode);

    EpisodeUtils.updateElapsed(episode, 1);

    long elapsed = EpisodeUtils.getEpisode(episode.episodeUuid).elapsed;
    Assert.assertEquals(1, elapsed);
  }

  @Test
  public void getEpisode() {
    Assert.assertNull(EpisodeUtils.getEpisode(new UUID(0, 1)));

    EpisodeUtils.insert(episode);

    Assert.assertNotNull(EpisodeUtils.getEpisode(new UUID(0, 1)));
  }

  @Test
  public void updateBytes() {
    EpisodeUtils.insert(episode);
    EpisodeUtils.updateBytesDownloaded(episode, 11);
    Assert.assertEquals(11, EpisodeUtils.getEpisode(new UUID(0,1)).bytesDownloaded);
  }
}

