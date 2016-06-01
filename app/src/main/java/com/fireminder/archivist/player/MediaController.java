package com.fireminder.archivist.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fireminder.archivist.model.EpisodeTable;
import com.fireminder.archivist.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.fireminder.archivist.model.EpisodeTable.*;

public class MediaController {

  // Actions to perform media controls
  public static final String ACTION_PLAY = "action_play";
  public static final String ACTION_SKIP = "action_skip";
  public static final String ACTION_RESUME = "action_resume";
  public static final String ACTION_PAUSE_TOGGLE = "action_pause_toggle";
  public static final String ACTION_STOP = "action_stop";

  // Action to determine if we shut down this service when the user exits the app
  // if we're playing, stay alive, if we're paused, exit
  // if headphones are taken out while we're paused, exit
  public static final String ACTION_LEAVING = "action_leaving";

  // Extras for communicating episode data
  public static final String EXTRA_MEDIA = "extra_item_to_play";
  public static final String EXTRA_MEDIA_CONTENT = "extra_media_content";

  // Message for adding the a caller's messenger to our list of clients.
  public static final int MSG_ADD_CLIENT = 100;

  public static final int MSG_START = 1000;

  // Flags for communication between client view controls and
  // the MediaPlayer
  public static final int MSG_SET_DATA = 200;
  public static final int MSG_PLAY_PAUSE = 300;
  public static final int MSG_SEEK_START = 400;
  public static final int MSG_SEEK_END = 401;
  public static final int MSG_BACK_THIRTY = 500;
  public static final int MSG_FORWARD_THIRTY = 501;
  public static final int MSG_NEXT = 502;
  public static final int MSG_PREVIOUS = 503;


  // Flags for communication from the MediaPlayer to our clients
  public static final int MSG_MEDIA_COMPLETE = 201;

  /**
   * Msg flag for an episode's duration.
   * msg.arg1 = episode duration
   */
  public static final int MSG_MEDIA_DURATION = 600;
  /**
   * Msg flag the elapsed time for an episode
   * msg.arg1 = elapsed time
   */
  public static final int MSG_MEDIA_ELAPSED = 601;

  /**
   *
   */
  public static final int MSG_MEDIA_TITLE = 602;
  public static final int MSG_HANDSHAKE_WITH_VIEW = 700; // Flag to send duration, elapsed, album art, and episode data to the view to be updated
  public static final int MSG_NOTHING_PLAYING = 701; // Flag to send duration, elapsed, album art, and episode data to the view to be updated


  // Flag for messages that do not contain an argument
  private static final int NO_ARG = -1;
  public static final int MSG_IS_PLAYING = 702;
  public static final int MSG_REQUEST_HANDSHAKE = 703;


  // clients tell the controller what the user is doing, which buttons they're messing with

  // controller decides which of these to pass on to the player


  // player sends state updates to controllre

  // coontrolller diseeminates updates to views


  public void addClientView(View view) {

  }

  public interface Player {
    void setData(Episode episode);
    void start();
    void playPause();
    void seekStart();
    void seekEnd();
    void forward(long amount);
    void back(long amount);
    void next();
    void previous();
    void requestHandshake();
  }

  public interface View {
    void title(String title);
    void complete(Episode completed);
    void duration(long duration);
    void elapsed(long elapsed);
    void handshake(long duration, long elapsed, Episode nowPlaying, String imageUri);
    void nothingPlaying();
    void isPlaying(boolean b);
  }

  static class ServiceMessageReader {

    public String readTitle(Message msg) {
      return (String) msg.getData().get(EXTRA_MEDIA_CONTENT);
    }

    public int readDuration(Message msg) {
      return msg.arg1;
    }

    public int readElapsed(Message msg) {
      return msg.arg1;
    }
    public Episode readComplete(Message msg) {
      return msg.getData().getParcelable(EXTRA_MEDIA);
    }

    public boolean readIsPlaying(Message msg) {
      return msg.arg1 == 1;
    }


  }

  static class ServiceMessageGenerator {

    public Message getTitle(String title) {
      Bundle bundle = new Bundle();
      bundle.putString(MediaController.EXTRA_MEDIA_CONTENT, title);
      return getMessage(MSG_MEDIA_TITLE, 0, bundle);
    }

    public Message getDuration(int duration) {
      return getMessage(MSG_MEDIA_DURATION, duration);
    }

    public Message getElapsed(int elapsed) {
      return getMessage(MSG_MEDIA_ELAPSED, elapsed);
    }

    public Message getIsPlaying(boolean isPlaying) {
      return getMessage(MSG_IS_PLAYING, isPlaying ? 1 : 0);
    }

    public Message getComplete(Episode episode) {
      Bundle bundle = new Bundle();
      bundle.putParcelable(MediaController.EXTRA_MEDIA, episode);
      return getMessage(MSG_MEDIA_COMPLETE, -1, bundle);
    }

    private Message getMessage(int what) {
      return getMessage(what, NO_ARG, null);
    }

    private Message getMessage(int what, int arg1) {
      return getMessage(what, arg1, null);
    }

    private Message getMessage(int what, int arg1, Bundle bundle) {
      Message msg = Message.obtain(null, what);
      msg.arg1 = arg1;
      if (bundle != null) {
        msg.setData(bundle);
      }
      return msg;
    }
  }
}

