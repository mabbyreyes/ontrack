package edu.com.deepdive.ontrack.model.repository;

import androidx.lifecycle.LiveData;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import java.util.List;

public class TaskRepository {

//  LiveData<List<Puzzle>> select();
  public LiveData<List<Puzzle>> get(){return database.getTaskDao().select();}
}
