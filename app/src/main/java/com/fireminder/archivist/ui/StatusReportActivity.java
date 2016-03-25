package com.fireminder.archivist.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fireminder.archivist.R;

public class StatusReportActivity extends ActionBarActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new StatusReportFragment())
          .commit();
    }
  }

  public static class StatusReportFragment extends ListFragment {

    public static final String REPORT = "report";
    private static final String[] ALL_OPTIONS = {
        REPORT
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_status_report, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
          android.R.layout.simple_list_item_1, android.R.id.text1, ALL_OPTIONS);
      getListView().setAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      switch (ALL_OPTIONS[position]) {
        case REPORT:
          break;
      }
    }

  }
}
