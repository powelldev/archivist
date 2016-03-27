package com.fireminder.archivist.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.utils.FileUtils;
import com.fireminder.archivist.utils.Logger;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fireminder.archivist.model.EpisodeTable.*;
import static com.fireminder.archivist.model.EpisodeTable.Contract;
import static com.fireminder.archivist.model.EpisodeTable.Episode;

public class EpisodeDao {

  private static final String TAG = "EpisodeDao";

  public void updateElapsed(final Episode media, final int currentPosition) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final ContentValues contentValues = new ContentValues(1);
    contentValues.put(Contract.ELAPSED, currentPosition);
    contentResolver.update(IvyContentProvider.Table.Episodes.uri, contentValues,
        Contract.EPISODE_UUID + " = ? ", SqlUtil.toArray(media.episodeUuid));
  }

  @Nullable
  public Episode getEpisode(@NonNull final UUID episodeUuid) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver.query(IvyContentProvider.Table.Episodes.uri, null,
        Contract.EPISODE_UUID + " = ? ", SqlUtil.toArray(episodeUuid), null);

    if (cursor == null || !cursor.moveToFirst()) {
      Logger.e(TAG, "getEpisode() cursor null or empty for episode uuid: " + episodeUuid.toString());
      return null;
    }

    try {
      return new Episode(cursor);
    } finally {
      cursor.close();
    }
  }

  public void insert(final Episode episode) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    contentResolver.insert(IvyContentProvider.Table.Episodes.uri, episode.toContentValues());
  }

  public int insert(final List<ContentValues> episodes) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    ContentValues[] cv = new ContentValues[episodes.size()];
    for (int i = 0; i < episodes.size(); i++) {
      cv[i] = episodes.get(i);
    }
    return contentResolver.bulkInsert(IvyContentProvider.Table.Episodes.uri, cv);
  }

  public void updateBytesDownloaded(final Episode media, final long downloaded) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final ContentValues contentValues = new ContentValues(1);
    contentValues.put(Contract.BYTES_DOWNLOADED, downloaded);
    contentResolver.update(IvyContentProvider.Table.Episodes.uri, contentValues,
        Contract.EPISODE_UUID + " = ? ", SqlUtil.toArray(media.episodeUuid));
  }

  public String generateFilename(final Episode episode) {
    if (TextUtils.isEmpty(episode.streamUri)) {
      Logger.e(TAG, episode.title + " has no stream uri.");
      throw new IllegalArgumentException("episode has no stream uri");
    }

    String name = FilenameUtils.getName(episode.streamUri);

    if (TextUtils.isEmpty(name)) {
      Logger.e(TAG, episode.title + " has no base uri.");
      name = episode.episodeUuid.toString();
    }

    final File file = new File(FileUtils.getEpisodeDirectory(), name);

    return file.getAbsolutePath();
  }

  public void updateFilename(final Episode media, final String filename) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final ContentValues contentValues = new ContentValues(1);
    contentValues.put(Contract.LOCAL_URI, filename);
    contentResolver.update(IvyContentProvider.Table.Episodes.uri, contentValues,
        Contract.EPISODE_UUID + " = ? ", SqlUtil.toArray(media.episodeUuid));
  }

  public String generateAndAssignFilename(final Episode episode) {
    final String filename = generateFilename(episode);
    updateFilename(episode, filename);
    return filename;
  }

  public void updateDownloaded(Episode media, DownloadStatus status) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final ContentValues contentValues = new ContentValues(1);
    contentValues.put(Contract.DOWNLOADED_STATUS, status.id);
    contentResolver.update(IvyContentProvider.Table.Episodes.uri, contentValues,
        Contract.EPISODE_UUID + " = ? ", SqlUtil.toArray(media.episodeUuid));
  }

  public List<Episode> getEpisodesForPodcast(PodcastTable.Podcast podcast) {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver.query(IvyContentProvider.Table.Episodes.uri, null,
        Contract.PODCAST_UUID + " = ? ", SqlUtil.toArray(podcast.id), null);
    return cursorToEpisodeList(cursor);
  }

  private List<Episode> cursorToEpisodeList(Cursor cursor) {
    List<Episode> episodes = new ArrayList<>();
    if (cursor == null) {
      Logger.e(TAG, "cursorToEpisodeList() cursor null");
      return episodes;
    }

    try {
      while (cursor.moveToNext()) {
        episodes.add(new Episode(cursor));
      }
    } finally {
      cursor.close();
    }
    return episodes;
  }

  public List<Episode> getEpisodesMarkedForDownload() {
    final ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    final Cursor cursor = contentResolver.query(IvyContentProvider.Table.Episodes.uri, null,
        Contract.DOWNLOADED_STATUS + " = ? ", SqlUtil.toArray(DownloadStatus.FLAGGED_FOR_DOWNLOAD.id), null);
    return cursorToEpisodeList(cursor);
  }

}
