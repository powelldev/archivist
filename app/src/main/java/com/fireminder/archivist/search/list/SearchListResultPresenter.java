package com.fireminder.archivist.search.list;

import android.content.Context;

import com.fireminder.archivist.search.SearchResult;
import com.fireminder.archivist.search.model.SearchResultsRepository;
import com.fireminder.archivist.search.result.PodcastSearchResultFragment;

import java.util.List;

public class SearchListResultPresenter implements SearchListResultContract.UserActionsListener {

  SearchResultsRepository searchResultsRepository;
  SearchListResultContract.View view;

  public SearchListResultPresenter(SearchResultsRepository searchResultsRepository,
                                   SearchListResultContract.View view) {
    this.view = view;
    this.searchResultsRepository = searchResultsRepository;
  }

  @Override
  public void openSearchResultDetail(Context context, SearchResult searchResult) {
    PodcastSearchResultFragment.createFragemnt(searchResult);
    // launch
  }

  @Override
  public void searchWithTerm(String term) {
    view.showLoadingDialog();
    SearchResultsRepository.SearchCallback callback = new SearchResultsRepository.SearchCallback() {
      @Override
      public void onSearchComplete(List<SearchResult> results) {
        view.hideLoadingDialog();
        view.populate(results);
      }
    };
    searchResultsRepository.search(term, callback);
  }

  @Override
  public void subscribe(SearchResult result) {

  }

}
