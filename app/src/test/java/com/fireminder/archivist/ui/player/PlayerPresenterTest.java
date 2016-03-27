package com.fireminder.archivist.ui.player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class PlayerPresenterTest {

  @Mock
  PlayerContract.View view;

  @Mock
  PlayerContract.Player player;

  PlayerPresenter presenter;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    presenter = new PlayerPresenter(view, player);
  }

  @Test
  public void playPauseClicked() throws Exception {
    presenter.onPlayPauseClicked();
    Mockito.verify(player, Mockito.times(1)).playPause();
  }

}