package com.fireminder.archivist.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class EpisodeTable extends SqlTable {

  static final String NAME = "episodes";

  public interface Contract {
    String TITLE = "title";
    String DESCRIPTION = "description";
    String STREAM_URI = "stream_uri";
    String LOCAL_URI = "local_uri";
    String PUBLICATION_DATE = "publication_date";
    String DURATION = "duration";
    String ELAPSED = "elapsed";
    String PODCAST_UUID = "podcast_uuid";
    String EPISODE_UUID = "episode_uuid";
    String DOWNLOADED_STATUS = "downloaded_status";
    String WAS_LISTENED_TO = "was_listened_to";
    String SIZE_IN_BYTES = "size_in_bytes";
    String BYTES_DOWNLOADED = "bytes_downloaded";
  }

  public enum DownloadStatus {
    DOWNLOADED(0), NOT_DOWNLOADED(1), FLAGGED_FOR_DOWNLOAD(2), DOWNLOAD_ATTEMPTED_FAILED(3);

    public final int id;
    DownloadStatus(int i) {
      this.id = i;
    }

    public static DownloadStatus from(int id) {
      for (DownloadStatus status : DownloadStatus.values()) {
        if (id == status.id) {
          return status;
        }
      }
      throw new UnsupportedOperationException("No download status for id: " + id);
    }
  }

  public EpisodeTable() {
    super(NAME);
    Column[] columns = {
        new Column(Type.TEXT, Contract.TITLE),
        new Column(Type.TEXT, Contract.DESCRIPTION),
        new Column(Type.TEXT, Contract.STREAM_URI),
        new Column(Type.TEXT, Contract.LOCAL_URI),
        new Column(Type.TEXT, Contract.EPISODE_UUID),
        new Column(Type.INTEGER, Contract.PUBLICATION_DATE),
        new Column(Type.INTEGER, Contract.DURATION),
        new Column(Type.INTEGER, Contract.ELAPSED),
        new Column(Type.INTEGER, Contract.PODCAST_UUID),
        new Column(Type.INTEGER, Contract.DOWNLOADED_STATUS),
        new Column(Type.BOOLEAN, Contract.WAS_LISTENED_TO),
        new Column(Type.INTEGER, Contract.SIZE_IN_BYTES),
        new Column(Type.INTEGER, Contract.BYTES_DOWNLOADED)
    };
    setColumns(columns);
  }

  public static class Episode {
    public final String title;
    public final String description;
    public final String streamUri;
    public final String localUri;
    public final long pubDate;
    public final long duration;
    public final long elapsed;
    public final UUID podcastUuid;
    public final UUID episodeUuid;
    public final DownloadStatus downloadStatus;
    public final boolean wasListenedTo;
    public final long sizeInBytes;
    public final long bytesDownloaded;

    @Override
    public String toString() {
      return "Episode{" +
          "title='" + title + '\'' +
          ", description='" + description + '\'' +
          ", streamUri='" + streamUri + '\'' +
          ", localUri='" + localUri + '\'' +
          ", pubDate=" + pubDate +
          ", duration=" + duration +
          ", elapsed=" + elapsed +
          ", podcastUuid=" + podcastUuid +
          ", episodeUuid=" + episodeUuid +
          ", downloadStatus=" + downloadStatus +
          ", wasListenedTo=" + wasListenedTo +
          ", sizeInBytes=" + sizeInBytes +
          ", bytesDownloaded=" + bytesDownloaded +
          '}';
    }

    public Episode(String title, String description, String streamUri, String localUri, long pubDate, long duration, long elapsed, UUID podcastUuid, UUID episodeUuid, DownloadStatus downloadStatus, boolean isComplete, long sizeInBytes, long bytesDownloaded) {
      this.title = title;
      this.description = description;
      this.streamUri = streamUri;
      this.localUri = localUri;
      this.pubDate = pubDate;
      this.duration = duration;
      this.elapsed = elapsed;
      this.podcastUuid = podcastUuid;
      this.episodeUuid = episodeUuid;
      this.downloadStatus = downloadStatus;
      this.wasListenedTo = isComplete;
      this.sizeInBytes = sizeInBytes;
      this.bytesDownloaded = bytesDownloaded;
    }

    public Episode(Cursor cursor) {
      title = cursor.getString(cursor.getColumnIndex(Contract.TITLE));
      description = cursor.getString(cursor.getColumnIndex(Contract.DESCRIPTION));
      streamUri = cursor.getString(cursor.getColumnIndex(Contract.STREAM_URI));
      localUri = cursor.getString(cursor.getColumnIndex(Contract.LOCAL_URI));
      pubDate = cursor.getInt(cursor.getColumnIndex(Contract.PUBLICATION_DATE));
      duration = cursor.getInt(cursor.getColumnIndex(Contract.DURATION));
      elapsed = cursor.getInt(cursor.getColumnIndex(Contract.ELAPSED));
      podcastUuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Contract.PODCAST_UUID)));
      episodeUuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Contract.EPISODE_UUID)));
      downloadStatus = DownloadStatus.from(cursor.getInt(cursor.getColumnIndex(Contract.DOWNLOADED_STATUS)));
      wasListenedTo = cursor.getInt(cursor.getColumnIndex(Contract.WAS_LISTENED_TO)) > 0;
      sizeInBytes = cursor.getInt(cursor.getColumnIndex(Contract.SIZE_IN_BYTES));
      bytesDownloaded = cursor.getInt(cursor.getColumnIndex(Contract.BYTES_DOWNLOADED));
    }

    public ContentValues toContentValues() {
      ContentValues values = new ContentValues();
      values.put(Contract.TITLE, title);
      values.put(Contract.DESCRIPTION, description);
      values.put(Contract.STREAM_URI, streamUri);
      values.put(Contract.LOCAL_URI, localUri);
      values.put(Contract.PUBLICATION_DATE, pubDate);
      values.put(Contract.DURATION, duration);
      values.put(Contract.ELAPSED, elapsed);
      values.put(Contract.PODCAST_UUID, podcastUuid.toString());
      values.put(Contract.EPISODE_UUID, episodeUuid.toString());
      values.put(Contract.DOWNLOADED_STATUS, downloadStatus.id);
      values.put(Contract.WAS_LISTENED_TO, wasListenedTo);
      values.put(Contract.SIZE_IN_BYTES, sizeInBytes);
      values.put(Contract.BYTES_DOWNLOADED, bytesDownloaded);
      return values;
    }
  }
}
