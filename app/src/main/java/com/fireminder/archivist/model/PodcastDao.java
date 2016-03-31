package com.fireminder.archivist.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

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

  public void getFromUuidAsync(final String uuid, final OnPodcastLoaded callback) {
    Logger.d(TAG, "get() for uuid:" + uuid);
    new AsyncTask<Void, Void, Podcast>() {
      @Override
      protected Podcast doInBackground(Void... params) {
        final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
        final Cursor cursor = contentResolver
            .query(IvyContentProvider.Table.Podcasts.uri, null, PODCAST_UUID + " = ? ", new String[]{uuid}, null);
        return getAsync(cursor);
      }

      @Override
      protected void onPostExecute(Podcast podcast) {
        callback.onLoaded(podcast);
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
  }

  private Podcast getAsync(Cursor cursor) {

    if (cursor == null) {
      Logger.e(TAG, "get(): Cursor null");
      return null;
    }

    Podcast podcast;

    try {
      cursor.moveToFirst();
      podcast = new Podcast(cursor);
    } finally {
      cursor.close();
    }
    return podcast;
  }

  public void getFromUuid(final String uuid, final OnPodcastLoaded callback) {
    Logger.d(TAG, "get() for uuid:" + uuid);
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver
        .query(IvyContentProvider.Table.Podcasts.uri, null, PODCAST_UUID + " = ? ", new String[] {uuid}, null);
    get(cursor, callback);
  }

  private void get(Cursor cursor, final OnPodcastLoaded callback) {

    if (cursor == null) {
      Logger.e(TAG, "get(): Cursor null");
      callback.onLoaded(null);
      return;
    }

    Podcast podcast;

    try {
      cursor.moveToFirst();
      podcast = new Podcast(cursor);
    } finally {
      cursor.close();
    }
    callback.onLoaded(podcast);

  }

  public void get(final String feedUrl, final OnPodcastLoaded callback) {
    Logger.d(TAG, "get(): for feedUrl: " + feedUrl);
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver
        .query(IvyContentProvider.Table.Podcasts.uri, null, FEED + " = ? ", new String[] {feedUrl}, null);

    get(cursor, callback);
  }

  public interface OnPodcastLoaded {
    void onLoaded(@Nullable Podcast podcast);
  }
}
