package com.fireminder.archivist.model;

import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;

import com.fireminder.archivist.IvyApplication;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class EpisodeTableTest {
  @Test
  public void createTableCommand_validateSql() {
    SQLiteDatabase db = new IvyContentProvider.IvyDatabaseHelper(IvyApplication.getAppContext()).getWritableDatabase();
    String create = new EpisodeTable().createTableCommand();
    db.execSQL(create);
  }

}
