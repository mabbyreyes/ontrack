package edu.com.deepdive.ontrack.controller.ui.timeline;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.com.deepdive.ontrack.R;

public class TimelineFragment extends Fragment {

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_timeline, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    TimelineViewModel timelineViewModel = ViewModelProviders.of(this).get(TimelineViewModel.class);
    // TODO: Use the ViewModel
  }

}
