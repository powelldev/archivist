package com.fireminder.archivist;

import com.fireminder.archivist.utils.FileUtils;
import com.fireminder.archivist.utils.Logger;
import com.fireminder.archivist.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class FireminderExceptionHandler implements Thread.UncaughtExceptionHandler {

  public final String localpath;
  public final String desiredUrl;

  public FireminderExceptionHandler(String localpath, String desiredUrl) {
    this.localpath = localpath;
    this.desiredUrl = desiredUrl;
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    ex.printStackTrace(printWriter);
    final String stacktrace = result.toString();
    printWriter.close();
    String filename = TimeUtils.getTimestamp() + ".stacktrace";

    if (localpath != null) {
      try {
        FileUtils.writeToFile(new File(filename), stacktrace);
      } catch (IOException e) {
        Logger.e("ExceptionHandler", "Failed to write stacktrace." + e);
      }
    }

    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, ex);
  }
}
