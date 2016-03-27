package com.fireminder.archivist.sync;


import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.TestUtil;
import com.fireminder.archivist.model.EpisodeDao;
import com.fireminder.archivist.model.EpisodeTable;
import com.koushikdutta.async.util.StreamUtility;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class IvyDownloadManagerTest {


  EpisodeDao episodeDao;
  IvyDownloadManager ivyDownloadManager;


  @Before
  public void setup() {
    episodeDao = new EpisodeDao();
    ivyDownloadManager = new IvyDownloadManager(episodeDao);
  }


  @Test
  public void download_erroneousStreamUri() throws IOException {
    File f = IvyApplication.getAppContext().getFileStreamPath("wrong.mp3");

    EpisodeTable.Episode episode = new EpisodeTable.Episode("title",
        "description", f.getAbsolutePath(), "", TestUtil.currentTimeMillis(),
        TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), EpisodeTable.DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

    episodeDao.insert(episode);

    ivyDownloadManager.download(episode, true);
    Assert.assertEquals(EpisodeTable.DownloadStatus.DOWNLOAD_ATTEMPTED_FAILED, episodeDao.getEpisode(episode.episodeUuid).downloadStatus);
  }
  @Test
  public void download() throws IOException {
   File f = IvyApplication.getAppContext().getFileStreamPath("test.mp3");
  StreamUtility.writeFile(f, "hello world");

    EpisodeTable.Episode episode = new EpisodeTable.Episode("title",
        "description", f.getAbsolutePath(), "", TestUtil.currentTimeMillis(),
        TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), EpisodeTable.DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

    episodeDao.insert(episode);

    ivyDownloadManager.download(episode, true);
    Assert.assertEquals(EpisodeTable.DownloadStatus.DOWNLOADED, episodeDao.getEpisode(episode.episodeUuid).downloadStatus);
  }

}