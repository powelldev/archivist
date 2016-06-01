package com.fireminder.archivist.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import com.fireminder.archivist.model.EpisodeTable;

import static com.fireminder.archivist.model.EpisodeTable.*;
import static com.fireminder.archivist.player.MediaController.*;

/*
 * The handlers and messengers make this class particularly arduous to test.
 * For now, just test the view and player actions.
 *
 * Because I can't think of a better way to encapsulate Messenger communication,
 * good documentation will have to serve as a guide for the msg directions.
 */
public class ViewCommChannel implements Player {

  private static final int NO_ARG = -1;

  private ServiceMessageReader messageReader;
  private View view;

  public ViewCommChannel(View view, ServiceMessageReader messageReader) {
    this.view = view;
    this.messageReader = messageReader;
  }

  // This handler RECEIVES messages from the media service
  private Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_MEDIA_TITLE:
          String title = messageReader.readTitle(msg);
          view.title(title);
          break;
        case MSG_MEDIA_DURATION:
          int duration = messageReader.readDuration(msg);
          view.duration(duration);
          break;
        case MSG_MEDIA_ELAPSED:
          int elapsed = messageReader.readElapsed(msg);
          view.elapsed(elapsed);
          break;
        case MSG_MEDIA_COMPLETE:
          Episode complete = messageReader.readComplete(msg);
          view.complete(complete);
          break;
        case MSG_IS_PLAYING:
          boolean isPlaying = messageReader.readIsPlaying(msg);
          view.isPlaying(isPlaying);
          break;
        case MSG_HANDSHAKE_WITH_VIEW:
          Episode episode = msg.getData().getParcelable(EXTRA_MEDIA);
          String imageUri = msg.getData().getParcelable(EXTRA_MEDIA_CONTENT);
          view.handshake(msg.arg2, msg.arg1, episode, imageUri);
          break;
        case MSG_NOTHING_PLAYING:
          view.handshake(0, 0, null, null);
          break;
      }
      return true;
    }

  });



  private final Messenger messengerToService = new Messenger(handler);
  private Messenger serviceMessenger;

  private final ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      try {
        serviceMessenger = new Messenger(service);
        Message msg = Message.obtain(null, MSG_ADD_CLIENT);
        msg.replyTo = messengerToService;
        serviceMessenger.send(msg);
        sendMessage(MSG_HANDSHAKE_WITH_VIEW);
      } catch (RemoteException e) {
        // TODO
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
  };


  private void sendMessage(int what) {
    sendMessage(what, NO_ARG, null);
  }

  private void sendMessage(int what, int arg1) {
    sendMessage(what, arg1, null);
  }

  private void sendMessage(int what, int arg1, Bundle bundle) {
    try {
      Message msg = Message.obtain(null, what);
      msg.arg1 = arg1;
      if (bundle != null) {
        msg.setData(bundle);
      }
      serviceMessenger.send(msg);
    } catch (Exception e) {
      // Messenger not linked or not started yet.
      e.printStackTrace();
    }
  }

  public void openComm(Activity activity) {
    //getActivity().startService()
    //getActivity().bindService()
  }

  public void closeComm(Activity activity) {
    //getActivity().unbindService(connection);
  }

  @Override
  public void setData(Episode episode) {

  }

  @Override
  public void start() {

  }

  @Override
  public void playPause() {
    sendMessage(MSG_PLAY_PAUSE);
  }

  @Override
  public void seekStart() {
    sendMessage(MSG_SEEK_START);
  }

  @Override
  public void seekEnd() {
    sendMessage(MSG_SEEK_END);
  }

  @Override
  public void forward(long amount) {
    sendMessage(MSG_FORWARD_THIRTY);
  }

  @Override
  public void back(long amount) {
    sendMessage(MSG_BACK_THIRTY);
  }

  @Override
  public void next() {
    sendMessage(MSG_NEXT);
  }

  @Override
  public void previous() {
    sendMessage(MSG_PREVIOUS);
  }

  @Override
  public void requestHandshake() {
    sendMessage(MSG_REQUEST_HANDSHAKE);
  }
}
