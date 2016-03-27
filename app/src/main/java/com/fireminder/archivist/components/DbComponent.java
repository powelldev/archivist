package com.fireminder.archivist.components;

import com.fireminder.archivist.model.PodcastEpisodeModel;
import com.fireminder.archivist.model.PodcastEpisodeModelModule;
import com.fireminder.archivist.ui.BaseActivity;

import dagger.Component;

@Component(modules = { PodcastEpisodeModelModule.class, NetworkModule.class })
public interface DbComponent {
  void inject(BaseActivity baseActivity);
}
