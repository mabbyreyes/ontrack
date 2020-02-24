package edu.com.deepdive.ontrack.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.com.deepdive.ontrack.model.entity.Image;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface PuzzleDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Puzzle> puzzle);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Puzzle puzzle);

  @Delete
  Single<Integer> delete(Puzzle... puzzles);

  @Query("SELECT * FROM Puzzle WHERE success ORDER BY start")
  LiveData<List<Puzzle>> selectSuccessful();

}
