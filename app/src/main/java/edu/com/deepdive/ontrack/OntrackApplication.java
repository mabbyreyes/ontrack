package edu.com.deepdive.ontrack;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
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
    OntrackDatabase.setContext(this);
    OntrackDatabase.getInstance().getImageDao().delete().subscribeOn(Schedulers.io()).subscribe();
  }

  }

