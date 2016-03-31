package com.fireminder.archivist.library;

import android.os.Bundle;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.ui.BaseActivity;

public class LibraryActivity extends BaseActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    IvyApplication.getAppContext().getDbComponent().inject(this);
    getFragmentManager().beginTransaction().add(R.id.container, new LibraryFragment(), "library").commit();
  }
}
