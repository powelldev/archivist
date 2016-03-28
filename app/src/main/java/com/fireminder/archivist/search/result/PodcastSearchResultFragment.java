package com.fireminder.archivist.search.result;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fireminder.archivist.R;
import com.fireminder.archivist.search.model.Injection;
import com.fireminder.archivist.search.SearchResult;

public class PodcastSearchResultFragment extends Fragment implements PodcastSearchContract.View {

  private static final String ARGUMENT_SEARCH_RESULT = "search_result";

  public static PodcastSearchResultFragment createFragment(SearchResult result) {
    Bundle arguments = new Bundle();
    arguments.putParcelable(ARGUMENT_SEARCH_RESULT, result);
    PodcastSearchResultFragment fragment = new PodcastSearchResultFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  private PodcastSearchResultHeaderView header;
  private PodcastSearchContract.UserActionsListener userActionsListener;
  private TextView latestInfo;
  private TextView description;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    userActionsListener = new PodcastSearchPresenter(Injection.providePodcastRepository(), this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_podcast_search_result, container, false);
    header = (PodcastSearchResultHeaderView) view.findViewById(R.id.header);
    header.setListener(userActionsListener);
    latestInfo = (TextView) view.findViewById(R.id.latest_info);
    description = (TextView) view.findViewById(R.id.description);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    SearchResult result = getArguments().getParcelable(ARGUMENT_SEARCH_RESULT);
    userActionsListener.openResult(result);
  }

  @Override
  public void onStop() {
    super.onStop();
    userActionsListener.cancelResult();
  }


  @Override
  public void showTitle(final CharSequence title) {
    header.setTitle(title);
  }

  @Override
  public void changeTextColor(final int color) {
    header.setTextColor(color);
  }

  @Override
  public void setLatestInfo(final CharSequence title, final long timestamp) {
    this.latestInfo.setText(timestamp + " " + title);
  }

  @Override
  public void setSubtitle(final CharSequence description) {
    this.description.setText(description);
  }

  @Override
  public final ImageView getAlbumArtView() {
    return header.albumArt;
  }

  @Override
  public final ImageView getAlbumArtLargeView() {
    return header.albumArtGiantBackground;
  }


}

