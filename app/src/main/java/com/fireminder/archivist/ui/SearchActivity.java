package com.fireminder.archivist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.fireminder.archivist.FireminderExceptionHandler;
import com.fireminder.archivist.R;
import com.fireminder.archivist.search.list.SearchResultListFragment;

public class SearchActivity extends AppCompatActivity {

  static {
    if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof FireminderExceptionHandler)) {
      Thread.setDefaultUncaughtExceptionHandler(new FireminderExceptionHandler(
          "/sdcard/stacktraces", null));
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.search, menu);

    /*
    MenuItem searchItem = menu.findItem(R.id.action_search);

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

    SearchView searchView = null;
    if (searchItem != null) {
      searchView = (SearchView) searchItem.getActionView();
    }
    if (searchView != null) {
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }
    */

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getFragmentManager().beginTransaction().add(R.id.container, new SearchResultListFragment());
  }

}
