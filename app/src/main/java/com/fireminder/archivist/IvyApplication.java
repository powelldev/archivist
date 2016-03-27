package com.fireminder.archivist;

import android.app.Application;
import android.content.Context;

import com.fireminder.archivist.components.DaggerDbComponent;
import com.fireminder.archivist.components.DbComponent;
import com.fireminder.archivist.sync.IvyDownloadManager;

public class IvyApplication extends Application {
  
  private static IvyApplication sApplication;
  DbComponent dbComponent;

  public IvyApplication() {
    super();
    sApplication = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sApplication = this;
    dbComponent = DaggerDbComponent.builder().build();
  }

  public DbComponent getDbComponent() {
    return dbComponent;
  }

  public static IvyApplication getAppContext() {
    return sApplication;
  }

}
