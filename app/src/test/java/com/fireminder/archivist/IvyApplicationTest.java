package com.fireminder.archivist;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class IvyApplicationTest {

  @Test
  public void getAppContextTest() throws Exception {
    Assert.assertNotNull(IvyApplication.getAppContext());
  }


}