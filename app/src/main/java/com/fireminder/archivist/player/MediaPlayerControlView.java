package com.fireminder.archivist.player;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fireminder.archivist.R;

/**
 * Class represents the media controls (play/pause, next, previous, seek bar, etc). This
 * is separated out because it will occur in two locations: the bar permanantly at the bottom
 * of BaseActivities and the full PlayerFragment.
 *
 * Classes that wish to use this view must implement this class's listener to handle
 * button presses.
 */
public class MediaPlayerControlView implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
  private TextView elapsed;
  private TextView duration;
  private TextView title;
  private SeekBar seekBar;
  private ImageButton playPauseButton;

  private Listener listener;

  public MediaPlayerControlView (Context context, View inflatedView) {
    playPauseButton = (ImageButton) inflatedView.findViewById(R.id.button_play_pause);
    playPauseButton.setOnClickListener(this);
    inflatedView.findViewById(R.id.button_back_thirty).setOnClickListener(this);
    inflatedView.findViewById(R.id.button_forward_thirty).setOnClickListener(this);
    inflatedView.findViewById(R.id.button_next).setOnClickListener(this);
    inflatedView.findViewById(R.id.button_previous).setOnClickListener(this);
    elapsed = (TextView) inflatedView.findViewById(R.id.text_view_elapsed);
    duration = (TextView) inflatedView.findViewById(R.id.text_view_duration);
    title = (TextView) inflatedView.findViewById(R.id.title);
    seekBar = (SeekBar) inflatedView.findViewById(R.id.seek_bar);
    seekBar.setOnSeekBarChangeListener(this);
  }

  public void isPlaying(boolean isPlaying) {
    playPauseButton.setImageResource(!isPlaying ? R.drawable.ic_play_black_36dp : R.drawable.ic_pause_black_36dp);
  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  public void setProgress(int progress) {
    seekBar.setProgress(progress);
    elapsed.setText(millisToTimeView(progress));
  }

  public void setDuration(int duration) {
    seekBar.setMax(duration);
    this.duration.setText(millisToTimeView(duration));
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_play_pause:
        listener.onPlayPauseClicked();
        break;
      case R.id.button_back_thirty:
        listener.onRewindThirtyClicked();
        break;
      case R.id.button_forward_thirty:
        listener.onForwardThirtyClicked();
        break;
      case R.id.button_next:
        listener.onNextClicked();
        break;
      case R.id.button_previous:
        listener.onPreviousClicked();
        break;
      default:
        throw new UnsupportedOperationException("Not yet implemented");
    }
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    elapsed.setText(millisToTimeView(progress));
    listener.onSeekBarProgressChanged(progress);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    listener.onSeekBarStarted();
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    listener.onSeekBarStopped(seekBar.getProgress());
  }

  private static String millisToTimeView(int millis) {
    int ss = millis / 1000 % 60;
    int mm = millis / 1000 / 60 % 60;
    if (millis < 1000 * 60 * 60) {
      return String.format("%02d:%02d", mm, ss);
    } else {
      int hh = ((millis / (1000*60*60)) % 24);
      return String.format("%d:%02d:%02d", hh, mm, ss);
    }
  }

  public interface Listener {
    void onPlayPauseClicked();
    void onRewindThirtyClicked();
    void onForwardThirtyClicked();
    void onSeekBarStarted();
    void onSeekBarStopped(int progress);
    void onSeekBarProgressChanged(int progress);
    void onPreviousClicked();
    void onNextClicked();
  }

}

