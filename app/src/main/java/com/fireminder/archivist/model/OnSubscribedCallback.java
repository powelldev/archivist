package com.fireminder.archivist.model;

public interface OnSubscribedCallback {
  void onSubscribed(PodcastTable.Podcast podcast);
  void onSubscribeFail(String feedUrl);
}
