package com.fireminder.archivist.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchResult implements Parcelable {

  @SerializedName("collectionName")
  public String title;

  @SerializedName("artworkUrl100")
  public String imgUrl;

  @SerializedName("feedUrl")
  public String feedUrl;

  @SerializedName("artistName")
  public String artist;

  public SearchResult(String title, String imgUrl, String feedUrl, String artist) {
    this.title = title;
    this.imgUrl = imgUrl;
    this.feedUrl = feedUrl;
    this.artist = artist;
  }

  protected SearchResult(Parcel in) {
    title = in.readString();
    imgUrl = in.readString();
    feedUrl = in.readString();
    artist = in.readString();
  }

  public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
    @Override
    public SearchResult createFromParcel(Parcel in) {
      return new SearchResult(in);
    }

    @Override
    public SearchResult[] newArray(int size) {
      return new SearchResult[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(imgUrl);
    dest.writeString(feedUrl);
    dest.writeString(artist);
  }

  @Override
  public String toString() {
    return "SearchResult{" +
        "title='" + title + '\'' +
        ", imgUrl='" + imgUrl + '\'' +
        ", feedUrl='" + feedUrl + '\'' +
        ", artist='" + artist + '\'' +
        '}';
  }
}
