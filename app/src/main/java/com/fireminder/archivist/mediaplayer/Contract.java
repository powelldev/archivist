package com.fireminder.archivist.mediaplayer;

import java.io.IOException;
import java.util.UUID;

public interface Contract {

  interface EpisodeRepository {
    Media retrieveMedia(UUID mediaId, OnMediaLoaded callback);

    interface OnMediaLoaded {
      void onMediaLoaded(Media media);
    }
  }

  interface PlayerActionListener {
    void changeMedia(UUID mediaUri);
    void onStateUpdated(PlayerController.State state);
    void onMediaElapsed(Media media, int currentPosition);
  }

  interface Player {
    void stop();
    void setDataSource(Media media, OnPlayerPrepared callback) throws IOException;

    boolean isPlaying();

    void start();

    interface OnPlayerPrepared {
      void onPlayerPrepared(Media media);
    }
  }

  class Media {

    private UUID id;
    private String uri;

    public UUID getId() {
      return id;
    }

    public String getUri() {
      return uri;
    }
  }

}
