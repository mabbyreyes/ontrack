package edu.com.deepdive.ontrack.controller;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;

import edu.com.deepdive.ontrack.R;
import java.util.Locale;

public class WheelActivity extends AppCompatActivity {

  private HorizontalWheelView horizontalWheelView;
  private TextView tvAngle;
  private ImageView ivRocket;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_puzzle);
    initViews();
    setupListeners();
    updateUi();
  }

  private void initViews() {
    horizontalWheelView = (HorizontalWheelView) findViewById(R.id.horizontalWheelView);
    tvAngle = findViewById(R.id.tvAngle);
    ivRocket = findViewById(R.id.ivRocket);
  }

  private void setupListeners() {
    horizontalWheelView.setListener(new HorizontalWheelView.Listener() {
      @Override
      public void onRotationChanged(double radians) {
        updateUi();
      }
    });
  }

  private void updateUi() {
    updateText();
    updateImage();
  }

  private void updateText() {
    String text = String.format(Locale.US, "%.0f°", horizontalWheelView.getDegreesAngle());
    tvAngle.setText(text);
  }

  private void updateImage() {
    float angle = (float) horizontalWheelView.getDegreesAngle();
    ivRocket.setRotation(angle);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    updateUi();
  }

}
