package com.fireminder.archivist.search.result;

import com.fireminder.archivist.ui.PodcastRepository;
import com.fireminder.archivist.search.SearchResult;

public class PodcastSearchPresenter implements PodcastSearchContract.UserActionsListener {

  final PodcastRepository podcastRepository;
  final PodcastSearchContract.View searchResultView;

  public PodcastSearchPresenter(PodcastRepository podcastRepository,
                                PodcastSearchContract.View searchResultView) {
    this.podcastRepository = podcastRepository;
    this.searchResultView = searchResultView;
  }

  @Override
  public void subscribe() {

  }

  @Override
  public void openResult(SearchResult searchResult) {
    searchResultView.showTitle(searchResult.title);
    searchResultView.setSubtitle(searchResult.artist);

    /*
    Glide.with(IvyApplication.getAppContext()).load(searchResult.imgUrl).asBitmap()
        .into(new ImageViewTargetCallback(searchResultView.getAlbumArtView(),
            new ImageViewTargetCallback.OnBitmapLoaded() {
              @Override
              public void onLoad(Bitmap resource) {
                Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                  @Override
                  public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getLightVibrantSwatch();
                    searchResultView.changeTextColor(swatch.getTitleTextColor());
                  }
                });
              }
            }));

    Glide.with(IvyApplication.getAppContext()).load(searchResult.imgUrl).asBitmap()
        .into(searchResultView.getAlbumArtLargeView());

        */
  }

  @Override
  public void cancelResult() {
    // No long running background calls, no need to cancel.
  }
}

