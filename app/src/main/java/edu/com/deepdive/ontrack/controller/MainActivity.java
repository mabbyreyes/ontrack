package edu.com.deepdive.ontrack.controller;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import edu.com.deepdive.ontrack.R;
import edu.com.deepdive.ontrack.controller.ui.home.HomeFragment;
import java.util.ArrayList;
import java.util.List;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;
import yalantis.com.sidemenu.util.ViewAnimator.ViewAnimatorListener;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {

  private AppBarConfiguration mAppBarConfiguration;
  private WheelActivity wheelActivity;
  private HomeFragment homeFragment;
  protected DrawerLayout drawerLayout;
  private ActionBarDrawerToggle drawerToggle;
  private List<SlideMenuItem> list = new ArrayList<>();
  private ViewAnimator viewAnimator;
  private LinearLayout linearLayout;
  private int res = R.drawable.puzzle_piece;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    HomeFragment homeFragment = HomeFragment.newInstance(R.drawable.puzzle_piece);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.content_frame, homeFragment)
        .commit();
    drawerLayout = findViewById(R.id.drawer_layout);
    drawerLayout.setScrimColor(Color.TRANSPARENT);
    linearLayout = findViewById(R.id.left_drawer);
    linearLayout.setOnClickListener(v -> drawerLayout.closeDrawers());
    setActionBar();
    createMenuList();
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
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (drawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    switch (item.getItemId()) {
      case R.id.action_settings:
      return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {
    this.res = this.res == R.drawable.puzzle_piece ? R.drawable.ic_home : R.drawable.puzzle_piece;
    View view = findViewById(R.id.content_frame);
    int finalRadius = Math.max(view.getWidth(), view.getHeight());
    Animator animator = ViewAnimationUtils.createCircularReveal(
        view, 0, topPosition, 0, finalRadius);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
    findViewById(R.id.content_overlay).setBackground(
        new BitmapDrawable(getResources(), screenShotable.getBitmap()));
    animator.start();
    HomeFragment homeFragment = HomeFragment.newInstance(this.res);
    getSupportFragmentManager().beginTransaction().replace(
        R.id.content_frame, homeFragment).commit();
    return homeFragment;
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

  @Override
  public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable,
      int position) {
    switch (slideMenuItem.getName()) {
      case HomeFragment.CLOSE:
        return screenShotable;
      case HomeFragment.HOME:
        Intent intent = new Intent(MainActivity.this, WheelActivity.class);
        startActivity(intent);
      default:
        return replaceFragment(screenShotable,position);
    }
  }

}
