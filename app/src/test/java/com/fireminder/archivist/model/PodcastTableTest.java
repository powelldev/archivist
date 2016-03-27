package com.fireminder.archivist.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.model.PodcastTable.Podcast;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.UUID;

import static com.fireminder.archivist.model.IvyContentProvider.Table.Podcasts;
import static com.fireminder.archivist.model.PodcastTable.Contract.*;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class PodcastTableTest {

  @Before
  public void setup() {
    ShadowLog.stream = System.out;
  }

  @Test
  public void delete_test() {
    ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    Podcast podcast = new Podcast(new UUID(0, 1), "title", "description", "feed", "imgUrl");
    contentResolver.insert(Podcasts.uri, podcast.toContentValues());
    contentResolver.delete(Podcasts.uri, TITLE + " = ? ",
        SqlUtil.toArray(podcast.title));

    Cursor cursor = contentResolver.query(Podcasts.uri, null, null, null, null);
    Assert.assertTrue(!cursor.moveToFirst());
  }
  @Test
  public void insert_query_test() {
    ContentResolver contentResolver = IvyApplication.getAppContext().getContentResolver();
    Podcast podcast = new Podcast(new UUID(0, 1), "title", "description", "feed", "imgUrl");
    contentResolver.insert(Podcasts.uri, podcast.toContentValues());
    contentResolver.insert(Podcasts.uri, podcast.toContentValues());
    Cursor cursor = contentResolver.query(Podcasts.uri, null, null, null, null);
    cursor.moveToFirst();
    Podcast podcast1 = new Podcast(cursor);
    Assert.assertEquals(podcast, podcast1);
  }

  @Test
  public void testToContentValues() {

  }

  @Test
  public void foo() {
    SQLiteDatabase db = new IvyContentProvider.IvyDatabaseHelper(IvyApplication.getAppContext()).getWritableDatabase();
    String create = new EpisodeTable().createTableCommand();
    db.execSQL(create);
  }

}
