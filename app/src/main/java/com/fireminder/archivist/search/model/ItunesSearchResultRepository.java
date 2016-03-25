package com.fireminder.archivist.search.model;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ItunesSearchResultRepository implements SearchResultsRepository {

  @Override
  public void search(String term, final SearchCallback callback) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    ItunesSearchApi api = retrofit.create(ItunesSearchApi.class);
    final Call<ItunesSearchResponse> results = api.searchTitle(term);
    results.enqueue(new Callback<ItunesSearchResponse>() {
      @Override
      public void onResponse(Response<ItunesSearchResponse> response, Retrofit retrofit) {
        callback.onSearchComplete(response.body().results);
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }

}
