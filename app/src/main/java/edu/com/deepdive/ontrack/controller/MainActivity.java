package edu.com.deepdive.ontrack.controller;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.com.deepdive.ontrack.MainViewModel;
import edu.com.deepdive.ontrack.R;
import edu.com.deepdive.ontrack.controller.ui.home.HomeFragment;
import edu.com.deepdive.ontrack.service.GoogleSignInRepository;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener,
    PermissionsFragment.OnAcknowledgeListener {

  private static final int EXTERNAL_STORAGE_REQUEST_CODE = 1000;

  private MainViewModel viewModel;
  private AppBarConfiguration mAppBarConfiguration;
  private WheelActivity wheelActivity;
  protected DrawerLayout drawerLayout;
  private ActionBarDrawerToggle drawerToggle;
  private List<SlideMenuItem> list = new ArrayList<>();
  private List<View> viewList = new ArrayList<>();
  private ViewAnimator viewAnimator;
  private LinearLayout linearLayout;
  private View view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    HomeFragment homeFragment = HomeFragment.newInstance();
    getSupportFragmentManager().beginTransaction().replace(
        R.id.content_frame, homeFragment).commit();
    drawerLayout = findViewById(R.id.drawer_layout);
    drawerLayout.setScrimColor(Color.TRANSPARENT);
    linearLayout = findViewById(R.id.left_drawer);
    linearLayout.setOnClickListener(v -> drawerLayout.closeDrawers());
    setActionBar();
    setupViewModel();
    createMenuList();
    checkPermissions();
    viewAnimator = new ViewAnimator<>(this, list, homeFragment, drawerLayout, this);
  }

  private void createMenuList() {
    SlideMenuItem menuItem0 = new SlideMenuItem(HomeFragment.CLOSE, R.drawable.ic_close);
    list.add(menuItem0);
    SlideMenuItem menuItem1 = new SlideMenuItem(HomeFragment.HOME, R.drawable.ic_home);
    list.add(menuItem1);
    SlideMenuItem menuItem2 = new SlideMenuItem(HomeFragment.JIGSAW, R.drawable.ic_jigsaw);
    list.add(menuItem2);
    SlideMenuItem menuItem3 = new SlideMenuItem(HomeFragment.TIMELINE, R.drawable.ic_timeline);
    list.add(menuItem3);
    SlideMenuItem menuItem4 = new SlideMenuItem(HomeFragment.SETTINGS, R.drawable.ic_settings);
    list.add(menuItem4);
  }

  private void setActionBar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    drawerToggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
    ) {
      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        linearLayout.removeAllViews();
        linearLayout.invalidate();
      }

      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (slideOffset > 0.6 && linearLayout.getChildCount() == 0) {
          viewAnimator.showMenuContent();
        }
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
      }
    };
    drawerLayout.addDrawerListener(drawerToggle);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//    if (drawerToggle.onOptionsItemSelected(item)) {
//      return true;
//    }
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.sign_out:
        GoogleSignInRepository.getInstance().signOut()
            .addOnCompleteListener((task) -> {
              Intent intent = new Intent(this, LoginActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
            });
        break;
      case R.id.action_settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE) {
      for (int i = 0; i < permissions.length; i++) {
        String permission = permissions[i];
        int result = grantResults[i];
        if (result == PackageManager.PERMISSION_GRANTED) {
          viewModel.grantPermission(permission);
        } else {
          viewModel.revokePermission(permission);
        }
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  @Override
  public void onAcknowledge(String[] permissionsToRequest) {
    ActivityCompat.requestPermissions(this, permissionsToRequest, EXTERNAL_STORAGE_REQUEST_CODE);
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
  }

  private void checkPermissions() {
    String[] permissions = null;
    try {
      PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
          PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
      permissions = info.requestedPermissions;
    } catch (NameNotFoundException e) {
      throw new RuntimeException(e);
    }
    List<String> permissionsToRequest = new LinkedList<>();
    List<String> permissionsToExplain = new LinkedList<>();
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission)
          != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(permission);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
          permissionsToExplain.add(permission);
        }
      } else {
        viewModel.grantPermission(permission);
      }
    }
    if (!permissionsToExplain.isEmpty()) {
      explainPermissions(
          permissionsToExplain.toArray(new String[0]), permissionsToRequest.toArray(new String[0]));
    } else if (!permissionsToRequest.isEmpty()) {
      onAcknowledge(permissionsToRequest.toArray(new String[0]));
    }
  }

  private void explainPermissions(String[] permissionsToExplain, String[] permissionsToRequest) {
    PermissionsFragment fragment =
        PermissionsFragment.createInstance(permissionsToExplain, permissionsToRequest);
    fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
  }

  @Override
  public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable,
      int position) {
    switch (slideMenuItem.getName()) {
      case HomeFragment.CLOSE:
        return screenShotable;
      case HomeFragment.HOME:
        startActivity(new Intent(this, WheelActivity.class));
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment1).commit();
//        return replaceFragment(fragment1, position);
      case HomeFragment.JIGSAW:
        CountdownFragment fragment2 = CountdownFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2).commit();
        return replaceFragment(fragment2, position);
//      case TIMELINE:
//        startActivity(new Intent(this, WheelActivity.class));
      default:
        HomeFragment fragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        return replaceFragment(fragment, position);
    }
  }

  private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {
    view = findViewById(R.id.content_frame);
    int finalRadius = Math.max(view.getWidth(), view.getHeight());
    Animator animator = ViewAnimationUtils.createCircularReveal(
        view, 0, topPosition, 0, finalRadius);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
    findViewById(R.id.content_overlay).setBackground(
        new BitmapDrawable(getResources(), screenShotable.getBitmap()));
    animator.start();
    return screenShotable;
  }


  @Override
  public void addViewToContainer(View view) {
    linearLayout.addView(view);
  }

  @Override
  public void disableHomeButton() {
    getSupportActionBar().setHomeButtonEnabled(false);
  }

  @Override
  public void enableHomeButton() {
    getSupportActionBar().setHomeButtonEnabled(true);
    drawerLayout.closeDrawers();
  }

}
