package com.fireminder.archivist.components;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.fireminder.archivist.sync.IvyRequestQueue;

import java.util.concurrent.TimeUnit;

public class NetworkApi {

  private static final long DEFAULT_TIMEOUT_MILLIS = 30000;

  IvyRequestQueue requestQueue;

  NetworkApi(IvyRequestQueue ivyRequestQueue) {
    this.requestQueue = ivyRequestQueue;
  }

  public interface Callback {
    void onSuccess(String feedUrl, String resposne);
    void onFailure(String reason, Throwable tr);
  }

  public void get(String url, Callback callback) {
    RequestFuture<String> future = RequestFuture.newFuture();
    StringRequest request = new StringRequest(url, future, future);
    requestQueue.addToRequestQueue(request);
    try {
      String response = future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      callback.onSuccess(url, response);
    } catch (Exception e) {
      callback.onFailure("Exception occurred:", e);
    }
  }



}
