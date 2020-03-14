package edu.com.deepdive.ontrack.controller;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View.BaseSavedState;

class SavedState extends BaseSavedState {

  double angle;

  SavedState(Parcelable superState) {
    super(superState);
  }

  // done.
  private SavedState(Parcel in) {
    super(in);
    this.angle = (Double) in.readValue((ClassLoader) null);
  }

  // done.. override
  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeValue(this.angle);
  }

  // done.. override
  public String toString() {
    return "HorizontalWheelView.SavedState{"
        + Integer.toHexString(System.identityHashCode(this))
        + " angle=" + this.angle + "}";
  }

  public static final Creator<SavedState> CREATOR
      = new Creator<SavedState>() {
    public SavedState createFromParcel(Parcel in) {
      return new SavedState(in);
    }

    public SavedState[] newArray(int size) {
      return new SavedState[size];
    }
  };

}
