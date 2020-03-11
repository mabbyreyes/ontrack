package edu.com.deepdive.ontrack.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.com.deepdive.ontrack.OntrackApplication;
import edu.com.deepdive.ontrack.model.dao.ImageDao;
import edu.com.deepdive.ontrack.model.dao.PuzzleDao;
import edu.com.deepdive.ontrack.model.dao.TaskDao;
import edu.com.deepdive.ontrack.model.entity.Image;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.model.entity.Task;
import edu.com.deepdive.ontrack.service.OntrackDatabase.Converters;
import java.util.Date;

  @Database(
      entities = {Image.class, Puzzle.class, Task.class},
      version = 1,
      exportSchema = true
  )
  @TypeConverters({Converters.class})
  public abstract class OntrackDatabase extends RoomDatabase {

    private static final String DB_NAME = "ontrack_db";

    private static Application context;

    public static void setContext(Application context) {
      OntrackDatabase.context = context;
    }

    public static OntrackDatabase getInstance() {
      return InstanceHolder.INSTANCE;
    }

    public abstract ImageDao getImageDao();

    public abstract PuzzleDao getPuzzleDao();

    public abstract TaskDao getTaskDao();

    private static class InstanceHolder {

      private static final OntrackDatabase INSTANCE = Room.databaseBuilder(
          context, OntrackDatabase.class, DB_NAME)
          .build();

    }

    public static class Converters {

      // Dates to numbers, numbers to dates.
      @TypeConverter
      public static Long fromDate(Date date) {
        return (date != null) ? date.getTime() : null;
      }

      @TypeConverter
      public static Date fromLong(Long value) {
        return (value != null) ? new Date(value) : null;
      }

    }

  }


