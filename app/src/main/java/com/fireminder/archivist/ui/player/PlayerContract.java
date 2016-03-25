package com.fireminder.archivist.ui.player;

import android.widget.ImageView;

public interface PlayerContract {

  interface View {
    ImageView getCenterAlbumArt();
    void setTitle(String title);
    void setSubTitle(String subTitle);
    void setDuration(String duration);
    void setElapsed(String elapsed);
    void setBackgroundColor(int color);
    void setPlayPauseButtonToPlay(boolean play);
  }

  interface UserActionsListener {
    void exitClicked();
    void onSnoozeClicked();
    void onPlayPauseClicked();
    void onRewindClicked();
    void onForwardClicked();
    void onSeekBarStarted();
    void onSeekBarStopped(int progress);
    void onSeekBarProgressChanged(int progress);
    void onPreviousClicked();
    void onNextClicked();
  }


  interface Player {

    void playPause();
    void start();
    void stop();
    void play();
    void pause();
    long getDuration();
    long getCurrentPosition();
    boolean isPlaying();
    void seekTo(long position);
    void seekStart();
    void setDataSource(String uri);
    String getMedia();
  }
}
