package com.thinkpace.pacifyca.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.thinkpace.pacifyca.app.ToolbarBinder;

public class APCBaseActivity extends AppCompatActivity {
    protected ToolbarBinder mToolbarBinder;
    private Toolbar mToolbar;

    protected void initialiseToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        mToolbarBinder = new ToolbarBinder();
        mToolbarBinder.bindToActivity(this, toolbar);
        setSupportActionBar(mToolbar);
    }
}