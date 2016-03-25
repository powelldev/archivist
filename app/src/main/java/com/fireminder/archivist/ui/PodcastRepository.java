package com.fireminder.archivist.ui;

import java.util.List;

import static com.fireminder.archivist.model.PodcastTable.Podcast;

public interface PodcastRepository {

  interface GetPodcastCallback {

    void onPodcastLoaded(List<Podcast> podcasts);

  }

  void getPodcast(long podcastId, GetPodcastCallback callback);

  void getPodcasts(GetPodcastCallback callback);

  void savePodcast(Podcast podcast);

}
