package com.fireminder.archivist.utils;

import com.fireminder.archivist.IvyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public final class FileUtils {
  public static void copy(File src, File dst) throws IOException {
    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(dst);

    try {
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } finally {
      in.close();
      out.close();
    }
  }

  public static void writeToFile(File file, String data) throws IOException {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
    try {
      outputStreamWriter.write(data);
    } finally {
      outputStreamWriter.close();
    }
  }

  public static String fileAsString(File file) throws IOException {

    String ret;

      InputStream inputStream = new FileInputStream(file);

    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String receiveString = "";
    StringBuilder stringBuilder = new StringBuilder();

    while ( (receiveString = bufferedReader.readLine()) != null ) {
      stringBuilder.append(receiveString);
    }

    inputStream.close();
    ret = stringBuilder.toString();

    return ret;
  }
  public static void appendFile(File base, File addition) throws IOException {
    InputStream in = null;
    OutputStream out = null;
    try {
      in = new FileInputStream(addition);
      out = new FileOutputStream(base, true);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } finally {
      if (out != null) {
        out.close();
      }
      if (in != null) {
        in.close();
      }
    }
  }

  public static File getEpisodeDirectory() {
    return IvyApplication.getAppContext().getFilesDir();
  }

}
