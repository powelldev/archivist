package com.fireminder.archivist.search.list;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.R;
import com.fireminder.archivist.search.SearchResult;
import com.fireminder.archivist.search.model.Injection;

import java.util.ArrayList;
import java.util.List;

public class SearchResultListFragment extends Fragment implements SearchListResultContract.View {

  private ProgressBar mSearchPendingProgressBar;
  private SearchListResultContract.UserActionsListener mUserActionListener;

  private RecyclerView mSearchResultListView;
  private SearchAdapter mSearchResultListAdapter;

  public static final String EXTRA_QUERY = "query";

  public static SearchResultListFragment newInstance(String query) {
    SearchResultListFragment fragment = new SearchResultListFragment();
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_QUERY, query);
    fragment.setArguments(bundle);
    return fragment;
  }

  public SearchResultListFragment() {
    // mandatory empty constructor
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mUserActionListener = new SearchListResultPresenter(Injection.provideSearchRepository(), this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSearchResultListAdapter = new SearchAdapter(new ArrayList<SearchResult>(0), mUserActionListener);
  }

  @Override
  public void onResume() {
    super.onResume();
    String query = getArguments().getString(EXTRA_QUERY);
    mUserActionListener.searchWithTerm(query);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_search_result_list, container, false);
    mSearchResultListView = (RecyclerView) root.findViewById(R.id.list);
    mSearchResultListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mSearchPendingProgressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
    mSearchResultListView.setAdapter(mSearchResultListAdapter);
    return root;
  }

  @Override
  public void populate(List<SearchResult> searchResults) {
    mSearchResultListAdapter.replaceData(searchResults);
    mSearchResultListView.setAdapter(mSearchResultListAdapter);
  }

  @Override
  public void showLoadingDialog() {
    if (mSearchPendingProgressBar != null) {
      mSearchPendingProgressBar.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void hideLoadingDialog() {
    if (mSearchPendingProgressBar != null) {
      mSearchPendingProgressBar.setVisibility(View.GONE);
    }
  }

  private static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchResult> results;
    private SearchListResultContract.UserActionsListener searchItemListener;

    public SearchAdapter(ArrayList<SearchResult> results, SearchListResultContract.UserActionsListener listener) {
      this.results = results;
      this.searchItemListener = listener;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      Context context = viewGroup.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);
      View noteView = inflater.inflate(R.layout.item_search_result, viewGroup, false);
      return new ViewHolder(noteView, searchItemListener);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
      SearchResult result = results.get(i);
      viewHolder.title.setText(result.title);
      viewHolder.subtitle.setText(result.title);
      Glide.with(IvyApplication.getAppContext()).load(result.imgUrl).into(viewHolder.cover);
    }

    @Override
    public int getItemCount() {
      return results.size();
    }

    public void replaceData(List<SearchResult> searchResults) {
      this.results = searchResults;
      notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      private SearchListResultContract.UserActionsListener searchItemListener;
      public TextView title;
      public TextView subtitle;
      public ImageView cover;
      public ImageButton action;

      public ViewHolder(View itemView, final SearchListResultContract.UserActionsListener searchItemListener) {
        super(itemView);
        this.searchItemListener = searchItemListener;
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        cover = (ImageView) itemView.findViewById(R.id.cover);
        action = (ImageButton) itemView.findViewById(R.id.action);
        itemView.setOnClickListener(this);
      }

      @Override
      public void onClick(View v) {
        int position = getAdapterPosition();
        SearchResult result = results.get(position);

        if (v.getId() == R.id.action) {
          searchItemListener.subscribe(result);
        }  else {
          searchItemListener.openSearchResultDetail(IvyApplication.getAppContext(), result);
        }
      }

    }

  }

}

