package edu.com.deepdive.ontrack.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.google.gson.Gson;
import edu.com.deepdive.ontrack.R;
import edu.com.deepdive.ontrack.controller.ui.tools.DigitFlip;
import edu.com.deepdive.ontrack.model.entity.Task;
import edu.com.deepdive.ontrack.service.OntrackDatabase;
import java.util.Locale;
import yalantis.com.sidemenu.util.ViewAnimator;

public class WheelActivity extends MainActivity implements ViewAnimator.ViewAnimatorListener {

  private HorizontalWheelView horizontalWheelView;
  private DigitFlip tabDigit1;
  private TextView tvSCTime;
  private TextView tvMusicMessage;
  private CountDownTimer mainTimer;
  private Button btnGiveUp;
  private MediaPlayer mediaPlayer;
  private String skillAsString;
  private boolean music = false;
  private ImageButton ibtnMusic;
  private AlertDialog musicListDialog;
  private ListView listView;
  private Task task;
  private int selectedMusic = 0;
  private int[] musicList;
  private String[] musicNameList;
  private boolean unExpectedPause = true;
  private OntrackDatabase db;
  private NotificationCompat.Builder notification;
  private static final int UNIQUE = 12121212;
  private long selectedTime, mainTimerCount = 0,  MIN_TIME = 5*60*1000;
  private ImageView ivRocket;
  private FrameLayout frameLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View contentView = inflater.inflate(R.layout.fragment_puzzle, null,false);
    drawerLayout.addView(contentView, 1);
    initViews();
    setupListeners();
    updateUi();
    Gson gson = new Gson();
    skillAsString = getIntent().getStringExtra("skill");
    task = gson.fromJson(skillAsString, Task.class);
    selectedTime = getIntent().getLongExtra("selectedTime", MIN_TIME);
    tvMusicMessage.setVisibility(View.INVISIBLE);
    listView = new ListView(this);
//    formatTimeAndShow(MIN_TIME);
    musicNameList = new String[]{"Forest", "Cricket Chirping", "Ocean Waves"};
    musicList = new int[]{R.raw.rainforest, R.raw.cricket, R.raw.ocean_waves};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_list_item_1, musicNameList);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      selectedMusic = i;
      setMusic(selectedMusic);
    });
    ibtnMusic.setOnLongClickListener(view -> {
      showMusicListDialog();
      return false;
    });

    ibtnMusic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(music){
          mediaPlayer.stop();
          ibtnMusic.setImageResource(R.drawable.sound_off);
          music = false;
        }
        else{
          setMusic(selectedMusic);
          ibtnMusic.setImageResource(R.drawable.sound_on);
          music = true;
          tvMusicMessage.setText("[Playing '"+ musicNameList[selectedMusic] +"'] Longpress for others");
          tvMusicMessage.setVisibility(View.VISIBLE);
          new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
              tvMusicMessage.setVisibility(View.INVISIBLE);
              this.cancel();
            }
          }.start();
        }
      }
    });
    btnGiveUp.setOnClickListener(view -> showCancelTimerDialog());
    startMainTimer();
    notification = new NotificationCompat.Builder(this);
    notification.setAutoCancel(true);
  }

  @OnLifecycleEvent(Event.ON_STOP)

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    updateUi();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  private void initViews() {
    horizontalWheelView = findViewById(R.id.horizontalWheelView);
    tabDigit1 = findViewById(R.id.tabDigit1);
    tvSCTime = findViewById(R.id.tvSCTime);
    ivRocket = findViewById(R.id.ivRocket);
//    Button btnFocus = findViewById(R.id.focus_button);
    btnGiveUp = findViewById(R.id.btn_GiveUp);
    ibtnMusic = findViewById(R.id.ibtnMusic);

    tvMusicMessage = findViewById(R.id.tvMusicMessage);
  }

  public void setMusic(int musicId){
    if(mediaPlayer != null) mediaPlayer.stop(); //stop if music playing
    mediaPlayer = MediaPlayer.create(getApplicationContext(), musicList[musicId]);
    mediaPlayer.setLooping(true);
    mediaPlayer.start();
    music = true;
    ibtnMusic.setImageResource(R.drawable.sound_on);
  }

  private void setupListeners() {
    horizontalWheelView.setListener(new HorizontalWheelView.Listener() {
      @Override
      public void onRotationChanged(double radians) {
        updateUi();
      }
    });
  }

  public static String convertAngleToTimeString(float angle) {
    String time = "";
    float decimalValue = 3.0f - (1.0f / 30.0f) * (angle % 360);
    if (decimalValue < 0)
      decimalValue += 12.0f;

    int hours = (int) decimalValue;
    if (hours == 0)
      hours = 12;
    time += (hours < 10 ? "0" + hours : hours) + ":";
    int minutes = (int) (decimalValue * 60) % 60;
    time += minutes < 10 ? "0" + minutes : minutes;
    return time;

  }

  private void getDegreesAngle() {
    horizontalWheelView.getDegreesAngle();

  }

    private void updateUi() {
        updateText();
        updateImage();
    }

  public void startMainTimer() {
    mainTimer = new CountDownTimer(selectedTime, 1000) {
      @Override
      public void onTick(long l) {
        mainTimerCount += 1000;
        int progress = 100 - (int) ((l * 100) / selectedTime);
        horizontalWheelView.setProgress(progress);
        updateText();
      }
       @Override
        public void onFinish() {
          //store skill task in database and set skilltask as true
          stopMainTimer();
        }
      }.start();
    }

    // TODO Add "TimeSpent" to dao, entity, repository, erd.
  public void stopMainTimer(){
    mainTimer.cancel();
    task.setTimeSpent(task.getTimeSpent() + mainTimerCount);
    db.getTaskDao().update(task);
//    Puzzle puzzle = new Puzzle(task.getId(), System.currentTimeMillis(), selectedTime, mainTimerCount);
//    db.getPuzzleDao().insert(puzzle);
  }

  private  void showCancelTimerDialog(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Do you want to give up?")
        .setPositiveButton("Yes", (dialogInterface, i) -> {
          stopMainTimer();
          if(music) mediaPlayer.stop();
          goBack();
        })
        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
  }

  private void showMusicListDialog(){
    AlertDialog.Builder builder = null;
    if(musicListDialog == null){
      builder = new AlertDialog.Builder(this);
      builder.setCancelable(true);
      builder.setMessage("Select the music you want to listen to");
      builder.setPositiveButton("Ok",null);
      builder.setView(listView);
      musicListDialog = builder.create();
      musicListDialog.show();
    }else{
      musicListDialog.show();
    }
  }

  private void updateImage() {
    float angle = (float) horizontalWheelView.getDegreesAngle();
    ivRocket.setRotation(angle);
  }

  private void updateText() {
    String text = String.format(Locale.US, "%02d:%02d:%02d", selectedTime / 3600,
        (selectedTime % 3600) / 60, (selectedTime % 60));;
    tvSCTime.setText(text);
  }

  private void goBack(){
    unExpectedPause = false;
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    intent.putExtra("skill", skillAsString);
    startActivity(intent);
    finish();
  }

  @Override
  public void onBackPressed() {
    showCancelTimerDialog();
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!unExpectedPause)
      return;
    if (music) {
      mediaPlayer.stop();
      ibtnMusic.setImageResource(R.drawable.sound_off);
    }
    notification.setPriority(Notification.DEFAULT_VIBRATE);
//    notification.setSmallIcon(R.drawable.forest);
    notification.setTicker("This is a ticker");
    notification.setWhen(System.currentTimeMillis());
    notification.setContentTitle("Return to your studying");
    notification.setContentText(
        "Your puzzle is going to crumble. Return quickly or you'll lose your hardwork!");
    notification.setVibrate(new long[]{0, 1000, 1000, 500, 1000, 1000});
//    Intent intent = new Intent(this, WheelActivity.class);
//    PendingIntent pendingIntent = PendingIntent
//        .getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    notification.setContentIntent(pendingIntent);
//    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    nm.notify(UNIQUE, notification.build());
  }



}
