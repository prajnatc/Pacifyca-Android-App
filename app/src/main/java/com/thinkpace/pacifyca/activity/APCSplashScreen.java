package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GK on 10/30/2016.
 */
public class APCSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.float_animation);
        launchHomeScreen();
    }

    private void launchHomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CPCAppCommonUtility.isUserSignedIn(APCSplashScreen.this)) {
                    redirectToHomeScreen();
                } else {
                    redirectToLoginScreen();
                }
            }
        }, 3000);
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(APCSplashScreen.this, APCLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToHomeScreen() {
        Intent intent = new Intent(APCSplashScreen.this, APCDashboard.class);
        startActivity(intent);
        finish();
    }

}
