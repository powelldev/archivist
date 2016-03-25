package com.fireminder.archivist.utils;

import android.os.AsyncTask;

public abstract class IvyAsyncTask extends AsyncTask<Void, Void, Boolean> {

  public IvyAsyncTask() {
    super();
  }

  public abstract boolean doInBackground();

  /**
   * Override to implement.
   */
  public void onSuccess() {
    // NO-OP
  }

  /**
   * Override to implement.
   */
  public void onFailure() {
    // NO-OP
  }

  @Override
  protected Boolean doInBackground(Void... params) {
    boolean success = doInBackground();
    if (success) {
      onSuccess();
    } else {
      onFailure();
    }
    return success;
  }

  public void execute() {
    this.executeOnExecutor(THREAD_POOL_EXECUTOR, (Void)null);
  }

}
