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


  private static final int NETWORK_THREAD_COUNT = 10;
  // Regex string to pull file name and give it name with date.
  private static final Pattern URL_FILENAME_PATTERN =
      Pattern.compile("^.*/([^/#?]+)(?:\\?.*)?(?:#.*)?$");
  // % placeholder for first parameter, t is date or time, Y is 4 digit year, m is two digit month, d is two digit,
  private static final String LOCAL_FILENAME_FORMAT = "%1$tY%1$tm%1$td-%2$s";

  private final OntrackDatabase database;
  private final OntrackService nm;
  private final Executor networkPool;

  private static Application context;

  private ImageRepository() {
    if (context == null) {
      throw new IllegalStateException();
    }
    database = OntrackDatabase.getInstance();
    nm = OntrackService.getInstance();
    // Sets so many threads. If no threads open, goes in que.
    networkPool = Executors.newFixedThreadPool(NETWORK_THREAD_COUNT);
  }

  public static void setContext(Application context) {
    ImageRepository.context = context;
  }

  public static ImageRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  // LiveData<List<Image>> selectAll();
  public LiveData<List<Image>> get() {
    return database.getImageDao().selectAll();
  }

  public Single<String> getImage(@NonNull Ontrack apod) {
    // TODO Add local file download & reference.
    boolean canBeLocal = (apod.getMediaType() == MediaType.IMAGE);
    File file = canBeLocal ? getFile(apod) : null;
    return Maybe.fromCallable(() ->
        canBeLocal ? (file.exists() ? file.toURI().toString() : null) : apod.getUrl()
    )
        .switchIfEmpty((SingleSource<String>) (observer) ->
            nasa.getFile(apod.getUrl())
                .map((body) -> {
                  try {
                    return download(body, file);
                  } catch (IOException ex) {
                    return apod.getUrl();
                  }
                })
                .subscribeOn(Schedulers.from(networkPool))
                .subscribe(observer)
        );
  }

  // Download method.
  private String download(ResponseBody body, File file) throws IOException {
    try (
        // Requesting image, recieving bytes.
        InputStream input = body.byteStream();
        // Opens file for output. Writes bytes to.
        OutputStream output = new FileOutputStream(file);
    ) {
      // Read bytes from NASA, writes to server. -1 and done.
      byte[] buffer = new byte[16_384];
      int bytesRead = 0;
      while (bytesRead >= 0) {
        bytesRead = input.read(buffer);
        if (bytesRead > 0) {
          output.write(buffer, 0, bytesRead);
        }
      }
      // Gets put in operating system. Flush writes them out.
      output.flush();
      return file.toURI().toString();
    }
  }

  // Construct file name from apod object
  private File getFile(@NonNull Apod apod) {
    String url = apod.getUrl();
    File file = null;
    Matcher matcher = URL_FILENAME_PATTERN.matcher(url);
    if (matcher.matches()) {
      // One is date and file name matched, two parameters. Catcher group 1. getdate is param 1, matcher is 2.
      String filename = String.format(LOCAL_FILENAME_FORMAT, apod.getDate(), matcher.group(1));
      // Stores this, external storage. Private to app. Stores in internal storage if no space.
      // Stores pictures, directory.
      File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      // Checks if unavailable, stores elsewhere. If directory not equal to media mounted, then new directory.
      if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(directory))) {
        // Internal storage.
        directory = context.getFilesDir();
      }
      file = new File(directory, filename);
    }
    return file;
  }

  private void insertAccess(Apod apod) {
    AccessDao accessDao = database.getAccessDao();
    Access access = new Access();
    access.setApodId(apod.getId());
    accessDao.insert(access)
        .subscribeOn(Schedulers.io())
        .subscribe(/* TODO Handle error result */);
  }

  private static class InstanceHolder {

    private static final ImageRepository INSTANCE = new ImageRepository();

  }
}