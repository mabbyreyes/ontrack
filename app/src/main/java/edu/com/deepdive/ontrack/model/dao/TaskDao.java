package edu.com.deepdive.ontrack.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.model.entity.Task;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface TaskDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Task> task);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  Single<Integer> update(Task task);

  @Delete
  Single<Integer> delete(Task... tasks);

  @Query("SELECT * FROM Puzzle ORDER BY start")
  LiveData<List<Puzzle>> select();
}