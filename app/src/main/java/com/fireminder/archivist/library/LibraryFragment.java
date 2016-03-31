package com.fireminder.archivist.library;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.model.EpisodeDao;
import com.fireminder.archivist.model.IvyContentProvider;
import com.fireminder.archivist.model.PodcastDao;
import com.fireminder.archivist.utils.LayoutUtils;
import com.fireminder.archivist.utils.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.fireminder.archivist.model.PodcastTable.*;

/**
 * Displays a list of the currently available podcasts.
 */
public class LibraryFragment extends Fragment implements LibraryContract.View, LoaderManager.LoaderCallbacks<Cursor> {

  private static final String TAG = "LibraryFragment";

  private LibraryContract.UserActionsListener controller;

  @Inject
  EpisodeDao episodeDao;

  @Inject
  PodcastDao podcastDao;

  @Bind(R.id.recyclerView)
  RecyclerView podcastListView;

  PodcastCursorRecyclerAdapter adapter;

  public static final int LOADER = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IvyApplication.getAppContext().getDbComponent().inject(this);
    controller = new LibraryController(this, podcastDao, episodeDao);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_library, container, false);
    ButterKnife.bind(this, rootView);
    podcastListView.setLayoutManager(new LinearLayoutManager(IvyApplication.getAppContext()));
    adapter = new PodcastCursorRecyclerAdapter(null);
    podcastListView.setAdapter(adapter);
    podcastListView.addItemDecoration(new VerticalSpaceItemDecoration());
    getLoaderManager().initLoader(LOADER, null, this);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void startFragment(Podcast podcast) {
    Intent intent = new Intent(getActivity(), PodcastActivity.class);
    intent.putExtra(PodcastActivity.EXTRA_PODCAST_UUID, podcast.id.toString());
    getActivity().startActivity(intent);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    switch(id) {
      case LOADER:
        return new CursorLoader(getActivity(),
            IvyContentProvider.Table.Podcasts.uri,
            null, // Just get every podcast
            null,
            null,
            null);
      default:
        Logger.e(TAG, "Invalid id passed in onCreateLoader");
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.changeCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.changeCursor(null);
  }

  public static class PodcastHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final ImageView image;
    public final ImageButton actions;

    public PodcastHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.title);
      image = (ImageView) itemView.findViewById(R.id.image);
      actions = (ImageButton) itemView.findViewById(R.id.actions);
    }

    public void bindPodcast(final Podcast podcast, final LibraryContract.UserActionsListener controller) {
      title.setText(podcast.title);
      Glide.with(IvyApplication.getAppContext()).load(podcast.imgUrl).into(image);
      actions.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          controller.openPodcastForDisplay(podcast);
        }
      });
    }

  }

  private class PodcastCursorRecyclerAdapter extends CursorRecyclerAdapter<PodcastHolder> {

    public PodcastCursorRecyclerAdapter(Cursor c) {
      super(c);
    }

    @Override
    public void onBindViewHolder(PodcastHolder holder, Cursor cursor) {
      Podcast podcast = new Podcast(cursor);
      holder.bindPodcast(podcast, controller);
    }

    @Override
    public PodcastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_library_item, parent, false);
      return new PodcastHolder(view);
    }
  }

  private class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      outRect.bottom = (int) LayoutUtils.dpToPx(10);
    }
  }
}
