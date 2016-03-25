package com.fireminder.archivist;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;

import com.fireminder.archivist.model.EpisodeUtils;
import com.fireminder.archivist.model.PodcastTable;
import com.fireminder.archivist.model.PodcastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import static com.fireminder.archivist.model.EpisodeTable.Episode;
import static com.fireminder.archivist.model.PodcastTable.Podcast;

public class StatusReport {

  final AndroidInfo androidInfo;
  final AppInfo appInfo;

  StatusReport() {
    androidInfo = new AndroidInfo();
    appInfo = new AppInfo();
  }

  public static void generateAndSend(Context context) {
    Gson gson = new Gson();
    gson.toJson(new StatusReport(), StatusReport.class);
    launchShareIntent(context);
  }

  private static void launchShareIntent(Context context) {
    throw new UnsupportedOperationException("Not yet implemented.");
  }

  public static class AndroidInfo {

    private final String osVersion = getAndroidOsVersion();
    private final String deviceName = getDeviceName();
    private final Pair<Integer, Integer> widthHeightPair = getWidthHeightPair();


    private String getAndroidOsVersion() {
      return "" + Build.VERSION.SDK_INT;
    }

    private String getDeviceName() {
      String manufacturer = Build.MANUFACTURER;
      String model = Build.MODEL;
      if (model.startsWith(manufacturer)) {
        return model;
      } else {
        return manufacturer + " " + model;
      }
    }

    private Pair<Integer, Integer> getWidthHeightPair() {
      return new Pair<>(new DisplayMetrics().widthPixels, new DisplayMetrics().heightPixels);
    }

  }

  public static class AppInfo {
    private final List<PodcastTable.Podcast> podcasts = PodcastUtil.getAllPodcasts();
    private final HashMap<String, List<Episode>> podcastEpisodeMap = getPodcastEpisodeMap();


    public HashMap<String, List<Episode>> getPodcastEpisodeMap() {
      HashMap<String, List<Episode>> map = new HashMap<>();
      for (Podcast podcast : podcasts) {
        List<Episode> episodes = EpisodeUtils.getEpisodesForPodcast(podcast);
        map.put(podcast.title, episodes);
      }
      return map;
    }
  }

}
