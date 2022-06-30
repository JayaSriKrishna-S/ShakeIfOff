package com.example.music;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;

import java.util.Calendar;
import java.util.regex.*;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Sensor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.music.R;
import com.example.music.adapter.ItemClickListener;
import com.example.music.adapter.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private static MainActivity instance;
    RecyclerView rv;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private int sensorTime = 0;
    private RelativeLayout topPanel;
    private RelativeLayout sidebar;
    private boolean isPanelShown;
    private ImageView trigger;
    private LinearLayout songPanel;
    private View f, k;
    private boolean aa, bb = false;

    private ImageView pause;
    private boolean ISDAY = true;
    private int lengthsong = 0;
    private ImageView forward;
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayLists;
    private ListView secret;
    private ImageView back;
    private LottieAnimationView col;
    private LottieAnimationView dark;
    private ArrayAdapter<String> arrayAdapter;

    AudioManager audioManager;
    private MediaPlayer mediap;
    Calendar calendar = Calendar.getInstance();

    private int tracker = 0;
    private static final int MY_PERMISSION_REQUEST = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        sidebar = findViewById(R.id.sidebar);
        rv = findViewById(R.id.rv);
        songPanel = findViewById(R.id.songpanel);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }
        trigger = findViewById(R.id.trigger);
        dark = findViewById(R.id.dark);
        dark.setSpeed(+10);
        dark.playAnimation();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        forward = findViewById(R.id.forward);
        back = findViewById(R.id.back);
        View backcolor = findViewById(R.id.backcolor);
        View forwardcolor = findViewById(R.id.forwardcolor);
        seekBar = findViewById(R.id.seekbar);
        pause = findViewById(R.id.pause);
        col = findViewById(R.id.ani2);
        View head = findViewById(R.id.headphone);
        topPanel = findViewById(R.id.topPanel);

        secret = findViewById(R.id.secret);


        if (ISDAY == true) {
            pause.setImageResource(R.drawable.nplay);
        } else {
            pause.setImageResource(R.drawable.playc);
        }

        getmusic();

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter adap = new adapter(this, arrayLists);
        rv.setAdapter(adap);

        tracker = 0;


        tracker = 0;


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        secret.setAdapter(arrayAdapter);

        dos();


        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songPanel.animate().translationX(-1100).setDuration(500);
                seekBar.setEnabled(true);
                pause.setEnabled(true);
                forward.setEnabled(true);
                back.setEnabled(true);


            }
        });
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songPanel.animate().translationX(-180).setDuration(500);
                seekBar.setEnabled(false);
                pause.setEnabled(false);
                forward.setEnabled(false);
                back.setEnabled(false);
                sidebar.setVisibility(View.VISIBLE);
            }
        });
        dark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ISDAY) {
                    songPanel.setBackgroundResource(R.drawable.night_side);
                    dark.setSpeed(-1);
                    dark.playAnimation();

                    backcolor.animate().alpha(1.0f).setDuration(3400);
                    forwardcolor.animate().alpha(1.0f).setDuration(3400);
                    back.animate().alpha(0.0f).setDuration(1000);
                    forward.animate().alpha(0.0f).setDuration(1000);
                    if (pause.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.nplay).getConstantState()) {
                        pause.setImageResource(R.drawable.playc);

                    } else {
                        pause.setImageResource(R.drawable.pausec);
                    }
                    tonight();
                    topPanel.setBackgroundResource(R.drawable.dayb);
                    head.animate().alpha(0.5f).setDuration(1300);


                    ISDAY = false;
                } else {
                    ISDAY = true;
                    songPanel.setBackgroundResource(R.drawable.mo_side);
                    dark.setSpeed(+1);
                    dark.playAnimation();
                    head.animate().alpha(1f).setDuration(1300);
                    backcolor.animate().alpha(0.0f).setDuration(1000);
                    forwardcolor.animate().alpha(0.0f).setDuration(1000);
                    back.animate().alpha(1.0f).setDuration(1000);
                    forward.animate().alpha(1.0f).setDuration(1000);
                    if (pause.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.playc).getConstantState()) {
                        pause.setImageResource(R.drawable.nplay);

                    } else {
                        pause.setImageResource(R.drawable.newp);
                    }
                    today();
                    topPanel.setBackgroundResource(R.drawable.lightb);


                }
                return false;
            }
        });

        pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                if (!mediap.isPlaying()) {
                    col.playAnimation();
                    if (check == 0) {
                        Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
                    }
                    mediap.start();
                    if (ISDAY == true) {
                        pause.setImageResource(R.drawable.nplay);
                    } else {
                        pause.setImageResource(R.drawable.playc);
                    }
                } else {
                    col.pauseAnimation();
                    if (ISDAY == true) {
                        pause.setImageResource(R.drawable.newp);
                    } else {
                        pause.setImageResource(R.drawable.pausec);
                    }
                    mediap.pause();
                }

                return false;
            }
        });

        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mediap.stop();

                mediap.release();

                ++tracker;
                Log.i("TRACKK", String.valueOf(tracker));
                if (tracker == lengthsong) {
                    tracker = 0;
                }

                col.playAnimation();
                seekBar.setProgress(0);
                dos();

                int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (check == 0) {
                    Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
                }


                return false;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mediap.stop();
                SystemClock.sleep(200);

                mediap.reset();
                mediap.release();
                --tracker;
                if (tracker == -1) {
                    tracker = lengthsong - 1;
                }
                seekBar.setProgress(0);
                col.playAnimation();

                dos();
                int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (check == 0) {
                    Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
                }

                seekk();
                return false;
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediap.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (ISDAY == true) {
                    pause.setImageResource(R.drawable.newp);
                } else {
                    pause.setImageResource(R.drawable.pausec);
                }
                mediap.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ISDAY == true) {
                    pause.setImageResource(R.drawable.nplay);
                } else {
                    pause.setImageResource(R.drawable.playc);
                }
                mediap.start();
            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    seekBar.setProgress(mediap.getCurrentPosition());
                } catch (Exception e) {
                    Log.i("jsk", "fdd");
                }
            }

        }, 0, 100);


    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];h
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 7) {




                    Log.i("ddd","inside");

                mediap.stop();

                mediap.release();

                ++tracker;
                Log.i("TRACKK", String.valueOf(tracker));
                if (tracker == lengthsong) {
                    tracker = 0;
                }

                col.playAnimation();
                seekBar.setProgress(0);
                dos();

                int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (check == 0) {
                    Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
                }
            }
            }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }






        public void dos() {


            String completePath;
            try {
                Toast.makeText(MainActivity.this,"ngkgjgljgjCHECK1",Toast.LENGTH_SHORT);
                completePath = Environment.getExternalStorageDirectory().getPath() + arrayList.get(tracker);

                File file = new File(completePath);
                mediap = MediaPlayer.create(this, Uri.parse(completePath));

                seekk();
                if(ISDAY==true) {
                    pause.setImageResource(R.drawable.nplay);
                }
                else {
                    pause.setImageResource(R.drawable.playc);
                }
                ;
                mediap.start();





                mediap.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(ISDAY==true) {
                            pause.setImageResource(R.drawable.nplay);
                        }
                        else {
                            pause.setImageResource(R.drawable.playc);
                        }
                        ++tracker;
                        if(tracker==lengthsong) {
                            tracker = 0;
                        }
                        dos();
                        int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        if (check == 0) {
                            Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
                        }
                        seekk();
                    }
                });


            } catch (Exception e) {
                Toast.makeText(this, "Made By JSK", Toast.LENGTH_SHORT).show();
            }
        }


        public void seekk() {

            seekBar.setMax(mediap.getDuration());


            }

        public void getmusic() {

            arrayList = new ArrayList<String>();
            arrayLists=new ArrayList<String>();
            ContentResolver contentResolver = getContentResolver();
            Uri songUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = contentResolver.query(songUrl, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int SongTitle = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int path = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

                do {
                    String songname=cursor.getString(SongTitle);
                    String paths = cursor.getString(path);
                    String pathsp[] = paths.split("/storage/emulated/0");
                   if(!(pathsp[1].contains("WhatsApp"))) {
                       String pathe[] = paths.split("/");
                       int length = pathe.length;
                       String newVar=pathe[length-1];

                       arrayList.add(pathsp[1].toString());

                       Pattern pattern = Pattern.compile("\\.");
                       int len=newVar.length();
                       Matcher matcher = pattern.matcher(newVar);
                       int max = 0;
                       while (matcher.find()) {

                           if(matcher.start()>max){

                               max=matcher.start();
                           }


                       }
                       String sub = newVar.substring(0,max);




                       arrayLists.add(sub);


                       lengthsong = arrayList.size();
                   }

                } while (cursor.moveToNext());

            }

        }

        public void tonight(){
            ColorDrawable[] colorDrawables={new ColorDrawable(Color.BLACK),new ColorDrawable(Color.WHITE)};
            TransitionDrawable transitionDrawable=new TransitionDrawable(colorDrawables);
            topPanel.setBackground(transitionDrawable);
            transitionDrawable.startTransition(1000);


        }
        public void today(){
            ColorDrawable[] colorDrawables={new ColorDrawable(Color.BLACK),new ColorDrawable(Color.WHITE)};
            TransitionDrawable transitionDrawable=new TransitionDrawable(colorDrawables);
            topPanel.setBackground(transitionDrawable);
            transitionDrawable.startTransition(1000);
        }
    public static MainActivity getInstance() {
        return instance;
    }
    public void takeit(int S){

        mediap.stop();

        mediap.release();
        tracker=S;
        col.playAnimation();
        seekBar.setProgress(0);
        dos();


        int check = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (check == 0) {
            Toast.makeText(MainActivity.this, "Please raise up the volume", Toast.LENGTH_LONG).show();
        }







    }



    }

