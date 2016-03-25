package com.fireminder.archivist.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.fireminder.archivist.utils.Logger;

public class IvyContentProvider extends ContentProvider {

  private static final String TAG = "IvyContentProvider";

  public static final String AUTHORITY = "com.fireminder.archivist.provider";
  public static final String CONTENT_URL = "content://" + AUTHORITY;

  public static final Uri CONTENT_URI = Uri.parse(CONTENT_URL);

  private static final String DATABASE_NAME = "ivy";

  public static final int DB_VERSION_CURR = 1;
  public static final int DB_VERSION_PREV = 1;

  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  private SQLiteDatabase mDb;

  @Override
  public boolean onCreate() {
    sUriMatcher.addURI(AUTHORITY, Table.Podcasts.name, Table.Podcasts.id);
    sUriMatcher.addURI(AUTHORITY, Table.Episodes.name, Table.Episodes.id);

    IvyDatabaseHelper ivyHelper = new IvyDatabaseHelper(getContext());
    mDb = ivyHelper.getWritableDatabase();

    return true;
  }

  public enum Table {
    Episodes(1, EpisodeTable.NAME, new EpisodeTable().createTableCommand()),
    Podcasts(2, PodcastTable.NAME, new PodcastTable().createTableCommand())
    ;

    public final int id;
    public final String name;
    public final Uri uri;
    public final String create;

    Table(int id, String name, String create) {
      this.id = id;
      this.name = name;
      this.uri = CONTENT_URI.buildUpon().appendPath(name).build();
      this.create = create;
    }

    static Table from(int id) {
      for (Table table : Table.values()) {
        if (id == table.id) {
          return table;
        }
      }
      throw new IllegalArgumentException("Unknown id: " + id);
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Table table = Table.from(sUriMatcher.match(uri));
    SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    builder.setTables(table.name);
    Cursor cursor = builder.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);

    if (getContext() != null) {
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
    }

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    Table table = Table.from(sUriMatcher.match(uri));
    long id = mDb.insertOrThrow(table.name, null, values);
    Uri insertedUri = table.uri.buildUpon().appendPath(String.valueOf(id)).build();
    notifyChange(insertedUri);
    return insertedUri;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    Table table = Table.from(sUriMatcher.match(uri));
    int count = 0;
    try {
      count = mDb.delete(table.name, selection, selectionArgs);
    } catch (Exception ex) {
      Logger.e(TAG, "delete() Error during delete " + ex);
    }
    notifyChange(uri);
    return count;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    Table table = Table.from(sUriMatcher.match(uri));
    int count = 0;
    try {
      count = mDb.update(table.name, values, selection, selectionArgs);
    } catch (Exception ex) {
      Logger.e(TAG, "update() Error during update" + ex);
    }
    notifyChange(uri);
    return count;
  }

  private void notifyChange(Uri uri) {
    getContext().getContentResolver().notifyChange(uri, null);
  }


  protected static class IvyDatabaseHelper extends SQLiteOpenHelper {

    public IvyDatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DB_VERSION_CURR);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      for (Table table : Table.values()) {
        db.execSQL(table.create);
      }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // NO OP yet
    }

  }

}
