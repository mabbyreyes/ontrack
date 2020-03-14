package edu.com.deepdive.ontrack.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import edu.com.deepdive.ontrack.R;

import static java.lang.Math.PI;


public class HorizontalWheelView extends View {

  private static final int DP_DEFAULT_WIDTH = 200;
  private static final int DP_DEFAULT_HEIGHT = 32;
  private static final int DEFAULT_MARKS_COUNT = 40;
  private static final int DEFAULT_NORMAL_COLOR = 0xffffffff;
  private static final int DEFAULT_ACTIVE_COLOR = 0xff54acf0;
  private static final boolean DEFAULT_SHOW_ACTIVE_RANGE = true;
  private static final boolean DEFAULT_SNAP_TO_MARKS = false;
  private static final boolean DEFAULT_END_LOCK = false;
  private static final boolean DEFAULT_ONLY_POSITIVE_VALUES = false;

  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_SETTLING = 2;

  private Glider glider = new Glider(this);
  private TouchHandler touchHandler = new TouchHandler(this);
  private double angle;
  private boolean onlyPositiveValues;
  private boolean endLock;
  private HorizontalWheelView.Listener listener;

  public HorizontalWheelView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.readAttrs(attrs);
  }

  private void readAttrs(AttributeSet attrs) {
    TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalWheelView);
    int marksCount = a.getInt(R.styleable.HorizontalWheelView_marksCount, DEFAULT_MARKS_COUNT);
    this.glider.setMarksCount(marksCount);
    int normalColor = a.getColor(R.styleable.HorizontalWheelView_normalColor, DEFAULT_NORMAL_COLOR);
    this.glider.setNormalColor(normalColor);
    int activeColor = a.getColor(R.styleable.HorizontalWheelView_activeColor, DEFAULT_ACTIVE_COLOR);
    this.glider.setActiveColor(activeColor);
    boolean showActiveRange = a.getBoolean(R.styleable.HorizontalWheelView_showActiveRange,
        DEFAULT_SHOW_ACTIVE_RANGE);
    this.glider.setShowActiveRange(showActiveRange);
    boolean snapToMarks = a.getBoolean(R.styleable.HorizontalWheelView_snapToMarks, DEFAULT_SNAP_TO_MARKS);
    this.touchHandler.setSnapToMarks(snapToMarks);
    this.endLock = a.getBoolean(R.styleable.HorizontalWheelView_endLock, DEFAULT_END_LOCK);
    this.onlyPositiveValues = a.getBoolean(R.styleable.HorizontalWheelView_onlyPositiveValues,
        DEFAULT_ONLY_POSITIVE_VALUES);
    a.recycle();
  }

  public void setListener(HorizontalWheelView.Listener listener) {
    this.listener = listener;
    this.touchHandler.setListener(listener);
  }

  public void setRadiansAngle(double radians) {
    if (!this.checkEndLock(radians)) {
      this.angle = radians % (2 * PI);
    }
    if (this.onlyPositiveValues && this.angle < 0) {
      this.angle += 2 * PI;
    }

    this.invalidate();
    if (this.listener != null) {
      this.listener.onRotationChanged(this.angle);
    }
  }

  private boolean checkEndLock(double radians) {
    if (!this.endLock) {
      return false;
    } else {
      boolean hit = false;
      if (radians >= 2 * PI) {
        this.angle = Math.nextAfter(2 * PI, Double.NEGATIVE_INFINITY);
        hit = true;
      } else if (this.onlyPositiveValues && radians < 0) {
        this.angle = 0;
        hit = true;
      } else if (radians <= -2 * PI) {
        this.angle = Math.nextAfter(-2 * PI, Double.POSITIVE_INFINITY);
        hit = true;
      }
      if (hit) {
        this.touchHandler.cancelFling();
      }
      return hit;
    }
  }

  public void setDegreesAngle(double degrees) {
    double radians = degrees * PI / 180;
    this.setRadiansAngle(radians);
  }

  public void setCompleteTurnFraction(double fraction) {
    double radians = fraction * 2 * PI;
    this.setRadiansAngle(radians);
  }

  public double getRadiansAngle() {
    return this.angle;
  }

  public double getDegreesAngle() {
    return this.getRadiansAngle() * 180 / PI;
  }

  public double getCompleteTurnFraction() {
    return this.getRadiansAngle() / (2 * PI);
  }

  public void setOnlyPositiveValues(boolean onlyPositiveValues) {
    this.onlyPositiveValues = onlyPositiveValues;
  }

  public void setEndLock(boolean lock) {
    this.endLock = lock;
  }

  public void setMarksCount(int marksCount) {
    this.glider.setMarksCount(marksCount);
    this.invalidate();
  }

  public void setShowActiveRange(boolean show) {
    this.glider.setShowActiveRange(show);
    this.invalidate();
  }

  public void setNormaColor(int color) {
    this.glider.setNormalColor(color);
    this.invalidate();
  }

  public void setActiveColor(int color) {
    this.glider.setActiveColor(color);
    this.invalidate();
  }

  public void setSnapToMarks(boolean snapToMarks) {
    this.touchHandler.setSnapToMarks(snapToMarks);
  }

  // override
  public boolean onTouchEvent(MotionEvent event) {
    return this.touchHandler.onTouchEvent(event);
  }

  //override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    this.glider.onSizeChanged();
  }

  // override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int resolvedWidthSpec = this.resolveMeasureSpec(widthMeasureSpec, DP_DEFAULT_WIDTH);
    int resolvedHeightSpec = this.resolveMeasureSpec(heightMeasureSpec, DP_DEFAULT_HEIGHT);
    super.onMeasure(resolvedWidthSpec, resolvedHeightSpec);
  }

  private int resolveMeasureSpec(int measureSpec, int dpDefault) {
    int mode = MeasureSpec.getMode(measureSpec);
    if (mode == MeasureSpec.EXACTLY) {
      return measureSpec;
    } else {
      int defaultSize = Utils.convertToPx(dpDefault, this.getResources());
      if (mode == MeasureSpec.AT_MOST) {
        defaultSize = Math.min(defaultSize, MeasureSpec.getSize(measureSpec));
      }
      return MeasureSpec.makeMeasureSpec(defaultSize, MeasureSpec.EXACTLY);
    }
  }

  //Override
  protected void onDraw(Canvas canvas) {
    this.glider.onDraw(canvas);
  }

  //override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    ss.angle = this.angle;
    return ss;
  }

  // override
  public void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    this.angle = ss.angle;
    this.invalidate();
  }

  int getMarksCount() {
    return this.glider.getMarksCount();
  }

  public static class Listener {
    public Listener() {
    }

    public void onRotationChanged(double radians) {
    }

    public void onScrollStateChanged(int state) {
    }
  }

}
