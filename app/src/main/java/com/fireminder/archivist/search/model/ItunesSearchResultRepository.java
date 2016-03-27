package com.fireminder.archivist.search.model;

import com.fireminder.archivist.utils.Logger;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ItunesSearchResultRepository implements SearchResultsRepository {

  private static final String TAG = "iTunesSearchResultRepo";

  @Override
  public void search(String term, final SearchCallback callback) {
    Logger.d(TAG, "searching for term:" + term);
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    ItunesSearchApi api = retrofit.create(ItunesSearchApi.class);

    final Call<ItunesSearchResponse> results = api.searchTitle(term);
    results.enqueue(new Callback<ItunesSearchResponse>() {
      @Override
      public void onResponse(Response<ItunesSearchResponse> response, Retrofit retrofit) {
        Logger.d(TAG, "onResponse: response complete.");
        callback.onSearchComplete(response.body().results);
      }

      @Override
      public void onFailure(Throwable t) {
        Logger.e(TAG, "Error occured getting search results: ", t);
      }
    });
  }

}
