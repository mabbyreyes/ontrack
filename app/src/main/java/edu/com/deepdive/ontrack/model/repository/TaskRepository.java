package edu.com.deepdive.ontrack.model.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import edu.com.deepdive.ontrack.service.OntrackService;
import java.util.List;

public class TaskRepository {


  private final OntrackDatabase database;
  private final OntrackService nm;

  private static Application context;

  private TaskRepository() {
    if (context == null) {
      throw new IllegalStateException();
    }
    database = OntrackDatabase.getInstance();
    nm = OntrackService.getInstance();
  }

  public static void setContext(Application context) {
    TaskRepository.context = context;
  }

  public static TaskRepository getInstance() {
    return TaskRepository.InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final TaskRepository INSTANCE = new TaskRepository();

//  public LiveData<List<Puzzle>> get() {
//    return database.getTaskDao().select();
//  }};
  }
}
