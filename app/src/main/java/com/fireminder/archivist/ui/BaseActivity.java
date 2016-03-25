package com.fireminder.archivist.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fireminder.archivist.R;

public class BaseActivity extends AppCompatActivity {

  private static final String TAG = "BaseActivity";

  /*
  private DrawerLayout mDrawerLayout;
  private NavigationView mNavigationView;
  private Handler mHandler;
  */

  private Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
    }
    /*
    if (ReviewNagActivity.shouldDisplay(this)) {
      Intent intent = new Intent(this, ReviewNagActivity.class);
      startActivity(intent);
      }
    mHandler = new Handler();
    */
  }

  protected Toolbar getActionBarToolbar() {
    if (mToolbar == null) {
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      if (mToolbar != null) {
        setSupportActionBar(mToolbar);
      }
    }
    return mToolbar;
  }

  /*
  @Override
  public void setContentView(int layoutResId) {
    super.setContentView(layoutResId);
    getActionBarToolbar();
  }



  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
    }
    return super.onOptionsItemSelected(item);
  }
  */

}
