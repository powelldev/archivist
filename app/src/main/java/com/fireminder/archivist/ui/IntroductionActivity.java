package com.fireminder.archivist.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fireminder.archivist.R;
import com.fireminder.archivist.ui.SimpleSinglePaneActivity;

public class IntroductionActivity extends SimpleSinglePaneActivity {


  @Override
  protected Fragment onCreatePane() {
    return new IntroductionFragment();
  }

  public static class IntroductionFragment extends Fragment {
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_introduction, container, false);
      return rootView;
    }
  }

}
