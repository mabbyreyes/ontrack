package edu.com.deepdive.ontrack.model.repository;

import android.app.Application;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.com.deepdive.ontrack.BuildConfig;
import edu.com.deepdive.ontrack.model.dao.ImageDao;
import edu.com.deepdive.ontrack.model.dao.PuzzleDao;
import edu.com.deepdive.ontrack.model.entity.Image;
import edu.com.deepdive.ontrack.model.entity.Puzzle;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import edu.com.deepdive.ontrack.service.OntrackService;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;

public class ImageRepository {

  private final OntrackDatabase database;
  private final OntrackService nm;

  private static Application context;

  private ImageRepository() {
    if (context == null) {
      throw new IllegalStateException();
    }
    database = OntrackDatabase.getInstance();
    nm = OntrackService.getInstance();
  }

  public static void setContext(Application context) {
    ImageRepository.context = context;
  }

  public static ImageRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final ImageRepository INSTANCE = new ImageRepository();

  }
}