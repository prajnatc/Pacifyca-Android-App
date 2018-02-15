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


public class APCWebViewActivity extends APCBaseActivity {
    private WebView webView;
    private ProgressBar mProgressBar;
    private LinearLayout mNoConnectionLayout;
    private static final String WEB_URL_STRING = "WEB_URL";
    private String WEB_URL;
    private static final String PAGE_TITLE_STRING = "PAGE_TITLE";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);


        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mNoConnectionLayout = (LinearLayout) findViewById(R.id.no_connection_layout);
        mProgressBar.setVisibility(View.GONE);
        Intent urlIntent = getIntent();

        if (urlIntent.hasExtra(WEB_URL_STRING)) {
            WEB_URL = urlIntent.getStringExtra(WEB_URL_STRING);
            if (CPCAppCommonUtility.isNetworkAvailable(this)) {
                if (!WEB_URL.isEmpty())
                    loadWebView();
            } else {
                mProgressBar.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.layout_root_web_view), CPCAppConstants.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
                mNoConnectionLayout.setVisibility(View.VISIBLE);
            }
        }
        if (urlIntent.hasExtra(PAGE_TITLE_STRING)) {
            String title = urlIntent.getStringExtra(PAGE_TITLE_STRING);
            ((TextView) mToolbar.findViewById(R.id.title)).setText(title);
//            getSupportActionBar().setTitle(title);
        }
    }

    private void loadWebView() {

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(WEB_URL);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            APCWebViewActivity.this.mProgressBar.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            APCWebViewActivity.this.mProgressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
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
