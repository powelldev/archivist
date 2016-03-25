package com.fireminder.archivist.mediaplayer;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.fireminder.archivist.utils.Logger;

import java.io.IOException;

public class IvyPlayer implements Contract.Player,
  MediaPlayer.OnBufferingUpdateListener,
  MediaPlayer.OnPreparedListener,
  MediaPlayer.OnErrorListener,
  MediaPlayer.OnInfoListener,
  MediaPlayer.OnSeekCompleteListener,
  MediaPlayer.OnCompletionListener {

  private static final String TAG = "IvyPlayer";

  @Override
  public void stop() {

  }

  final MediaPlayer mMediaPlayer;
  StatefulMediaPlayer.State mState;

  public IvyPlayer(MediaPlayer mediaPlayer, Contract.PlayerActionListener playerActionListener) {
    mState = StatefulMediaPlayer.State.CREATED;
    mMediaPlayer = mediaPlayer;
    mMediaPlayer.setOnCompletionListener(this);
    mMediaPlayer.setOnSeekCompleteListener(this);
    mMediaPlayer.setOnInfoListener(this);
    mMediaPlayer.setOnErrorListener(this);
    mMediaPlayer.setOnPreparedListener(this);
    mMediaPlayer.setOnBufferingUpdateListener(this);
  }

  private OnPlayerPrepared mOnPreparedCallback;
  private Contract.Media mMedia;

  @Override
  public void setDataSource(Contract.Media media, OnPlayerPrepared callback) throws IllegalArgumentException, IOException {
    checkParam(media);
    checkParam(callback);

    mOnPreparedCallback = callback;
    mMedia = media;

    final String uri = media.getUri();
    if (TextUtils.isEmpty(uri)) {
      throw new IllegalArgumentException("media uri is empty or null");
    }

    mMediaPlayer.reset();
    mMediaPlayer.setDataSource(uri);
    mMediaPlayer.prepare();
  }

  private <T> void checkParam(T param) throws IllegalArgumentException {
    if (param == null) {
      throw new IllegalArgumentException("parameter may not be null");
    }
  }



  @Override
  public boolean isPlaying() {
    return mMediaPlayer.isPlaying();
  }

  @Override
  public void start() {
    mMediaPlayer.start();
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {

  }

  @Override
  public void onCompletion(MediaPlayer mp) {

  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    return false;
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    if (mOnPreparedCallback == null) {
      Logger.d(TAG, "No OnPreparedCalback set before call to onPrepared()");
    }
    mOnPreparedCallback.onPlayerPrepared(mMedia);
    mOnPreparedCallback = null;
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {

  }
}
