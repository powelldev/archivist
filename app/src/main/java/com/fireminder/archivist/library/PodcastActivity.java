package com.fireminder.archivist.library;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.ui.BaseActivity;

public class PodcastActivity extends BaseActivity {

  public static final String EXTRA_PODCAST_UUID = "extra-podcast";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    String id = getExtraId();
    if (TextUtils.isEmpty(id)) {
      throw new IllegalArgumentException("Id was null for podcast activity");
    }
    IvyApplication.getAppContext().getDbComponent().inject(this);
    getFragmentManager().beginTransaction().add(R.id.container, PodcastFragment.getFragment(id), "podcast").commit();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  String getExtraId() {
    return (String) getIntent().getExtras().get(EXTRA_PODCAST_UUID);
  }
}
