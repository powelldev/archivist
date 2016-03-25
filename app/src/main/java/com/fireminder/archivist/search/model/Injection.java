package com.fireminder.archivist.search.model;

import com.fireminder.archivist.ui.PodcastRepository;

public class Injection {
  public static PodcastRepository providePodcastRepository() {
    throw new UnsupportedOperationException("NYI");
  }

  public static SearchResultsRepository provideSearchRepository() {
    return new ItunesSearchResultRepository();
  }
}
