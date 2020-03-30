package edu.com.deepdive.ontrack;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.com.deepdive.ontrack.controller.ui.home.HomeFragment;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class CountdownFragment extends Fragment implements ScreenShotable {

  private View containerView;
  private Bitmap bitmap;
  private CountdownView mCountdown;

  public static CountdownFragment newInstance() {
    return new CountdownFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.countdown_fragment, container, false);
    mCountdown = view.findViewById(R.id.clock);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    mCountdown.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mCountdown.pause();
  }

  @Override
  public void takeScreenShot() {
    Thread thread = new Thread() {
      @Override
      public void run() {
        Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
            containerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        containerView.draw(canvas);
        CountdownFragment.this.bitmap = bitmap;
      }
    };
    thread.start();
  }

  @Override
  public Bitmap getBitmap() {
    return bitmap;
  }
}