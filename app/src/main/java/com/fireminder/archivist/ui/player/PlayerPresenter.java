package com.fireminder.archivist.ui.player;

import com.fireminder.archivist.IvyPref;
import com.fireminder.archivist.ui.player.PlayerContract.UserActionsListener;

import static com.fireminder.archivist.ui.player.PlayerContract.View;

public class PlayerPresenter implements UserActionsListener {

  View view;
  PlayerContract.Player player;
  IvyPref prefs;

  public PlayerPresenter(View view, PlayerContract.Player mediaPlayer) {
    this.view = view;
    this.player = mediaPlayer;
  }

  @Override
  public void exitClicked() {
  }

  @Override
  public void onSnoozeClicked() {
  }

  @Override
  public void onPlayPauseClicked() {
    player.playPause();
  }

  @Override
  public void onRewindClicked() {
    player.seekTo(player.getCurrentPosition() - prefs.getRewindTimeMillis());
  }

  @Override
  public void onForwardClicked() {
    player.seekTo(player.getCurrentPosition() - prefs.getRewindTimeMillis());
  }

  @Override
  public void onSeekBarStarted() {

  }

  @Override
  public void onSeekBarStopped(int progress) {

  }

  @Override
  public void onSeekBarProgressChanged(int progress) {

  }

  @Override
  public void onPreviousClicked() {

  }

  @Override
  public void onNextClicked() {

  }
}
