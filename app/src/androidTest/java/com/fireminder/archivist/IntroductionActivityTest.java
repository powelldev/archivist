package com.fireminder.archivist;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.fireminder.archivist.search.SearchBaseActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntroductionActivityTest {

  @Rule
  public ActivityTestRule<SearchBaseActivity> mActivityRule = new ActivityTestRule(SearchBaseActivity.class);

  @Test
  public void actionMenu_searchExists() {
    //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
    onView(ViewMatchers.withContentDescription(R.string.search));
  }

}
