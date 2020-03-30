package edu.com.deepdive.ontrack.controller.ui.tools;

public abstract class AbstractTabAnimation {

  protected final static int LOWER_POSITION = 0;
  protected final static int MIDDLE_POSITION = 1;
  protected final static int UPPER_POSITION = 2;

  protected final DigitFlip.Tab mTopTab;
  protected final DigitFlip.Tab mBottomTab;
  protected final DigitFlip.Tab mMiddleTab;

  protected int state;
  protected int mAlpha = 0;
  protected long mTime = -1;
  protected float mElapsedTime = 1000.0f;

  public AbstractTabAnimation(DigitFlip.Tab mTopTab, DigitFlip.Tab mBottomTab, DigitFlip.Tab mMiddleTab) {
    this.mTopTab = mTopTab;
    this.mBottomTab = mBottomTab;
    this.mMiddleTab = mMiddleTab;
    initState();
  }

  public void start() {
    makeSureCycleIsClosed();
    mTime = System.currentTimeMillis();
  }

  public void sync() {
    makeSureCycleIsClosed();
  }

  public abstract void initState();
  public abstract void initMiddleTab();
  public abstract void run();
  protected abstract void makeSureCycleIsClosed();

}
