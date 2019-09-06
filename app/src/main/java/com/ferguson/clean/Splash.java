package com.ferguson.clean;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Splash extends AppCompatActivity {

//    private RelativeLayout relativeLayout;
//    private AnimationDrawable animationDrawable;
    private static int SPLASH_TIME_OUT = 3000;
    private TextView txtAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        txtAppName = findViewById(R.id.txtAppName);

        runSplash();

        // init relativeLayout
//        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        // initializing animation drawable by getting background from constraint layout
//        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        // setting enter fade animation duration to 5 seconds
//        animationDrawable.setEnterFadeDuration(500);

        // setting exit fade animation duration to 2 seconds
//        animationDrawable.setExitFadeDuration(500);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                Intent i = new Intent(Splash.this, Dashboard.class);
//                Bundle b = null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    b = ActivityOptions.makeScaleUpAnimation(txtAppName, 0, 0, txtAppName.getWidth(),
//                            txtAppName.getHeight()).toBundle();
//                }
//                startActivity(i, b);
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }

    public void runSplash(){
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME_OUT);

                    Intent i = new Intent(Splash.this, Dashboard.class);
                    Bundle b = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        b = ActivityOptions.makeScaleUpAnimation(txtAppName, 0, 0, txtAppName.getWidth(),
                                txtAppName.getHeight()).toBundle();
                    }
                    startActivity(i, b);
                    // close this activity
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
        mThread.start();
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }*/
/*
    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }*/
}
