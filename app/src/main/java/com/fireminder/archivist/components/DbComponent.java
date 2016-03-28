package com.fireminder.archivist.components;

import com.fireminder.archivist.model.PodcastEpisodeModelModule;
import com.fireminder.archivist.search.list.SearchResultListFragment;
import com.fireminder.archivist.ui.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PodcastEpisodeModelModule.class, NetworkModule.class })
public interface DbComponent {
  void inject(BaseActivity baseActivity);
  void inject(SearchResultListFragment fragment);
}
