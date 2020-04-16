package edu.com.deepdive.ontrack;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModel;
import edu.com.deepdive.ontrack.controller.ui.tools.DigitFlip;
import java.util.Calendar;

public class CountdownView extends LinearLayout implements Runnable {

  private static final char[] SEXAGISIMAL = new char[]{'5', '4', '3', '2', '1', '0'};

  private static final char[] DECIMAL = new char[]{'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};

  private DigitFlip mCharHighSecond;
  private DigitFlip mCharLowSecond;
  private DigitFlip mCharHighMinute;
  private DigitFlip mCharLowMinute;
  private DigitFlip mCharHighHour;
  private DigitFlip mCharLowHour;
  private View mClock = this;

  private boolean mPause = true;

  private long startedTime = System.currentTimeMillis();

  private long totalTime = 10 * 60 * 60; // 10 hours count down

  private long elapsedTime = 0;

  public CountdownView(Context context) {
    this(context, null);
  }

  public CountdownView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CountdownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public void init() {
    setOrientation(HORIZONTAL);
    inflate(getContext(), R.layout.clock, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mCharHighSecond = findViewById(R.id.charHighSecond);
    mCharLowSecond = findViewById(R.id.charLowSecond);
    mCharHighMinute = findViewById(R.id.charHighMinute);
    mCharLowMinute = findViewById(R.id.charLowMinute);
    mCharHighHour = findViewById(R.id.charHighHour);
    mCharLowHour = findViewById(R.id.charLowHour);

    mCharHighSecond.setTextSize(100);
    mCharHighSecond.setChars(SEXAGISIMAL);
    mCharLowSecond.setTextSize(100);
    mCharLowSecond.setChars(DECIMAL);

    mCharHighMinute.setTextSize(100);
    mCharHighMinute.setChars(SEXAGISIMAL);
    mCharLowMinute.setTextSize(100);
    mCharLowMinute.setChars(DECIMAL);

    mCharHighHour.setTextSize(100);
    mCharHighHour.setChars(DECIMAL);
    mCharLowHour.setTextSize(100);
    mCharLowHour.setChars(DECIMAL);
  }

  public void pause() {
    mPause = true;
    mCharHighSecond.sync();
    mCharLowSecond.sync();
    mCharHighMinute.sync();
    mCharLowMinute.sync();
    mCharHighHour.sync();
    mCharLowHour.sync();
  }

  public void resume() {
    mPause = false;

    long now = System.currentTimeMillis();
    elapsedTime = (now - startedTime) / 1000;
    totalTime -= elapsedTime;

    long time = totalTime;

    int hourHeight = (int) (time / 36000);
    mCharHighHour.setChar(9 - hourHeight);

    time -= hourHeight * 36000;

    int hourLow = (int) (time / 3600);
    mCharLowHour.setChar(9 - hourLow);

    time -= hourLow * 3600;

    int minuteHeight = (int) (time / 600);
    mCharHighMinute.setChar(5 - minuteHeight);

    time -= minuteHeight * 600;

    int minuteLow = (int) (time / 60);
    mCharLowMinute.setChar(9 - minuteLow);

    time -= minuteLow * 60;

    int secHeight = (int) (time / 10);
    mCharHighSecond.setChar(5 - secHeight);

    time -= secHeight * 10;

    int secLow = (int) time;
    mCharLowSecond.setChar(9 - secLow);

    elapsedTime = 0;
    ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
  }

  @Override
  public void run() {
    if(mPause){
      return;
    }
    mCharLowSecond.start();
    if (elapsedTime % 10 == 0) {
      mCharHighSecond.start();
    }
    if (elapsedTime % 60 == 0) {
      mCharLowMinute.start();
    }
    if (elapsedTime % 600 == 0) {
      mCharHighMinute.start();
    }
    if (elapsedTime % 3600 == 0) {
      mCharLowHour.start();
    }
    if (elapsedTime % 36000 == 0) {
      mCharHighHour.start();
    }
    elapsedTime += 1;
    ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
  }
}