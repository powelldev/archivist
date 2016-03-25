package com.fireminder.archivist.utils;

import com.fireminder.archivist.IvyApplication;
import com.fireminder.archivist.TestUtil;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.File;


@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class LogTest {

  @Before
  public void setup() {
    ShadowLog.setupLogging();
  }

  @Test
  public void test_write_level() throws Exception {
    Logger.write(TestUtil.currentTimeMillis(), Logger.Level.DEBUG, "TAG", "MESSAGE");
    File file = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.LOG_FILENAME);
    Assert.assertEquals("2001-09-08 18:46:40|D|TAG: MESSAGE", FileUtils.fileAsString(file));
  }
  @Test
  public void test_CreateFile() throws Exception {
    Logger.createLogFile();
    File file = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.LOG_FILENAME);
    Assert.assertTrue(file.exists());
  }

  @Test
  public void test_cacheFile() throws Exception {
    File file = Logger.createLogFile();
    FileUtils.writeToFile(file, "testing");
    Logger.cacheFile(Logger.LOG_FILENAME, Logger.CACHE_FILENAME);
    File log = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.CACHE_FILENAME);
    String string = FileUtils.fileAsString(log);
    Assert.assertEquals("testing", string);
  }

  @Test
  public void test_writeToLog() throws Exception {
    File file = Logger.createLogFile();
    String randomString = TestUtil.randomString(2000 * 1024+1);
    Logger.writeToLog(file, randomString);
    File log = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.CACHE_FILENAME);
    Assert.assertEquals(0, file.length());
  }

  @Test
  public void test_cocatenateLogs() throws Exception {

    File log = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.LOG_FILENAME);
    Logger.writeToLog(log, "Test");
    File cache = new File(IvyApplication.getAppContext().getExternalCacheDir(), Logger.CACHE_FILENAME);
    Logger.writeToLog(cache, "Test");

    File file = Logger.cocatenate(cache, log);
    String string = FileUtils.fileAsString(file);
    Assert.assertNotNull(string);



  }
}
