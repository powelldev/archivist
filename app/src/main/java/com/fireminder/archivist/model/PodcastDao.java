package com.fireminder.archivist.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fireminder.archivist.model.PodcastTable.Contract.*;
import static com.fireminder.archivist.model.PodcastTable.Podcast;

public class PodcastDao {

  private static final String TAG = "PodcastUtil";

  public List<Podcast> getAllPodcasts() {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver.query(IvyContentProvider.Table.Podcasts.uri, null, null, null, null);

    List<Podcast> podcasts = new ArrayList<>();

    if (cursor == null) {
      Logger.e(TAG, "getAllPodcasts() cursor null");
      return podcasts;
    }

    try {
      while (cursor.moveToNext()) {
        podcasts.add(new Podcast(cursor));
      }
    } finally {
      cursor.close();
    }
    return podcasts;
  }

  public void insert(ContentValues podcast) {
    if (podcast.get(PODCAST_UUID) == null) {
      podcast.put(PODCAST_UUID, UUID.randomUUID().toString());
    }
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    contentResolver.insert(IvyContentProvider.Table.Podcasts.uri, podcast);
  }

  public void insert(Podcast podcast) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    contentResolver.insert(IvyContentProvider.Table.Podcasts.uri, podcast.toContentValues());
  }
}
