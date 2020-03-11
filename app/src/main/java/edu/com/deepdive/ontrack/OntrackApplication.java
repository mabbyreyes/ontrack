package edu.com.deepdive.ontrack;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.model.repository.ImageRepository;
import edu.com.deepdive.ontrack.model.repository.PuzzleRepository;
import edu.com.deepdive.ontrack.model.repository.TaskRepository;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import io.reactivex.schedulers.Schedulers;

public class OntrackApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);

    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .loggingEnabled(true)
            .build()
    );
    ImageRepository.setContext(this);
    PuzzleRepository.setContext(this);
    TaskRepository.setContext(this);

    OntrackDatabase.setContext(this);

    OntrackDatabase.getInstance().getImageDao().delete().subscribeOn(Schedulers.io()).subscribe();
  }



  }

