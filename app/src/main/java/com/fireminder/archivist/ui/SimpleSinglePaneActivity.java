package com.fireminder.archivist.ui;

import android.app.Fragment;
import android.os.Bundle;

import com.fireminder.archivist.R;

/**
 * A {@link BaseActivity} that simply contains a single fragment. The intent used to invoke this
 * activity is forwarded to the fragment as arguments during fragment instantiation. Derived
 * activities should only need to implement {@link SimpleSinglePaneActivity#onCreatePane()}.
 */
public abstract class SimpleSinglePaneActivity extends BaseActivity {
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(getContentViewResId());

      if (savedInstanceState == null) {
        mFragment = onCreatePane();
        getFragmentManager().beginTransaction()
                .add(R.id.container, mFragment, "single_pane")
                .commit();
      } else {
        mFragment = getFragmentManager().findFragmentByTag("single_pane");
      }
    }

    protected int getContentViewResId() {
      return R.layout.activity_singlepane;
    }

    /**
     * Called in <code>onCreate</code> when the fragment constituting this activity is needed.
     * The returned fragment's arguments will be set to the intent used to invoke this activity.
     */
    protected abstract Fragment onCreatePane();

    public Fragment getFragment() {
        return mFragment;
    }
}