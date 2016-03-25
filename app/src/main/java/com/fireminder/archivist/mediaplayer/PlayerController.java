package com.fireminder.archivist.mediaplayer;

import com.fireminder.archivist.utils.Logger;

import java.io.IOException;
import java.util.UUID;

public class PlayerController implements Contract.PlayerActionListener {

  private static final String TAG = "PlayerController";

  private Contract.Player player;
  private Contract.EpisodeRepository repo;
  private State mState;

  public PlayerController(Contract.Player player, Contract.EpisodeRepository repo) {
    this.player = player;
    this.repo = repo;
  }


  public enum State {
    STARTED, STOPPED, PAUSED, PREPARED, COMPLETED, CREATED, RELEASED
  }

  @Override
  public void changeMedia(UUID mediaId) {
    repo.retrieveMedia(mediaId,
        new Contract.EpisodeRepository.OnMediaLoaded() {
      @Override
      public void onMediaLoaded(Contract.Media media) {
        final boolean wasPlaying = player.isPlaying();
        player.stop();
        setDataSourceManagePlayback(media, wasPlaying);
      }
    });
  }

  @Override
  public void onStateUpdated(State state) {

  }

  /*
  @Override
  public void onStateUpdated(State state) {

  }

  private void transitionToState(PlayerController.State state) {
    Logger.v(TAG, "transitionToState() currentState: " + mState.name() + " goalState: " + state.name());
    mState = state;
    /*
    if (mListener != null) {
      mListener.onStateUpdated(state);
    }
  }

    */
  private void setDataSourceManagePlayback(Contract.Media media, final boolean shouldPlayback) {
    try {
      player.setDataSource(media, new Contract.Player.OnPlayerPrepared() {
        @Override
        public void onPlayerPrepared(Contract.Media media) {
          if (shouldPlayback) {
            player.start();
          }
        }
      });
    } catch (IOException e) {
      Logger.e(TAG, "setDataSource() an error occurred: ", e);
    }
  }


  @Override
  public void onMediaElapsed(Contract.Media media, int currentPosition) {

  }


}
