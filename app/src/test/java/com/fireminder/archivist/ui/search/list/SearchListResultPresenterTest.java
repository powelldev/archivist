package com.fireminder.archivist.search.list;

import com.fireminder.archivist.model.PodcastSubscriber;
import com.fireminder.archivist.search.SearchResult;
import com.fireminder.archivist.search.model.SearchResultsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class SearchListResultPresenterTest {

  @Mock
  SearchResultsRepository repository;

  @Mock
  SearchListResultContract.View view;

  @Mock
  PodcastSubscriber podcastSubscriber;

  @Captor
  private ArgumentCaptor<SearchResultsRepository.SearchCallback> mSearchCallbackCaptor;

  SearchListResultPresenter presenter;
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    presenter = new SearchListResultPresenter(repository, view, podcastSubscriber);
  }

  @Test
  public void searchWithTerm() throws Exception {
    final List<SearchResult> results = new ArrayList<>();
    results.add(new SearchResult("", "", "", ""));

    presenter.searchWithTerm("");
    Mockito.verify(repository).search(Mockito.eq(""), mSearchCallbackCaptor.capture());
    Mockito.verify(view, Mockito.times(1)).showLoadingDialog();

    mSearchCallbackCaptor.getValue().onSearchComplete(results);
    Mockito.verify(view, Mockito.times(1)).hideLoadingDialog();
    Mockito.verify(view, Mockito.times(1)).populate(results);
  }
}