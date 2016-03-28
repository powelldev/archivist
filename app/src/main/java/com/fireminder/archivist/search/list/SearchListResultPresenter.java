package com.fireminder.archivist.search.list;

import android.content.Context;

import com.fireminder.archivist.model.PodcastSubscriber;
import com.fireminder.archivist.search.SearchResult;
import com.fireminder.archivist.search.model.SearchResultsRepository;
import com.fireminder.archivist.search.result.PodcastSearchResultFragment;
import com.fireminder.archivist.utils.IvyAsyncTask;
import com.fireminder.archivist.utils.Logger;

import java.util.List;

public class SearchListResultPresenter implements SearchListResultContract.UserActionsListener {

  private static final String TAG = "SearchListResultPresenter";

  private final SearchResultsRepository searchResultsRepository;
  private final SearchListResultContract.View view;
  private final PodcastSubscriber podcastSubscriber;

  public SearchListResultPresenter(SearchResultsRepository searchResultsRepository,
                                   SearchListResultContract.View view,
                                   PodcastSubscriber podcastSubscriber) {
    this.view = view;
    this.searchResultsRepository = searchResultsRepository;
    this.podcastSubscriber = podcastSubscriber;
  }

  @Override
  public void openSearchResultDetail(Context context, SearchResult searchResult) {
    PodcastSearchResultFragment.createFragment(searchResult);
    // TODO launch
  }

  @Override
  public void searchWithTerm(String term) {
    Logger.d(TAG, "searching with term: " + term);
    view.showLoadingDialog();

    SearchResultsRepository.SearchCallback callback = new SearchResultsRepository.SearchCallback() {
      @Override
      public void onSearchComplete(List<SearchResult> results) {
        Logger.d(TAG, "search complete with " + results.size() + " results");
        view.hideLoadingDialog();
        view.populate(results);
      }
    };

    searchResultsRepository.search(term, callback);
  }

  @Override
  public boolean subscribe(final SearchResult result) {
    new IvyAsyncTask() {

      @Override
      public boolean doInBackground() {
        podcastSubscriber.subscribe(result.feedUrl);
        return true;
      }
    }.execute();

    return true;
  }

}
