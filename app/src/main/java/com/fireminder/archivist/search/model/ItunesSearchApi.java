package com.fireminder.archivist.search.model;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ItunesSearchApi {
  @GET("/search?media=podcast&attribute=titleTerm")
  Call<ItunesSearchResponse> searchTitle(@Query("term") String titleTerm);
}
