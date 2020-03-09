package edu.com.deepdive.ontrack.controller.ui.puzzle;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.com.deepdive.ontrack.R;

public class PuzzleFragment extends Fragment {

  private PuzzleViewModel mViewModel;

  public static PuzzleFragment newInstance() {
    return new PuzzleFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_puzzle, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(PuzzleViewModel.class);
    // TODO: Use the ViewModel
  }

}
