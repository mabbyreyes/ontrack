package edu.com.deepdive.ontrack.controller;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Arrays;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class Glider {

    private static final int DP_CURSOR_CORNERS_RADIUS = 1;
    private static final int DP_NORMAL_MARK_WIDTH = 1;
    private static final int DP_ZERO_MARK_WIDTH = 2;
    private static final int DP_CURSOR_WIDTH = 3;
    private static final float NORMAL_MARK_RELATIVE_HEIGHT = 0.6f;
    private static final float ZERO_MARK_RELATIVE_HEIGHT = 0.8f;
    private static final float CURSOR_RELATIVE_HEIGHT = 1f;
    private static final float SHADE_RANGE = 0.7f;
    private static final float SCALE_RANGE = 0.1f;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private HorizontalWheelView view;
    private int marksCount;
    private int normalColor;
    private int activeColor;
    private boolean showActiveRange;
    private float[] gaps;
    private float[] shades;
    private float[] scales;
    private int[] colorSwitches = new int[]{-1, -1, -1};
    private int viewportHeight;
    private int normalMarkWidth;
    private int normalMarkHeight;
    private int zeroMarkWidth;
    private int zeroMarkHeight;
    private int cursorCornersRadius;
    private RectF cursorRect = new RectF();
    private int maxVisibleMarksCount;

    Glider(HorizontalWheelView view) {
      this.view = view;
      this.initDpSizes();
    }

    private void initDpSizes() {
      this.normalMarkWidth = this.convertToPx(DP_NORMAL_MARK_WIDTH);
      this.zeroMarkWidth = this.convertToPx(DP_ZERO_MARK_WIDTH);
      this.cursorCornersRadius = this.convertToPx(DP_CURSOR_CORNERS_RADIUS);
    }

    private int convertToPx(int dp) {
      return Utils.convertToPx(dp, this.view.getResources());
    }

    void setMarksCount(int marksCount) {
      this.marksCount = marksCount;
     this. maxVisibleMarksCount = (marksCount / 2) + 1;
      this.gaps = new float[this.maxVisibleMarksCount];
      this.shades = new float[this.maxVisibleMarksCount];
      this.scales = new float[this.maxVisibleMarksCount];
    }

    void setNormalColor(int color) {
      this.normalColor = color;
    }

    void setActiveColor(int color) {
      this.activeColor = color;
    }

    void setShowActiveRange(boolean show) {
      this.showActiveRange = show;
    }

    void onSizeChanged() {
      this.viewportHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
      this.normalMarkHeight = (int) ((float)this.viewportHeight * NORMAL_MARK_RELATIVE_HEIGHT);
      this.zeroMarkHeight = (int) ((float)this.viewportHeight * ZERO_MARK_RELATIVE_HEIGHT);
      this.setupCursorRect();
    }

    private void setupCursorRect() {
      int cursorHeight = (int) ((float)this.viewportHeight * CURSOR_RELATIVE_HEIGHT);
      this.cursorRect.top = (float)(this.view.getPaddingTop() + (this.viewportHeight - cursorHeight) / 2);
      this.cursorRect.bottom = this.cursorRect.top + (float)cursorHeight;
      int cursorWidth = this.convertToPx(DP_CURSOR_WIDTH);
      this.cursorRect.left = (float)((this.view.getWidth() - cursorWidth) / 2);
      this.cursorRect.right = this.cursorRect.left + (float)cursorWidth;
    }

    int getMarksCount() {
      return this.marksCount;
    }

    void onDraw(Canvas canvas) {
      double step = 2 * PI / (double)this.marksCount;
      double offset = (PI / 2 - this.view.getRadiansAngle()) % step;
      if (offset < 0) {
        offset += step;
      }
      this.setupGaps(step, offset);
      this.setupShadesAndScales(step, offset);
      int zeroIndex = this.calcZeroIndex(step);
      this.setupColorSwitches(step, offset, zeroIndex);
      this.drawMarks(canvas, zeroIndex);
      this.drawCursor(canvas);
    }

    private void setupGaps(double step, double offset) {
      this.gaps[0] = (float) Math.sin(offset / 2);
      float sum = this.gaps[0];
      double angle = offset;
      // come fix
      int n = 1;
      while (angle + step <= PI) {
        gaps[n] = (float) sin(angle + step / 2);
        sum += gaps[n];
        angle += step;
        n++;
      }

      float lastGap = (float) Math.sin((PI + angle) / 2);
      sum += lastGap;
      if (n != this.gaps.length) {
        this.gaps[this.gaps.length - 1] = -1;
      }

      float k = (float)this.view.getWidth() / sum;

      for (int i = 0; i < this.gaps.length; i++) {
        if (this.gaps[i] != -1) {
          float[] var10000 = this.gaps;
          var10000[i] *= k;
        }
      }
    }

    private void setupShadesAndScales(double step, double offset) {
      double angle = offset;

      for (int i = 0; i < this.maxVisibleMarksCount; i++) {
        double sin = Math.sin(angle);
        this.shades[i] = (float) (1 - SHADE_RANGE * (1 - sin));
        this.scales[i] = (float) (1 - SCALE_RANGE * (1 - sin));
        angle += step;
      }
    }

    private int calcZeroIndex(double step) {
      double twoPi = 2 * PI;
      double normalizedAngle = (this.view.getRadiansAngle() + PI / 2 + twoPi) % twoPi;
      return normalizedAngle > PI ? -1 : (int)((PI - normalizedAngle) / step);
    }

    private void setupColorSwitches(double step, double offset, int zeroIndex) {
      if (!this.showActiveRange) {
        Arrays.fill(this.colorSwitches, -1);
        return;
      } else {
        double angle = this.view.getRadiansAngle();
        int afterMiddleIndex = 0;
        if (offset < PI / 2) {
          afterMiddleIndex = (int) ((PI / 2 - offset) / step) + 1;
        }

        if (angle > 3 * PI / 2) {
          this.colorSwitches[0] = 0;
          this.colorSwitches[1] = afterMiddleIndex;
          this.colorSwitches[2] = zeroIndex;
        } else if (angle >= 0) {
          this.colorSwitches[0] = Math.max(0, zeroIndex);
          this.colorSwitches[1] = afterMiddleIndex;
          this.colorSwitches[2] = -1;
        } else if (angle < -3 * PI / 2) {
          this.colorSwitches[0] = 0;
          this.colorSwitches[1] = zeroIndex;
          this.colorSwitches[2] = afterMiddleIndex;
        } else if (angle < 0) {
          this.colorSwitches[0] = afterMiddleIndex;
          this.colorSwitches[1] = zeroIndex;
          this.colorSwitches[2] = -1;
        }
      }
    }

    private void drawMarks(Canvas canvas, int zeroIndex) {
      float x = (float)this.view.getPaddingLeft();
      int color = this.normalColor;
      int colorPointer = 0;

      for(int i = 0; i < this.gaps.length && this.gaps[i] != -1.0F; ++i) {
        for(x += this.gaps[i]; colorPointer < 3 && i == this.colorSwitches[colorPointer]; ++colorPointer) {
          color = color == this.normalColor ? this.activeColor : this.normalColor;
        }

        if (i != zeroIndex) {
          this.drawNormalMark(canvas, x, this.scales[i], this.shades[i], color);
        } else {
          this.drawZeroMark(canvas, x, this.scales[i], this.shades[i]);
        }
      }

    }

    private void drawNormalMark(Canvas canvas, float x, float scale, float shade, int color) {
      float height = (float)this.normalMarkHeight * scale;
      float top = (float)this.view.getPaddingTop() + ((float)this.viewportHeight - height) / 2;
      float bottom = top + height;
      this.paint.setStrokeWidth((float)this.normalMarkWidth);
      this.paint.setColor(this.applyShade(color, shade));
      canvas.drawLine(x, top, x, bottom, this.paint);
    }

    private int applyShade(int color, float shade) {
      int r = (int) ((float)Color.red(color) * shade);
      int g = (int) ((float)Color.green(color) * shade);
      int b = (int) ((float)Color.blue(color) * shade);
      return Color.rgb(r, g, b);
    }

    private void drawZeroMark(Canvas canvas, float x, float scale, float shade) {
      float height = (float)this.zeroMarkHeight * scale;
      float top = (float)this.view.getPaddingTop() + (viewportHeight - height) / 2;
      float bottom = top + height;
      this.paint.setStrokeWidth((float)this.zeroMarkWidth);
      this.paint.setColor(this.applyShade(this.activeColor, shade));
      canvas.drawLine(x, top, x, bottom, this.paint);
    }

    private void drawCursor(Canvas canvas) {
      this.paint.setStrokeWidth(0);
      this.paint.setColor(this.activeColor);
      canvas.drawRoundRect(this.cursorRect, (float)this.cursorCornersRadius, (float)this.cursorCornersRadius, this.paint);
    }

  }
