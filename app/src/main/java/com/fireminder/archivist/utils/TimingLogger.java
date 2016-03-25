package com.fireminder.archivist.utils;

import com.fireminder.archivist.utils.Logger;

import java.util.ArrayList;

public class TimingLogger {

  private String mTag;
  private String mLabel;
  ArrayList<Long> mSplits;
  ArrayList<String> mSplitLabels;

  public TimingLogger(String tag, String label) {
    reset(tag, label);
  }

  public void reset(String tag, String label) {
    mTag = tag;
    mLabel = label;
    reset();
  }

  public void reset() {
    if (mSplits == null) {
      mSplits = new ArrayList<>();
      mSplitLabels = new ArrayList<>();
    } else {
      mSplitLabels.clear();
      mSplits.clear();
    }
    addSplit(null);
  }

  public void addSplit(String splitLabel) {
    long now = System.nanoTime();
    mSplits.add(now);
    mSplitLabels.add(splitLabel);
  }

  public void dumpToLog() {
    Logger.v(mTag, mLabel);
    final long first = mSplits.get(0);
    long now = first;
    for (int i = 1; i < mSplits.size(); i++) {
      now = mSplits.get(i);
      final String splitLabel = mSplitLabels.get(i);
      final long prev = mSplits.get(i-1);
      Logger.v(mTag, mLabel + ":  " + (now - prev) + "ms, " + splitLabel);
    }
    Logger.v(mTag, mLabel + ": end, " + (now - first) + "ms, ");
  }

}
