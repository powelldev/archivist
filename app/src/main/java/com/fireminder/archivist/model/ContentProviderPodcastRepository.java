package com.fireminder.archivist.model;

import com.fireminder.archivist.ui.PodcastRepository;

import static com.fireminder.archivist.model.PodcastTable.Podcast;

public class ContentProviderPodcastRepository implements PodcastRepository {
  @Override
  public void getPodcast(long podcastId, GetPodcastCallback callback) {

  }

  @Override
  public void getPodcasts(final GetPodcastCallback callback) {
    callback.onPodcastLoaded(PodcastUtil.getAllPodcasts());
  }

  @Override
  public void savePodcast(Podcast podcast) {
  }

}
