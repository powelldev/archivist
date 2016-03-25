package com.fireminder.archivist;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideWrapper {

  private final Context mContext;

  public GlideWrapper(final Context context) {
    mContext = context;
  }

  public void load(String imgUrl, ImageViewTargetCallback callback) {
    Glide.with(IvyApplication.getAppContext()).load(imgUrl).asBitmap()
      .into(callback);
  }

  public void load(String imgUrl, ImageView target) {
    Glide.with(IvyApplication.getAppContext()).load(imgUrl).asBitmap()
        .into(target);
  }



}
