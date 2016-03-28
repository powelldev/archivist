package com.fireminder.archivist.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.model.PodcastSubscriber;
import com.fireminder.archivist.search.list.SearchResultListFragment;
import com.fireminder.archivist.ui.BaseActivity;
import com.fireminder.archivist.utils.Logger;

import javax.inject.Inject;

/**
 * Processes search requests by receiving Intent.ACTION_SEARCH intents and passing
 * the associated search term to a ListFragment.
 */
public class SearchBaseActivity extends BaseActivity {

  @Inject
  PodcastSubscriber podcastSubscriber;


  private static final String TAG = "SearchBaseActivity";
  // TODO nav drawer
  // TODO text view for failed search


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    IvyApplication.getAppContext().getDbComponent().inject(this);
  }

  @Override
  /** To avoid multiple instances of this activity being created, it is
   * flagged as android:launchMode="singleTop". We receive new intents
   * here instead of in onCreate as per
   * http://developer.android.com/guide/topics/manifest/activity-element.html*/
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (intent != null && intent.getAction().matches(Intent.ACTION_SEARCH)) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      Logger.d(TAG, "handleIntent() query received: " + query);
      getFragmentManager()
          .beginTransaction()
          .add(R.id.container, SearchResultListFragment.newInstance(query), "search_fragment")
          .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.search, menu);

    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    return super.onCreateOptionsMenu(menu);
  }

}
