package edu.com.deepdive.ontrack.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import edu.com.deepdive.ontrack.controller.HorizontalWheelView.Listener;

import static edu.com.deepdive.ontrack.controller.HorizontalWheelView.SCROLL_STATE_DRAGGING;
import static edu.com.deepdive.ontrack.controller.HorizontalWheelView.SCROLL_STATE_IDLE;
import static edu.com.deepdive.ontrack.controller.HorizontalWheelView.SCROLL_STATE_SETTLING;
import static java.lang.Math.PI;

class TouchHandler extends SimpleOnGestureListener {

  private static final float SCROLL_ANGLE_MULTIPLIER = 0.002F;
  private static final float FLING_ANGLE_MULTIPLIER = 2.0E-4F;
  private static final int SETTLING_DURATION_MULTIPLIER = 1000;
  private static final Interpolator INTERPOLATOR = new DecelerateInterpolator(2.5F);

  private HorizontalWheelView view;
  private Listener listener;
  private GestureDetector gestureDetector;
  private ValueAnimator settlingAnimator;
  private boolean snapToMarks;
  private int scrollState = 0;

  TouchHandler(HorizontalWheelView view) {
    this.view = view;
    this.gestureDetector = new GestureDetector(view.getContext(), this);
  }

  void setListener(Listener listener) {
    this.listener = listener;
  }

  void setSnapToMarks(boolean snapToMarks) {
    this.snapToMarks = snapToMarks;
  }

  boolean onTouchEvent(MotionEvent event) {
    this.gestureDetector.onTouchEvent(event);
    int action = event.getActionMasked();
    if (this.scrollState != SCROLL_STATE_SETTLING
        && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)) {
      if (this.snapToMarks) {
        this.playSettlingAnimation(this.findNearestMarkAngle(this.view.getRadiansAngle()));
      } else {
        this.updateScrollStateIfRequired(SCROLL_STATE_IDLE);
      }
    }
    return true;
  }

  // done..override
  public boolean onDown(MotionEvent e) {
    this.cancelFling();
    return true;
  }

  void cancelFling() {
    if (this.scrollState == SCROLL_STATE_SETTLING) {
      this.settlingAnimator.cancel();
    }
  }

  // done.. override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    double newAngle = this.view.getRadiansAngle() + (double)(distanceX * SCROLL_ANGLE_MULTIPLIER);
    this.view.setRadiansAngle(newAngle);
    this.updateScrollStateIfRequired(SCROLL_STATE_DRAGGING);
    return true;
  }

  private void updateScrollStateIfRequired(int newState) {
    if (this.listener != null && this.scrollState != newState) {
      this.scrollState = newState;
      this.listener.onScrollStateChanged(newState);
    }
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    double endAngle = this.view.getRadiansAngle() - (double)(velocityX * FLING_ANGLE_MULTIPLIER);
    if (this.snapToMarks) {
      endAngle = (double) ((float) this.findNearestMarkAngle(endAngle));
    }
    this.playSettlingAnimation(endAngle);
    return true;
  }

  private double findNearestMarkAngle(double angle) {
    double step = 2 * PI / (double) this.view.getMarksCount();
    return (double) Math.round(angle / step) * step;
  }

  private void playSettlingAnimation(double endAngle) {
    this.updateScrollStateIfRequired(SCROLL_STATE_SETTLING);
    double startAngle = this.view.getRadiansAngle();
    int duration = (int) (Math.abs(startAngle - endAngle) * SETTLING_DURATION_MULTIPLIER);
    this.settlingAnimator = ValueAnimator.ofFloat((float) startAngle, (float) endAngle)
        .setDuration((long) duration);
    this.settlingAnimator.setInterpolator(INTERPOLATOR);
    this.settlingAnimator.addUpdateListener(this.flingAnimatorListener);
    this.settlingAnimator.addListener(this.animatorListener);
    this.settlingAnimator.start();
  }

  private AnimatorUpdateListener flingAnimatorListener =
      new AnimatorUpdateListener() {

    // done.. override.
    public void onAnimationUpdate(ValueAnimator animation) {
      TouchHandler.this.view.setRadiansAngle((double)(float) animation.getAnimatedValue());
    }
  };

  private AnimatorListener animatorListener = new AnimatorListenerAdapter() {

    // done.. override.
    public void onAnimationEnd(Animator animation) {
      TouchHandler.this.updateScrollStateIfRequired(SCROLL_STATE_IDLE);
    }
  };

}
