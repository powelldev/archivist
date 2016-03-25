package com.fireminder.archivist.sync;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.model.EpisodeTable;
import com.fireminder.archivist.model.EpisodeUtils;
import com.fireminder.archivist.utils.Logger;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class IvyDownloadManager {

  private static final String TAG = "IvyDownloadManager";
  private static IvyDownloadManager sInstance;

  IvyDownloadManager() {}

  public static void init() {
    sInstance = new IvyDownloadManager();
  }

  public static IvyDownloadManager getInstance() {
    if (sInstance == null) {
      throw new IllegalArgumentException("Download manager not yet instanciated. Make sure you are" +
          "calling after Application singleton has been created");
    }
    return sInstance;
  }

  public void download(final EpisodeTable.Episode episode, boolean blocking) {
    Logger.v(TAG, "download() episode: " + episode.toString());
    EpisodeUtils.updateDownloaded(episode, EpisodeTable.DownloadStatus.FLAGGED_FOR_DOWNLOAD);
    Future<File> future = Ion.with(IvyApplication.getAppContext())
        .load(episode.streamUri)
        .progressHandler(new ProgressCallback() {
          @Override
          public void onProgress(long downloaded, long total) {
            EpisodeUtils.updateBytesDownloaded(episode, downloaded);
          }
        })
        .write(new File(EpisodeUtils.generateAndAssignFilename(episode)))
        .setCallback(new FutureCallback<File>() {
          @Override
          public void onCompleted(Exception e, File result) {
            if (e != null) {
              Logger.e(TAG, "download() episode: " + episode.toString() + " Error downloading: " + e);
              resetDownload(episode);
              return;
            }
            Logger.v(TAG, "download() episode: " + episode.title + " successful");
            EpisodeUtils.updateDownloaded(episode, EpisodeTable.DownloadStatus.DOWNLOADED);
          }
        });
    if (blocking) {
      try {
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private void resetDownload(final EpisodeTable.Episode episode) {
    EpisodeUtils.updateFilename(episode, "");
    EpisodeUtils.updateBytesDownloaded(episode, 0);
    EpisodeUtils.updateDownloaded(episode, EpisodeTable.DownloadStatus.DOWNLOAD_ATTEMPTED_FAILED);
  }

}
