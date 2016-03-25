package com.fireminder.archivist.model;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SqlTable {

  private List<Column> columns = new ArrayList<>();
  private final String tableName;

  public SqlTable(@NonNull String tableName) {
    if (tableName.contains(" ")) {
      throw new IllegalArgumentException("TableName may not contain spaces.");
    }
    this.tableName = tableName;
    setColumns(new Column(Type.INTEGER, BaseColumns._ID));
  }

  public String[] getAllColumns() {
    String[] allColumns = new String[columns.size()];
    for (int i = 0; i < columns.size(); i++) {
      allColumns[i] = columns.get(i).name;
    }
    return allColumns;
  }

  public void setColumns(@NonNull Column column) {
      this.columns.add(column);
  }

  public void setColumns(@NonNull Column[] columns) {
    for (Column column : columns) {
      this.columns.add(column);
    }
  }

  public String createTableCommand() {
    final String createTableIfNotExists = "CREATE TABLE IF NOT EXISTS ";

    StringBuilder builder = new StringBuilder();

    builder.append(createTableIfNotExists).append(this.tableName).append(" (");


    for (Column column : columns) {
      builder.append(column.createCommand()).append(", ");
    }
    builder.replace(builder.length()-2, builder.length(), ");");


    return builder.toString();
  }

  public enum Type {
    INTEGER("INTEGER"),
    REAL("REAL"),
    TEXT("TEXT"),
    BLOB("BLOB"),
    BOOLEAN("INTEGER");

    private final String type;
    Type(String type) {
      this.type = type;
    }

    public String getType() {
      if (this.equals(BOOLEAN)) {
        return INTEGER.getType();
      }
      return type;
    }
  }

  public static class Column {
    public static final String _ID = "_id";
    private final Type type;
    private final String name;

    public Column(@NonNull Type type, @NonNull String name) {
      this.type = type;
      this.name = name;
    }

    public String createCommand() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.name).append(" ")
          .append(type.getType());
      if (this.name.equals(_ID)) {
        builder.append(" PRIMARY KEY AUTOINCREMENT");
      }
      return builder.toString();
    }
  }
}
