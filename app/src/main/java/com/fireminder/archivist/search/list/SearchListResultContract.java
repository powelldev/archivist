package com.fireminder.archivist.search.list;

import android.content.Context;
import android.widget.ImageView;

import com.fireminder.archivist.search.SearchResult;

import java.util.List;

public interface SearchListResultContract {

  interface View {
    void populate(List<SearchResult> searchResults);
    void showLoadingDialog();
    void hideLoadingDialog();
  }

  interface RowView {
    void showTitle(String title);
    void showSubtitle(String subtitle);
    ImageView getImage();
  }

  interface UserActionsListener {
    void openSearchResultDetail(Context context, SearchResult result);
    void searchWithTerm(String term);
    void subscribe(SearchResult result);
  }
}
