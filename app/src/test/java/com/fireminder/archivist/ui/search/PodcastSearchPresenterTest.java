package com.fireminder.archivist.search;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.model.PodcastTable;
import com.fireminder.archivist.ui.PodcastRepository;
import com.fireminder.archivist.search.result.PodcastSearchContract;
import com.fireminder.archivist.search.result.PodcastSearchPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;

@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class PodcastSearchPresenterTest {

  @Mock
  PodcastRepository podcastRepository;

  @Mock
  PodcastSearchContract.View view;


  ImageView spyAlbumArt;
  ImageView spyLargeAlbumArt;

  PodcastSearchPresenter presenter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    presenter = new PodcastSearchPresenter(podcastRepository, view);
    ImageView imageView = new ImageView(IvyApplication.getAppContext());
    ImageView largeView = new ImageView(IvyApplication.getAppContext());
    spyAlbumArt = Mockito.spy(imageView);
    spyLargeAlbumArt = Mockito.spy(largeView);

    Mockito.when(view.getAlbumArtView()).thenReturn(spyAlbumArt);
    Mockito.when(view.getAlbumArtLargeView()).thenReturn(spyLargeAlbumArt);
  }


  @Test
  public void openPodcast_sunnyPath() throws Exception {
    presenter.openResult(new SearchResult("", "", "", "", ""));

    ArgumentCaptor<PodcastRepository.GetPodcastCallback> callbackArgumentCaptor =
        ArgumentCaptor.forClass(PodcastRepository.GetPodcastCallback.class);
    verify(podcastRepository).getPodcast(anyLong(), callbackArgumentCaptor.capture());

    List<PodcastTable.Podcast> podcastList = new ArrayList<>();
    PodcastTable.Podcast podcast = new PodcastTable.Podcast(new UUID(0,1), "title", "description", "feed", "imgurl");
    podcastList.add(podcast);
    callbackArgumentCaptor.getValue().onPodcastLoaded(podcastList);

    verify(view).showTitle("title");
    verify(view).setDescription("description");
    verify(spyAlbumArt, Mockito.times(1)).setImageDrawable(Matchers.<Drawable>any());
    verify(spyLargeAlbumArt, Mockito.times(1)).setImageDrawable(Matchers.<Drawable>any());
  }
}