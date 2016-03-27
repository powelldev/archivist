package com.fireminder.archivist.sync;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fireminder.archivist.IvyApplication;

/**
 * Helper class for Volley's RequestQueue implementation.
 */
public class IvyRequestQueue {
  private RequestQueue requestQueue;

  private RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      requestQueue = Volley.newRequestQueue(IvyApplication.getAppContext());
    }
    return requestQueue;
  }

  public <T> void addToRequestQueue(Request<T> request) {
    getRequestQueue().add(request);
  }
}
