package com.fireminder.archivist.components;

import com.fireminder.archivist.model.EpisodeDao;
import com.fireminder.archivist.sync.IvyDownloadManager;
import com.fireminder.archivist.sync.IvyRequestQueue;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

  @Provides
  @Singleton
  IvyDownloadManager providesIvyDownloadManager(EpisodeDao episodeDao) {
    return new IvyDownloadManager(episodeDao);
  }

  @Provides
  @Singleton
  NetworkApi providesNetworkApi() {
    return new NetworkApi(new IvyRequestQueue());
  }
}
