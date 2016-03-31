package com.fireminder.archivist.components;

import com.fireminder.archivist.library.LibraryFragment;
import com.fireminder.archivist.library.PodcastFragment;
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
  void inject(LibraryFragment fragment);
  void inject(PodcastFragment fragment);
}
