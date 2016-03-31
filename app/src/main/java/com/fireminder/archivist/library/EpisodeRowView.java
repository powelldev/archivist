package com.fireminder.archivist.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fireminder.archivist.R;
import com.fireminder.archivist.model.EpisodeTable;
import com.fireminder.archivist.model.EpisodeTable.Episode;

public class EpisodeRowView extends RecyclerView.ViewHolder {

  private TextView date;
  private TextView title;
  private ImageButton action;
  private TextView actionSubtext;
  private Episode episode;

  public EpisodeRowView(View rootView) {
    super(rootView);
    date = (TextView) rootView.findViewById(R.id.date);
    title = (TextView) rootView.findViewById(R.id.title);
    action = (ImageButton) rootView.findViewById(R.id.action);
    actionSubtext = (TextView) rootView.findViewById(R.id.actionSubtext);
  }

  public void bindEpisode(Episode episode) {
    this.episode = episode;

    date.setText(toMonthDate(episode.pubDate));
    title.setText(episode.title);

    switch (episode.downloadStatus) {
      case DOWNLOADED:
        action.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
        actionSubtext.setText("" + episode.duration);
        break;
      case NOT_DOWNLOADED:
        action.setImageResource(R.drawable.ic_arrow_down_bold_circle_outline_black_48dp);
        actionSubtext.setText("" + episode.sizeInBytes);
        break;
      case DOWNLOAD_ATTEMPTED_FAILED:
        action.setImageResource(R.drawable.ic_alert_circle_outline_black_48dp);
        actionSubtext.setText("Error");
        break;
      case FLAGGED_FOR_DOWNLOAD:
        action.setImageResource(R.drawable.ic_minus_circle_outline_black_48dp);
        actionSubtext.setText("" + episode.bytesDownloaded);
        break;
    }


  }

  private String toMonthDate(long pubDate) {
    // Mar 18
    return "";
  }

}
