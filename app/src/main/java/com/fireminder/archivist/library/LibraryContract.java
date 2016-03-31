package com.fireminder.archivist.library;

import com.fireminder.archivist.model.PodcastTable;

public interface LibraryContract {

  interface View {

    void startFragment(PodcastTable.Podcast podcast);
  }

  interface UserActionsListener {
    void openPodcastForDisplay(PodcastTable.Podcast podcast);
  }

  
}
