package edu.com.deepdive.ontrack.controller.ui.timeline;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import edu.com.deepdive.ontrack.R;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class SlideshowFragment extends Fragment implements ScreenShotable {

  private Bitmap bitmap;
  private View containerView;

  private SlideshowViewModel slideshowViewModel;

  public static SlideshowFragment newInstance() {
    return new SlideshowFragment();
  }

  @Override
  public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.containerView = view.findViewById(R.id.container);
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    slideshowViewModel =
        ViewModelProviders.of(this).get(SlideshowViewModel.class);
    View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
    final TextView textView = root.findViewById(R.id.text_slideshow);
    slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    return root;
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
        SlideshowFragment.this.bitmap = bitmap;
      }
    };
    thread.start();
  }

  @Override
  public Bitmap getBitmap() {
    return bitmap;
  }
}