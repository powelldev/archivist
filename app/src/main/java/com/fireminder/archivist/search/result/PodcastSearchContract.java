package com.fireminder.archivist.search.result;

import android.widget.ImageView;

import com.fireminder.archivist.search.SearchResult;

public interface PodcastSearchContract {

  interface View {

    void showTitle(CharSequence title);

    void changeTextColor(int color);

    void setLatestInfo(CharSequence title, long timestamp);

    void setSubtitle(CharSequence description);

    ImageView getAlbumArtView();

    ImageView getAlbumArtLargeView();
  }

  interface UserActionsListener {

    void subscribe();

    void openResult(SearchResult searchResult);

    void cancelResult();

  }

}
