package edu.com.deepdive.ontrack.model.repository;

import androidx.lifecycle.LiveData;
import edu.com.deepdive.ontrack.model.dao.PuzzleDao;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.model.entity.Task;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class PuzzleRepository {


  private void insertAccess (Puzzle puzzle) {
    PuzzleDao puzzleDao = database.getPuzzleDao();
    Task task = new Task();
    task.setPuzzleId(apod.getId());
    taskDao.insert(task)
        .subscribeOn(Schedulers.io())
        .subscribe(/* TODO Handle error result */);
  }

  private static class InstanceHolder {

    private static final PuzzleRepository INSTANCE = new PuzzleRepository();

    //LiveData<List<Puzzle>> selectSuccessful();
    public LiveData<List<Puzzle>> get() {return database.getPuzzleDao().selectSuccessful();}

  }
}
