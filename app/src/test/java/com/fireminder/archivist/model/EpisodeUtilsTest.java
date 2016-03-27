package com.fireminder.archivist.model;

import com.fireminder.archivist.TestUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

import static com.fireminder.archivist.model.EpisodeTable.*;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class EpisodeUtilsTest extends TestCase {

  static final Episode episode = new Episode("title",
      "description", "streamuri", "localuri", TestUtil.currentTimeMillis(),
      TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);


  EpisodeDao episodeDao;

  @Before
  public void setup() {
    episodeDao = new EpisodeDao();
  }
  @Test
  public void testUpdateElapsed() throws Exception {

    episodeDao.insert(episode);

    episodeDao.updateElapsed(episode, 1);

    long elapsed = episodeDao.getEpisode(episode.episodeUuid).elapsed;
    Assert.assertEquals(1, elapsed);
  }

  @Test
  public void getEpisode() {
    Assert.assertNull(episodeDao.getEpisode(new UUID(0, 1)));

    episodeDao.insert(episode);

    Assert.assertNotNull(episodeDao.getEpisode(new UUID(0, 1)));
  }

  @Test
  public void updateBytes() {
    episodeDao.insert(episode);
    episodeDao.updateBytesDownloaded(episode, 11);
    Assert.assertEquals(11, episodeDao.getEpisode(new UUID(0, 1)).bytesDownloaded);
  }
}

