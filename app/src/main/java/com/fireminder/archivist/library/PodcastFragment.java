package com.fireminder.archivist.library;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.model.EpisodeDao;
import com.fireminder.archivist.model.EpisodeTable;
import com.fireminder.archivist.model.IvyContentProvider;
import com.fireminder.archivist.model.PodcastDao;
import com.fireminder.archivist.model.PodcastTable;
import com.fireminder.archivist.sync.IvyDownloadManager;
import com.fireminder.archivist.utils.Logger;

import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PodcastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

  private static final String TAG = "PodcastFragment";

  @Inject
  EpisodeDao episodeDao;

  @Inject
  PodcastDao podcastDao;

  @Bind(R.id.recyclerView)
  RecyclerView episodeListView;

  @Bind(R.id.image)
  ImageView album;

  @Inject
  IvyDownloadManager downloadManager;

  EpisodeCursorRecyclerAdapter adapter;

  private String podcastUuid;

  public static final int LOADER = 0;

  public static final String EXTRA_PODCAST_UUID = "extra-podcast-id";

  public static PodcastFragment getFragment(String podcastUuid) {
    Bundle extras = new Bundle();
    extras.putString(EXTRA_PODCAST_UUID, podcastUuid);
    PodcastFragment fragment = new PodcastFragment();
    fragment.setArguments(extras);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IvyApplication.getAppContext().getDbComponent().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_podcast, container, false);
    ButterKnife.bind(this, rootView);

    episodeListView.setLayoutManager(new LinearLayoutManager(IvyApplication.getAppContext()));
    adapter = new EpisodeCursorRecyclerAdapter(null);
    episodeListView.setAdapter(adapter);

    this.podcastUuid = getArguments().getString(EXTRA_PODCAST_UUID);
    podcastDao.getFromUuid(podcastUuid, new PodcastDao.OnPodcastLoaded() {
      @Override
      public void onLoaded(@Nullable PodcastTable.Podcast podcast) {
        if (podcast == null) {
          Logger.e(TAG, "Podcast null, cannot load image");
          return;
        }
        Glide.with(IvyApplication.getAppContext()).load(podcast.imgUrl).into(album);
      }
    });

    getLoaderManager().initLoader(LOADER, null, this);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
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
            IvyContentProvider.Table.Episodes.uri,
            null,
            EpisodeTable.Contract.PODCAST_UUID + " = ? ",
            new String[] {podcastUuid},
            null);
      default:
        Logger.e(TAG, "Invalid id passed in onCreateLoader");
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.changeCursor(null);
  }

  private class EpisodeCursorRecyclerAdapter extends CursorRecyclerAdapter<EpisodeRowView> {

    public EpisodeCursorRecyclerAdapter(Cursor c) {
      super(c);
    }

    @Override
    public EpisodeRowView onCreateViewHolder(ViewGroup parent, int viewType) {
      View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_episode, parent, false);
      return new EpisodeRowView(rootView);
    }

    @Override
    public void onBindViewHolder(EpisodeRowView holder, Cursor cursor) {
      EpisodeTable.Episode episode = new EpisodeTable.Episode(cursor);
      holder.bindEpisode(episode, downloadManager);
    }
  }

}

