package com.fireminder.archivist.library;

import android.support.annotation.Nullable;

import com.fireminder.archivist.model.EpisodeDao;
import com.fireminder.archivist.model.PodcastDao;
import com.fireminder.archivist.model.PodcastTable;
import com.fireminder.archivist.utils.Logger;

import static com.fireminder.archivist.library.LibraryContract.*;

public class LibraryController implements UserActionsListener {

  private static final String TAG = "LibraryController";

  View view;
  PodcastDao podcasts;
  EpisodeDao episodes;

  public LibraryController(View view, PodcastDao podcastDao, EpisodeDao episodeDao) {
    this.view = view;
    this.podcasts = podcastDao;
    this.episodes = episodeDao;
  }


  @Override
  public void openPodcastForDisplay(PodcastTable.Podcast podcast) {
    if (podcast == null) {
      Logger.e(TAG, "openPodcastForDisplay(): No podcast available");
      return;
    }
    view.startFragment(podcast);
  }
}
