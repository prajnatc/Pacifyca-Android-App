package com.thinkpace.pacifyca.app;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

public class ToolbarBinder {
    private Activity context;
    private Toolbar toolbar;

    public void bindToActivity(Activity inContext, Toolbar inToolbar) {
        context = (Activity) inContext;
        toolbar = inToolbar;
    }
}
