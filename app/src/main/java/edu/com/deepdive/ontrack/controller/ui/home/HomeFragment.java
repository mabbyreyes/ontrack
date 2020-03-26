package edu.com.deepdive.ontrack.controller.ui.home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import edu.com.deepdive.ontrack.R;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class HomeFragment extends Fragment implements ScreenShotable {

  public static final String CLOSE = "Close";
  public static final String HOME = "Home";
  public static final String JIGSAW = "Jigsaw";
  public static final String TIMELINE = "Timeline";
  public static final String SETTINGS = "Settings";

  private View containerView;
  private ImageView imageView;
  private int res;
  private Bitmap bitmap;

  public static HomeFragment newInstance(int resId) {
    HomeFragment homeFragment = new HomeFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(Integer.class.getName(), resId);
    homeFragment.setArguments(bundle);
    return homeFragment;
  }

  @Override
  public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.containerView = view.findViewById(R.id.container);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    res = getArguments().getInt(Integer.class.getName());
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    imageView = rootView.findViewById(R.id.image_content);
    imageView.setClickable(true);
    imageView.setFocusable(true);
    imageView.setImageResource(res);
    return rootView;
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
        HomeFragment.this.bitmap = bitmap;
      }
    };
    thread.start();
  }

  @Override
  public Bitmap getBitmap() {
    return bitmap;
  }

}