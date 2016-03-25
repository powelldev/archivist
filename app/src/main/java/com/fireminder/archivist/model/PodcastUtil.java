package com.fireminder.archivist.model;

import android.content.ContentResolver;
import android.database.Cursor;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.fireminder.archivist.model.PodcastTable.Podcast;

public class PodcastUtil {

  private static final String TAG = "PodcastUtil";

  public static List<Podcast> getAllPodcasts() {
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


}
