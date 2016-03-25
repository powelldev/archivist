package com.fireminder.archivist.model;

import android.provider.BaseColumns;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = "app/src/main/AndroidManifest.xml", sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ColumnTest {

  @Test
  public void testCreateCommand() throws Exception {
    SqlTable.Column column = new SqlTable.Column(SqlTable.Type.INTEGER, BaseColumns._ID);
    String createColumn = column.createCommand();
    Assert.assertEquals("_id INTEGER PRIMARY KEY AUTOINCREMENT", createColumn);
  }

  @Test
  public void testCreateCommand_booleans() throws Exception {
    SqlTable.Column column = new SqlTable.Column(SqlTable.Type.BOOLEAN, "IS_SOMETHING");
    String createColumn = column.createCommand();
    Assert.assertEquals("IS_SOMETHING INTEGER ".trim(), createColumn.trim());
  }

  @Test
  public void testCreate() {
    SqlTable table = new SqlTable("table_name") {
    };
    String create  = table.createTableCommand();
    Assert.assertEquals("CREATE TABLE IF NOT EXISTS table_name (_id INTEGER PRIMARY KEY AUTOINCREMENT);", create);
  }

  @Test
  public void testAllColumns() {
    SqlTable table = new SqlTable("table_name") {
    };
    String[] columns = table.getAllColumns();
    Assert.assertEquals("_id", columns[0]);
  }

}