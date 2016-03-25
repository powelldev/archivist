package com.fireminder.archivist.sync;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fireminder.archivist.IvyApplication;

/**
 * Helper class for Volley's RequestQueue implementation.
 */
public class IvyRequestQueue {
  private static IvyRequestQueue mInstance;
  private RequestQueue mRequestQueue;

  private IvyRequestQueue() {
    mRequestQueue = getRequestQueue();
  }

  public static synchronized IvyRequestQueue getInstance() {
    if (mInstance == null) {
      mInstance = new IvyRequestQueue();
    }
    return mInstance;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(IvyApplication.getAppContext());
    }
    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> request) {
    getRequestQueue().add(request);
  }
}
