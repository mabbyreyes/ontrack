package edu.com.deepdive.ontrack;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class OntrackApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
  }

}
