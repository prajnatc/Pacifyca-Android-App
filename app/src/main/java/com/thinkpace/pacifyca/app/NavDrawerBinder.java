package com.thinkpace.pacifyca.app;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.thinkpace.pacifyca.R;

public class NavDrawerBinder {
    private Context context;

    public void bindToActivity(Context inContext, final DrawerLayout inDrawerLyt, Toolbar inToolbar) {
        context = inContext;
        if (inToolbar != null) {
            inToolbar.setNavigationIcon(R.drawable.nav_icon);
            inToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inDrawerLyt.openDrawer(GravityCompat.START);
                }
            });
        }
    }
}
