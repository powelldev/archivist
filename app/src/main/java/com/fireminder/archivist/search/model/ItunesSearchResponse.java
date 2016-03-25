package com.fireminder.archivist.search.model;

import com.fireminder.archivist.search.SearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ItunesSearchResponse {

  @SerializedName("results")
  List<SearchResult> results;

  public ItunesSearchResponse() {
    results = new ArrayList<>();
  }

  public static ItunesSearchResponse parseJson(String response) {
    Gson gson = new GsonBuilder().create();
    ItunesSearchResponse itunesSearchResponse =
        gson.fromJson(response, ItunesSearchResponse.class);
    return itunesSearchResponse;
  }
}
