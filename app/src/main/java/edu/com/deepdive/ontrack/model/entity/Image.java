package edu.com.deepdive.ontrack.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    indices = {
        @Index(value = "path", unique = true),
        @Index(value = "title", unique = true)
    }
)

public class Image {

  @ColumnInfo(name = "image_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private String path;

  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private String title;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
