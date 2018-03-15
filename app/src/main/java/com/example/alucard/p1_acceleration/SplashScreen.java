package com.example.alucard.p1_acceleration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

public class SplashScreen extends Activity{

    protected static final int splashTime = 5000;
    private Thread splashTread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spashscreen);

        splashTread = new Thread() {
            @Override
            public void run() {
            try {
                synchronized(this){
                    wait(splashTime);
                }

            } catch(InterruptedException e) {
            }
            finally {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
            }
        };

        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized(splashTread){
                splashTread.notifyAll();
            }
        }
        return true;
    }


}
