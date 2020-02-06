package edu.com.deepdive.ontrack.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.com.deepdive.ontrack.model.entity.Image;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface ImageDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Image image);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Image> image);


  @Update
  int update(Image image);

  @Delete
  Single<Integer> delete(Image... images);

  @Query("SELECT * FROM Image ORDER BY title")
  LiveData<List<Image>> select();

}
