package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class Frag extends AppCompatActivity {
private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
     lottieAnimationView=findViewById(R.id.ani);
     lottieAnimationView.animate().alpha(0).setDuration(4000);

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             Intent i=new Intent(Frag.this, MainActivity.class);
             startActivity(i);
             finish();

         }
     },3800);

    }
}