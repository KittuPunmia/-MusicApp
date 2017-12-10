package com.kittu.mediaplayer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class sp_activity extends AppCompatActivity {
ImageView im;
    TextView tv;
    Animation animation;
   private static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_activity);
        im=(ImageView)findViewById(R.id.im);
        tv=(TextView)findViewById(R.id.tv);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.a1);
        im.startAnimation(animation);
        tv.startAnimation(animation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(count==0) {
                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(i);
                    finish();
                    count++;
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();


                }
            }
        }).start();


    }
}
