package edu.com.deepdive.ontrack.model.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.com.deepdive.ontrack.model.dao.PuzzleDao;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.model.entity.Task;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import edu.com.deepdive.ontrack.service.OntrackService;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class PuzzleRepository {

  private final OntrackDatabase database;
 private final OntrackService nm;

  private static Application context;

  private PuzzleRepository() {
    if (context == null) {
      throw new IllegalStateException();
    }
    database = OntrackDatabase.getInstance();
    nm = OntrackService.getInstance();
  }

  public static void setContext(Application context) {
    PuzzleRepository.context = context;
  }

  public static PuzzleRepository getInstance() {
    return PuzzleRepository.InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final PuzzleRepository INSTANCE = new PuzzleRepository();

    //LiveData<List<Puzzle>> selectSuccessful();
//    public LiveData<List<Puzzle>> get() {return database.getPuzzleDao().selectSuccessful()};

  }
}
