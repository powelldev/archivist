package com.fireminder.archivist;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class ImageViewTargetCallback extends BitmapImageViewTarget {

  private OnBitmapLoaded callback;

  public ImageViewTargetCallback(ImageView view, OnBitmapLoaded callback) {
    super(view);
    this.callback = callback;
  }

  @Override
  protected void setResource(Bitmap resource) {
    super.setResource(resource);
    callback.onLoad(resource);
  }

  public interface OnBitmapLoaded {
    void onLoad(Bitmap resource);
  }
}
