package edu.com.deepdive.ontrack.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = Image.class,
            parentColumns = "image_id",
            childColumns = "image_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "image_id")
}
)
public class Puzzle {

  @ColumnInfo(name = "puzzle_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "image_id", index = true)
  private long image;

  @ColumnInfo(index = true)
  private Date start;

  @ColumnInfo(index = true)
  private Date end;

  @ColumnInfo(index = true)
  private boolean success;

  public long getImage() {
    return image;
  }

  public void setImage(long image) {
    this.image = image;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
