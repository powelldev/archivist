package com.fireminder.archivist;

import android.app.Application;
import android.content.Context;

import com.fireminder.archivist.sync.IvyDownloadManager;

public class IvyApplication extends Application {
  
  private static IvyApplication sApplication;

  public IvyApplication() {
    super();
    sApplication = this;
    IvyDownloadManager.init();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sApplication = this;
  }

  public static Context getAppContext() {
    return sApplication;
  }

}
