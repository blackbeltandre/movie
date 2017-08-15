package com.tulisandigital.movie.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.tulisandigital.movie.R;
/**
 * Created by PDAK-Dev on 8/1/2017.
 * developerpdak@gmail.com
 * Student on udacity & IAK (base jakarta)
 * website : http://tulisandigital.com
 */

public class SplashScreen extends AppCompatActivity {
    private ProgressDialog progressBar;
    private ImageView imgSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_splash_screen);
                     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                      final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
                  progressBar = ProgressDialog.show(SplashScreen.this, "Connecting to server", "Please Wait...");
                /*https://stackoverflow.com/questions/22024779/android-fullscreen-activity-programmatically
                https://developer.android.com/samples/ImmersiveMode/src/com.example.android.immersivemode/ImmersiveModeFragment.html*/
                //BUAT SPLASHCREEN DENGAN FULL RESOLUSI
                /*requestWindowFeature(Window.FEATURE_NO_TITLE);*/
                 imgSplash = (ImageView) findViewById(R.id.imgSplash);
                     AnimationSet animation = new AnimationSet(true);
                         animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
                             animation.addAnimation(new ScaleAnimation(0.0f, 1, 0.0f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)); // Change args as desired
                         animation.setDuration(5500);
                     imgSplash.startAnimation(animation);
              new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent i = new Intent(SplashScreen.this, MovieActivity.class);
                startActivity(i);
                progressBar.hide();
                finish();
            }
        }
      .start();
    }
}