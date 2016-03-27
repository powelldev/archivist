package com.fireminder.archivist.mediaplayer;

import android.media.MediaPlayer;

import com.fireminder.archivist.TestUtil;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

import static com.fireminder.archivist.model.EpisodeTable.DownloadStatus;
import static com.fireminder.archivist.model.EpisodeTable.Episode;
import static junit.framework.Assert.assertEquals;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class StatefulMediaPlayerMockitoTest {



  Episode BASIC_EPISODE = new Episode("title",
      "description", "streamuri", "localuri", TestUtil.currentTimeMillis(),
      TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

  static StatefulMediaPlayer.MediaStateListener mBaseListener = new StatefulMediaPlayer.MediaStateListener() {
    @Override
    public void onStateUpdated(StatefulMediaPlayer.State state) {

    }

    @Override
    public void onMediaElapsed(Episode media, int currentPosition) {

    }
  };


  @Test
  public void setDataSource_happyPath() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    smp.setDataSource(BASIC_EPISODE, false);
    assertEquals(BASIC_EPISODE, smp.media);
    assertEquals(false, smp.beginPlaybackWhenMediaIsPrepared);

    smp.setDataSource(BASIC_EPISODE, true);
    assertEquals(true, smp.beginPlaybackWhenMediaIsPrepared);
  }

  @Test
  public void setDataSource_nullDataSources() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    Exception e = null;
    try {
      smp.setDataSource(null, true);
    } catch (IllegalArgumentException ex) {
      e = ex;
    }

    if (e == null) {
      Assert.fail("Exception not thrown");
    }

    Episode episodeWithNullLocalUri = new Episode("title",
        "description", "streamuri", "", TestUtil.currentTimeMillis(),
        TestUtil.getDefaultDuration(), 0L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

    e = null;
    try {
      smp.setDataSource(episodeWithNullLocalUri, true);
    } catch (IllegalArgumentException ex) {
      e = ex;
    }

    if (e == null) {
      Assert.fail("Exception not thrown");
    }
  }

  @Test
  public void onPrepared_play() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    StatefulMediaPlayer spySmp = Mockito.spy(smp);

    Episode episodeWithElapsedTime = new Episode("title",
        "description", "streamuri", "localuri", TestUtil.currentTimeMillis(),
        TestUtil.getDefaultDuration(), 1L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

    spySmp.setDataSource(episodeWithElapsedTime, true);
    spySmp.onPrepared(null);
    Mockito.verify(spySmp, Mockito.times(1)).play();
  }

  @Test
  public void onPrepared_elapsedEpisode() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    StatefulMediaPlayer spySmp = Mockito.spy(smp);

    Episode episodeWithElapsedTime = new Episode("title",
        "description", "streamuri", "localuri", TestUtil.currentTimeMillis(),
        TestUtil.getDefaultDuration(), 1L, new UUID(0, 1), new UUID(0, 1), DownloadStatus.NOT_DOWNLOADED, false, 0, 0);

    spySmp.setDataSource(episodeWithElapsedTime, true);
    spySmp.onPrepared(null);
    Mockito.verify(mockMp, Mockito.times(1)).seekTo(1);
  }

  @Test(expected = IllegalStateException.class)
  public void onPrepared_null() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    smp.onPrepared(null);
  }

  @Test
  public void seeking() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    StatefulMediaPlayer spySmp = Mockito.spy(smp);

    spySmp.setDataSource(BASIC_EPISODE, false);
    spySmp.onPrepared(null);
    spySmp.play();
    spySmp.seekStart();
    spySmp.seekTo(3);


    Mockito.verify(spySmp, Mockito.times(1)).play();
    Mockito.verify(mockMp, Mockito.times(1)).seekTo(3);
  }

  @Test
  public void play() throws Exception {
    MediaPlayer mockMp = Mockito.mock(MediaPlayer.class);
    StatefulMediaPlayer smp = new StatefulMediaPlayer(mockMp, mBaseListener);
    StatefulMediaPlayer spySmp = Mockito.spy(smp);

    spySmp.setDataSource(BASIC_EPISODE, false);
    spySmp.onPrepared(null);

    spySmp.play();

    Mockito.verify(mockMp, Mockito.times(1)).start();
  }
}

