package com.fireminder.archivist.search.model;

import com.fireminder.archivist.search.SearchResult;

import java.util.List;

public interface SearchResultsRepository {
  void search(String term, SearchCallback callback);

  interface SearchCallback {
    void onSearchComplete(List<SearchResult> results);
  }

}
