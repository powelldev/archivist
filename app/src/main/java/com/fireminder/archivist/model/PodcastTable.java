package com.fireminder.archivist.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

import static com.fireminder.archivist.model.SqlTable.Type.*;

public class PodcastTable extends SqlTable {

  static final String NAME = "podcasts";

  public interface Contract {
    String PODCAST_UUID = "uuid";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String FEED = "feed";
    String IMG_URL = "imgUrl";
  }

  public PodcastTable() {
    super(NAME);
    Column[] columns = {
      new Column(TEXT, Contract.PODCAST_UUID),
      new Column(TEXT, Contract.TITLE),
      new Column(TEXT, Contract.DESCRIPTION),
      new Column(TEXT, Contract.FEED),
      new Column(TEXT, Contract.IMG_URL)
    };
    setColumns(columns);
  }

  public static class Podcast {
    public final UUID id;
    public final String title;
    public final String description;
    public final String feed;
    public final String imgUrl;

    public Podcast(UUID uuid, String title, String description, String feed, String imgUrl) {
      this.id = uuid;
      this.title = title;
      this.description = description;
      this.feed = feed;
      this.imgUrl = imgUrl;
    }

    public Podcast(Cursor cursor) {
      this.id = UUID.fromString(cursor.getString(cursor.getColumnIndex(Contract.PODCAST_UUID)));
      this.title = cursor.getString(cursor.getColumnIndex(Contract.TITLE));
      this.description = cursor.getString(cursor.getColumnIndex(Contract.DESCRIPTION));
      this.feed = cursor.getString(cursor.getColumnIndex(Contract.FEED));
      this.imgUrl = cursor.getString(cursor.getColumnIndex(Contract.IMG_URL));
    }


    public ContentValues toContentValues() {
      ContentValues cv = new ContentValues();
      cv.put(Contract.PODCAST_UUID, id.toString());
      cv.put(Contract.TITLE, title);
      cv.put(Contract.DESCRIPTION, description);
      cv.put(Contract.FEED, feed);
      cv.put(Contract.IMG_URL, imgUrl);
      return cv;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Podcast podcast = (Podcast) o;

      if (title != null ? !title.equals(podcast.title) : podcast.title != null) return false;
      if (description != null ? !description.equals(podcast.description) : podcast.description != null)
        return false;
      if (feed != null ? !feed.equals(podcast.feed) : podcast.feed != null) return false;
      return !(imgUrl != null ? !imgUrl.equals(podcast.imgUrl) : podcast.imgUrl != null);

    }

    @Override
    public int hashCode() {
      int result = title != null ? title.hashCode() : 0;
      result = 31 * result + (description != null ? description.hashCode() : 0);
      result = 31 * result + (feed != null ? feed.hashCode() : 0);
      result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
      return result;
    }
  }

}
