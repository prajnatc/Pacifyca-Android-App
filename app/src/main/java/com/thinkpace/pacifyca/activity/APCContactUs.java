package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class APCContactUs extends APCBaseActivity {
    @BindView(R.id.webviewContact)
    WebView webContact;

    private static final String PAGE_TITLE_STRING = "PAGE_TITLE";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus_activity);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent titleIntent = getIntent();
        if (titleIntent.hasExtra(PAGE_TITLE_STRING)) {
            String title = titleIntent.getStringExtra(PAGE_TITLE_STRING);
            ((TextView) mToolbar.findViewById(R.id.title)).setText(title);
        }
        String contactUsInfo = getString(R.string.contact_us_info);
        webContact.loadData(contactUsInfo, "text/html", "utf-8");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
