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
            entity = Puzzle.class,
            parentColumns = "puzzle_id",
            childColumns = "puzzle_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Task {

  @ColumnInfo(name = "task_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "puzzle_id", index = true)
  private long puzzle;

  @ColumnInfo(index = true)
  private Date start;

  @ColumnInfo(index = true)
  private Date end;

  @ColumnInfo(index = true)
  private boolean success;

  @ColumnInfo(index = true)
  private int block;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getPuzzle() {
    return puzzle;
  }

  public void setPuzzle(long puzzle) {
    this.puzzle = puzzle;
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

  public int getBlock() {
    return block;
  }

  public void setBlock(int block) {
    this.block = block;
  }
}
