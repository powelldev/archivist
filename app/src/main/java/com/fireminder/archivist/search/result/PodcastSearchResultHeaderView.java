package com.fireminder.archivist.search.result;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fireminder.archivist.R;

public class PodcastSearchResultHeaderView extends RelativeLayout {

  protected final ImageView albumArtGiantBackground;
  protected final ImageView albumArt;
  private final Button subscribe;
  private final TextView title;
  private final TextView subtitle;

  public PodcastSearchResultHeaderView(Context context) {
    super(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    View rootView = inflater.inflate(R.layout.view_podcast_search_result_header, this, true);
    albumArtGiantBackground = (ImageView) rootView.findViewById(R.id.album_art_background);
    albumArt = (ImageView) rootView.findViewById(R.id.album_art);
    subscribe = (Button) rootView.findViewById(R.id.subscribe);
    title = (TextView) rootView.findViewById(R.id.title);
    subtitle = (TextView) rootView.findViewById(R.id.subtitle);
  }

  public void setListener(final PodcastSearchContract.UserActionsListener listener) {
    subscribe.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.subscribe();
      }
    });
  }

  public void setTextColor(int color) {
    title.setTextColor(color);
    subtitle.setTextColor(color);
  }

  public void setTitle(CharSequence charSequence) {
    title.setText(charSequence);
  }

  public void setSubtitle(CharSequence charSequence) {
    subscribe.setText(charSequence);
  }

  public void setAlbumArt(Bitmap bitmap) {
    albumArt.setImageBitmap(bitmap);
    albumArtGiantBackground.setImageBitmap(bitmap);
  }

}
