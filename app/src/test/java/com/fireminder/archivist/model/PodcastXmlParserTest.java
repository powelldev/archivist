package com.fireminder.archivist.model;

import android.content.ContentValues;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21, resourceDir = "app/src/test/res/")
@RunWith(RobolectricTestRunner.class)
public class PodcastXmlParserTest extends TestCase {

  @Test
  public void testParse() throws Exception {
    String response = EpisodeXmlParserTest.RESPONSE;
    ContentValues cv = new PodcastXmlParser().parse(response);
    Assert.assertEquals("Dan Carlin's Hardcore History", cv.getAsString(PodcastTable.Contract.TITLE));
    Assert.assertTrue(cv.getAsString(PodcastTable.Contract.DESCRIPTION).contains("Was Alexander"));
    Assert.assertEquals("http://www.dancarlin.com/graphics/DC_HH_iTunes.jpg", cv.getAsString(PodcastTable.Contract.IMG_URL));
  }


}