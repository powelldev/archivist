package com.fireminder.archivist.model;

import com.fireminder.archivist.components.NetworkApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PodcastEpisodeModelModule {

  @Provides
  @Singleton
  PodcastDao providesPodcastDao() {
    return new PodcastDao();
  }

  @Provides
  @Singleton
  EpisodeDao providesEpisodeDao() {
    return new EpisodeDao();
  }

  @Provides
  @Singleton
  PodcastEpisodeModel providesPodcastEpisodeModel(NetworkApi networkApi, PodcastDao podcastDao,
    EpisodeDao episodeDao) {
    return new PodcastEpisodeModel(networkApi, podcastDao, episodeDao);
  }

}
