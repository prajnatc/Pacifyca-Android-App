package com.thinkpace.pacifyca.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.activity.APCBaseActivity;
import com.thinkpace.pacifyca.fragment.DrawerFragment;


public class CTVNavigationDrawer {
    private DrawerLayout drawerLayout;
    private NavDrawerBinder navDrawerBinder;
    private Toolbar toolbar;
    private int containerResID;
    private int selectedItem;
    private DrawerFragment drawerLayoutFragment;

    public static class Builder {
        private DrawerLayout drawerLayout;
        private Toolbar toolbar;
        private int containerResID;
        private int selectedItem;


        public Builder setDrawerLayout(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
            return this;
        }

        public Builder setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            return this;
        }

        public Builder setContainerResID(int containerResID) {
            this.containerResID = containerResID;
            return this;
        }

        public Builder setToolbar(Toolbar toolbar) {
            this.toolbar = toolbar;
            return this;
        }

        public CTVNavigationDrawer build() {
            return new CTVNavigationDrawer(this);
        }
    }

    private CTVNavigationDrawer(Builder builder) {
        this.drawerLayout = builder.drawerLayout;
        this.toolbar = builder.toolbar;
        this.containerResID = builder.containerResID;
        this.selectedItem = builder.containerResID;
    }

    public void initializeNavigationDrawer(APCBaseActivity activity) {
        navDrawerBinder = new NavDrawerBinder();
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        initializeDrawerToggle(activity);
        addDrawerLayout(activity, containerResID, selectedItem);
    }

    private void initializeDrawerToggle(Activity activity) {
        if (drawerLayout != null && toolbar != null) {
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
    }

    private void addDrawerLayout(APCBaseActivity activity, int containerResID, int selectedItem) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        drawerLayoutFragment = new DrawerFragment();
        Bundle bundle = new Bundle();
        drawerLayoutFragment.setArguments(bundle);
        fragmentTransaction.add(containerResID, drawerLayoutFragment, "DRAWER_LAYOUT");
        fragmentTransaction.commit();
    }

    public void closeDrawers() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }

    public void setChildName() {
        if (drawerLayoutFragment != null && !drawerLayoutFragment.isDetached()) {
            drawerLayoutFragment.setChildName();
        }
    }

    public void setLeftDrawerVisibility(boolean leftDrawerVisibility) {
        if (drawerLayout != null) {
            if (leftDrawerVisibility) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }
}
