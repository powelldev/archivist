package com.fireminder.archivist.mediaplayer;

import android.media.MediaPlayer;
import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;


@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class IvyPlayerTest {

  @Mock
  Contract.PlayerActionListener playerActionListener;

  @Mock
  Contract.EpisodeRepository episodeRepository;

  DummyMediaPlayer mediaPlayer;


  IvyPlayer ivyPlayer;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mediaPlayer = Mockito.spy(new DummyMediaPlayer());
    ivyPlayer = new IvyPlayer(mediaPlayer, playerActionListener);
  }


  @Test
  public void setDataSource_sunnyPath() throws Exception {
    final Contract.Media expectedMedia = Mockito.mock(Contract.Media.class);
    Mockito.when(expectedMedia.getUri()).thenReturn("i");
    Contract.Player.OnPlayerPrepared prepared = Mockito.mock(Contract.Player.OnPlayerPrepared.class);
    ivyPlayer.setDataSource(expectedMedia, prepared);
    Mockito.verify(prepared, Mockito.atLeastOnce()).onPlayerPrepared(expectedMedia);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setDataSource_nullUri() throws Exception {
    final Contract.Media expectedMedia = Mockito.mock(Contract.Media.class);
    Mockito.when(expectedMedia.getUri()).thenReturn(null);
    Contract.Player.OnPlayerPrepared prepared = Mockito.mock(Contract.Player.OnPlayerPrepared.class);
    ivyPlayer.setDataSource(expectedMedia, prepared);
  }

  @Test
  public void start() throws Exception {
    ivyPlayer.start();
    Mockito.verify(mediaPlayer, Mockito.times(1)).start();
  }

  /*
  @Test
  public void testStop() throws Exception {

  }


  */

  static class DummyMediaPlayer extends MediaPlayer {
    String dataSource;
    OnPreparedListener onPreparedListener;

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
      this.onPreparedListener = listener;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
      if (TextUtils.isEmpty(path)){
        throw new IOException("Path is null");
      }
      this.dataSource = path;
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
      if (TextUtils.isEmpty(dataSource)) {
        throw new IOException("data source not set");
      }
      onPreparedListener.onPrepared(this);
    }

    @Override
    public void start() throws IllegalStateException {

    }
  }
}