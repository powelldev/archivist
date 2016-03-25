package com.fireminder.archivist.mediaplayer;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class PlayerControllerTest {

  @Mock
  Contract.Player mPlayer;

  @Mock
  Contract.EpisodeRepository mRepo;


  @Captor
  ArgumentCaptor<Contract.EpisodeRepository.OnMediaLoaded> onMediaLoadedArgumentCaptor;

  @Captor
  ArgumentCaptor<Contract.Player.OnPlayerPrepared> onPlayerPreparedArgumentCaptor;

  PlayerController controller;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    controller = new PlayerController(mPlayer, mRepo);
  }

  @Test
  public void changeMedia_inducesPlayerToStop() throws Exception {
    Contract.Media media = Mockito.mock(Contract.Media.class);
    Mockito.when(media.getId()).thenReturn(new UUID(0, 1));

    controller.changeMedia(new UUID(0, 1));

    Mockito.verify(mRepo).retrieveMedia(Mockito.any(UUID.class), onMediaLoadedArgumentCaptor.capture());

    onMediaLoadedArgumentCaptor.getValue().onMediaLoaded(media);

    Mockito.verify(mPlayer, Mockito.times(1)).stop();
  }


  @Test
  public void changeMedia_wasNotPlaying_shouldNotContinuePlayback() throws Exception {
    Contract.Media media = Mockito.mock(Contract.Media.class);
    Mockito.when(media.getId()).thenReturn(new UUID(0, 1));

    controller.changeMedia(new UUID(0, 1));

    Mockito.when(mPlayer.isPlaying()).thenReturn(false);
    Mockito.verify(mRepo).retrieveMedia(Mockito.any(UUID.class), onMediaLoadedArgumentCaptor.capture());
    onMediaLoadedArgumentCaptor.getValue().onMediaLoaded(media);

    Mockito.verify(mPlayer).setDataSource(Mockito.any(Contract.Media.class), onPlayerPreparedArgumentCaptor.capture());
    onPlayerPreparedArgumentCaptor.getValue().onPlayerPrepared(media);

    Mockito.verify(mPlayer, Mockito.never()).start();
  }

  @Test
  public void changeMedia_wasPlaying_shouldContinuePlayback() throws Exception {
    Contract.Media media = Mockito.mock(Contract.Media.class);
    Mockito.when(media.getId()).thenReturn(new UUID(0, 1));

    controller.changeMedia(new UUID(0, 1));

    Mockito.when(mPlayer.isPlaying()).thenReturn(true);
    Mockito.verify(mRepo).retrieveMedia(Mockito.any(UUID.class), onMediaLoadedArgumentCaptor.capture());
    onMediaLoadedArgumentCaptor.getValue().onMediaLoaded(media);

    Mockito.verify(mPlayer).setDataSource(Mockito.any(Contract.Media.class), onPlayerPreparedArgumentCaptor.capture());
    onPlayerPreparedArgumentCaptor.getValue().onPlayerPrepared(media);

    Mockito.verify(mPlayer, Mockito.times(1)).start();
  }

  @After
  public void tearDown() throws Exception {

  }
}