package com.fireminder.archivist.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.fireminder.archivist.IvyApplication;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

  protected static final String CACHE_FILENAME = "archivist_cache.txt";
  protected static final String LOG_FILENAME = "archivist_log.txt";

  protected static final int MAX_SIZE = 2000*1024;
  private static final String TAG = "Ivy";

  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static File sLog;

  private static HandlerThread sHandlerThread;
  private static Handler sHandler;

  public static void d(String tag, String s) {
    android.util.Log.d(TAG, tag + ": " + s);
    write(System.currentTimeMillis(), Level.DEBUG, tag, s);
  }

  public static void e(String tag, String s, Throwable t) {
    android.util.Log.e(TAG, tag + ": " + s, t);
    write(System.currentTimeMillis(), Level.ERROR, tag, s);
  }

  enum Level {
    ERROR("E"), DEBUG("D"), VERBOSE("V"), WTF("WTF");

    public final String string;

    Level(String string) {
      this.string = string;
    }
  }

  public static void v(final String tag, final String message) {
    write(System.currentTimeMillis(), Level.VERBOSE, tag, message);
  }



  public static void e(final String tag, final String message) {
    write(System.currentTimeMillis(), Level.ERROR, tag, message);
  }

  protected static void write(final long currentTimeMillis, final Level level, final String tag, final String message) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(message)) {
      return;
    }
    final String timestamp = sdf.format(new Date(currentTimeMillis));
    write(String.format("%s|%s|%s: %s", timestamp, level.string, tag, message));
  }

  protected static void write(final String message) {
    if (sLog == null) {
      try {
        sLog = createLogFile();
      } catch (IOException e) {
        android.util.Log.e(TAG, "Error establishing logger: " + e);
      }
    }

    try {
      writeToLog(sLog, message);
    } catch (IOException e) {
      android.util.Log.e(TAG, "Error writing to logger: " + e);
    }
  }


  protected static File createLogFile() throws IOException {
    final File file = new File(IvyApplication.getAppContext().getExternalCacheDir(), LOG_FILENAME);
    file.createNewFile();
    return file;
  }

  protected static void cacheFile(final String filename, final String cacheFilename) throws IOException {
    final File source = new File(IvyApplication.getAppContext().getExternalCacheDir(), filename);
    final File sink = new File(IvyApplication.getAppContext().getExternalCacheDir(), cacheFilename);
    FileUtils.copy(source, sink);
  }



  protected static synchronized void writeToLog(final File log, final String message) throws IOException {
    FileUtils.writeToFile(log, message);
    final long length = log.length();
    if (length > MAX_SIZE) {
      cacheFile(LOG_FILENAME, CACHE_FILENAME);
      final PrintWriter pw = new PrintWriter(log);
      pw.close();
    }
  }

  protected static synchronized File cocatenate(final File base, final File addition) throws IOException {
    FileUtils.appendFile(base, addition);
    return base;
  }

}
