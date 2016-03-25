package com.fireminder.archivist.mediaplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import com.fireminder.archivist.utils.Logger;

import java.io.IOException;

import static com.fireminder.archivist.model.EpisodeTable.Episode;

public class StatefulMediaPlayer implements
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnInfoListener,
    MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnCompletionListener {

  private static final String TAG = "StatefulMediaPlayer";

  protected Episode media;
  protected MediaPlayer mMediaPlayer;
  private MediaStateListener mListener;
  protected boolean wasPlaying = false;

  protected boolean beginPlaybackWhenMediaIsPrepared = false;

  public State mState;

  public StatefulMediaPlayer(MediaPlayer mediaPlayer, MediaStateListener listener) {
    mState = State.CREATED;
    mMediaPlayer = mediaPlayer;
    mMediaPlayer.setOnCompletionListener(this);
    mMediaPlayer.setOnSeekCompleteListener(this);
    mMediaPlayer.setOnInfoListener(this);
    mMediaPlayer.setOnErrorListener(this);
    mMediaPlayer.setOnPreparedListener(this);
    mMediaPlayer.setOnBufferingUpdateListener(this);
    mListener = listener;
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    Logger.e(TAG, "onBufferingUpdate: " + " percent " + percent);
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    Logger.v(TAG, "onPrepared");
    transitionToState(State.PREPARED);
    if (media == null) {
      // something odd has happened
      Logger.e(TAG, "onPrepared called with null episode. Was setDataSource not called?");
      throw new IllegalStateException("onPrepared called with null episode. Was setDataSource not called?");
    }
    if (media.elapsed != 0) {
      Logger.v(TAG, "onPrepared: media being resumed, starting at time: " + media.elapsed);
      mMediaPlayer.seekTo((int) media.elapsed);
    }
    if (beginPlaybackWhenMediaIsPrepared) {
      Logger.v(TAG, "onPrepared: media prepared and starting playback.");
      play();
    }
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    Logger.e(TAG, "onError: " + "what: " + what + " extra: " + extra);
    return false;
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    Logger.v(TAG, "onInfo: " + "what: " + what + " extra: " + extra);
    return false;
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {
    Logger.v(TAG, "onSeekComplete");
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    Logger.v(TAG, "onCompletion");
    transitionToState(State.COMPLETED);
  }

  public long getDuration(Context context) {
    long durationLong = 0;
    if (media != null) {
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      retriever.setDataSource(context, Uri.parse(media.streamUri));
      String duration =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
      durationLong = Long.parseLong(duration);
      retriever.release();
    }
    return durationLong;
  }

  public long getCurrentPosition() {
    return mMediaPlayer.getCurrentPosition();
  }

  public boolean isPlaying() {
    return mMediaPlayer.isPlaying();
  }

  public Episode getMedia() {
    return media;
  }

  public enum State {
    STARTED, STOPPED, PAUSED, PREPARED, COMPLETED, CREATED, RELEASED
  }

  /* VERIFY DATA BEFORE PASSED TO MEDIAPLAYER */
  public void setDataSource(Episode media, boolean playWhenReady) throws IOException {
    Logger.v(TAG, "setDataSource() currentState: " + mState.name());
    if (media == null) {
      Logger.e(TAG, "setDataSource() media was null");
      throw new IllegalArgumentException("Media was null during setDataSource");
    }
    final String uri = media.localUri;
    if (TextUtils.isEmpty(uri)) {
      Logger.e(TAG, "setDataSource() media uri was null");
      throw new IllegalArgumentException("media uri null");
    }

    this.media = media;
    this.beginPlaybackWhenMediaIsPrepared = playWhenReady;
    mMediaPlayer.reset();
    mMediaPlayer.setDataSource(uri);
    mMediaPlayer.prepare();
  }


  private void transitionToState(State state) {
    Logger.v(TAG, "transitionToState() currentState: " + mState.name() + " goalState: " + state.name());
    mState = state;
    if (mListener != null) {
      mListener.onStateUpdated(state);
    }
  }

  public interface MediaStateListener {
    void onStateUpdated(State state);

    /**
     * Handle the media playing elasping
     * @param episode
     * @param currentPosition
     */
    void onMediaElapsed(Episode episode, int currentPosition);
  }

  public void playPause() throws IllegalStateException {
    Logger.v(TAG, "playPause(): State: " + mState.name());
    switch (mState) {
      case PAUSED:
      case PREPARED:
        play();
        break;
      case STARTED:
        pause();
        break;
      default:
        Logger.v(TAG, "playPause was called with no data source set");
        throw new IllegalStateException("No data source set.");
    }
  }

  public void start() throws IOException {
    Logger.v(TAG, "start(): State: " + mState.name());
    this.setDataSource(media, true);
  }

  public void stop() {
    Logger.v(TAG, "stop(): State: " + mState.name());
    mMediaPlayer.stop();
    transitionToState(State.STOPPED);
  }


  public void play() {
    Logger.v(TAG, "play(): State: " + mState.name());
    switch (mState) {
      case PREPARED:
      case PAUSED:
        mMediaPlayer.start();
        transitionToState(State.STARTED);
        break;
      default:
        Logger.d(TAG, "play(): was called in wrong state: " + mState.name());
        break;
    }
  }

  public void pause() {
    Logger.v(TAG, "pause(): State: " + mState.name());
    switch (mState) {
      case STARTED:
        mMediaPlayer.pause();
        mListener.onMediaElapsed(media, mMediaPlayer.getCurrentPosition());
        transitionToState(State.PAUSED);
        break;
      default:
        Logger.d(TAG, "pause(): was called in wrong state: " + mState.name());
        break;
    }
  }

  public void seekTo(int position) {
    Logger.v(TAG, "seekTo(): State: " + mState.name() + "wasPlaying: " + wasPlaying + " position: " + position);
    switch (mState) {
      case STARTED:
      case PAUSED:
        mMediaPlayer.seekTo(position);
        if (wasPlaying) {
          this.play();
        }
        mListener.onMediaElapsed(media, mMediaPlayer.getCurrentPosition());
        break;
      case PREPARED:
        mMediaPlayer.seekTo(position);
        mListener.onMediaElapsed(media, mMediaPlayer.getCurrentPosition());
        break;
      default:
        Logger.d(TAG, "seekTo(): was called in wrong state: " + mState.name());
        break;
    }
  }

  public void seekStart() {
    Logger.v(TAG, "seekStart(): State: " + mState.name());
    switch (mState) {
      case STARTED:
      case PAUSED:
        wasPlaying = mMediaPlayer.isPlaying();
        Logger.v(TAG, "seekStart(): pausing to begin seeking. Currently Playing?: " + wasPlaying);
        this.pause();
        break;
      default:
        Logger.d(TAG, "seekStart(): was called in wrong state: " + mState.name());
        break;
    }
  }

}


