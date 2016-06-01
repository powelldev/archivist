package com.fireminder.archivist.player;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.model.EpisodeTable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This fragment exists to:
 *  a) Show what the media controller tells it to
 *  b) Report touch events to the media controller
 */
public class PodcastPlaybackFragment extends Fragment implements MediaController.View, MediaPlayerControlView.Listener {

  private ViewCommChannel commChannel;
  private ImageView albumArt;
  private MediaPlayerControlView playerControlView;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commChannel = new ViewCommChannel(this, new MediaController.ServiceMessageReader());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_podcast_playback, container, false);
    albumArt = (ImageView) rootView.findViewById(R.id.album_art);
    playerControlView = new MediaPlayerControlView(IvyApplication.getAppContext(), rootView);
    playerControlView.setListener(this);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    commChannel.openComm(getActivity());
    commChannel.requestHandshake();
  }

  @Override
  public void onPause() {
    super.onPause();
    commChannel.closeComm(getActivity());
  }

  public void setAlbumArt(EpisodeTable.Episode episode, String backupImage) {
    try {
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      retriever.setDataSource(getActivity(), Uri.parse(episode.localUri));
      byte[] bArray = retriever.getEmbeddedPicture();
      if (bArray != null) {
        InputStream is = new ByteArrayInputStream(bArray);
        Bitmap bm = BitmapFactory.decodeStream(is);
        albumArt.setImageBitmap(bm);
      } else {
        Glide.with(getActivity()).load(backupImage).into(albumArt);
      }
    } catch (Exception e) {
      // Episode art DNE, consume
    }
  }

  private void startAnim() {
    try {
      Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation);
      albumArt.startAnimation(animation);
    } catch (NullPointerException e) {
      //TODO
    }
  }

  @Override
  public void title(String title) {
    playerControlView.setTitle(title);
  }

  @Override
  public void complete(EpisodeTable.Episode completed) {
  }

  @Override
  public void duration(long duration) {
    playerControlView.setDuration((int) duration);
  }

  @Override
  public void elapsed(long elapsed) {
    playerControlView.setProgress((int) elapsed);
  }

  @Override
  public void handshake(long duration, long elapsed, EpisodeTable.Episode nowPlaying, String imageUri) {
    // setAlbumArt
    // start the animation
    // set textviews, progress duration and nowplaying (boolean)
  }

  @Override
  public void nothingPlaying() {
    // clear imageview, text, progress, duration, nowplaying is false
  }

  @Override
  public void isPlaying(boolean b) {
    playerControlView.isPlaying(b);
  }

  @Override
  public void onPlayPauseClicked() {
    commChannel.playPause();
  }

  @Override
  public void onRewindThirtyClicked() {
    commChannel.back(ivyPrefs.getRewindAmount());
  }

  @Override
  public void onForwardThirtyClicked() {
    commChannel.forward(ivyPrefs.getForwardAmount());
  }

  @Override
  public void onSeekBarStarted() {
    commChannel.seekStart();
  }

  @Override
  public void onSeekBarStopped(int progress) {
    commChannel.seekEnd();
  }

  @Override
  public void onSeekBarProgressChanged(int progress) {
    // Since seekBar changes are handled in onSeekBarStopped I don't see a reason to use this yet.
  }

  @Override
  public void onPreviousClicked() {
    commChannel.previous();
  }

  @Override
  public void onNextClicked() {
    commChannel.next();
  }

}
