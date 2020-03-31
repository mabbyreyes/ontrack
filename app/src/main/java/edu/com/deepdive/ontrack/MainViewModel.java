package edu.com.deepdive.ontrack;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;
import java.util.HashSet;
import java.util.Set;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Set<String>> permissions;

  public MainViewModel(@NonNull Application application) {
    super(application);
    throwable = new MutableLiveData<>();
    permissions = new MutableLiveData<>(new HashSet<>());
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Set<String>> getPermissions() {
    return permissions;
  }

  public void grantPermission(String permission) {
    Set<String> permissions = this.permissions.getValue();
    if (permissions.add(permission)) {
      this.permissions.setValue(permissions);
    }
  }

  public void revokePermission(String permission) {
    Set<String> permissions = this.permissions.getValue();
    if (permissions.remove(permission)) {
      this.permissions.setValue(permissions);
    }
  }

}
