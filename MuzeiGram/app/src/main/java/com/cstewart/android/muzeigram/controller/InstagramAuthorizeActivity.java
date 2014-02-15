package com.cstewart.android.muzeigram.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.Settings;

/**
 * Created by chris on 2/15/14.
 */
public class InstagramAuthorizeActivity extends Activity {

    private static final String AUTHORIZE_URL = "https://instagram.com/oauth/authorize/?client_id=f46b6a1920c34da59c87d99d9209d20a&redirect_uri=http://REDIRECT_URL&response_type=token";
    private static final String ACCESS_TOKEN_CONSTANT = "access_token=";


    private WebView mAuthorizeWebView;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramAuthorizeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_authorize);

        mAuthorizeWebView = (WebView) findViewById(R.id.activity_instagram_authorize_webview);
        mAuthorizeWebView.setWebViewClient(mWebViewClient);

        mAuthorizeWebView.loadUrl(AUTHORIZE_URL);
    }

    private void aquireToken(String url) {
        int index = url.indexOf(ACCESS_TOKEN_CONSTANT);
        index = index + ACCESS_TOKEN_CONSTANT.length();
        String accessToken = url.substring(index, url.length());

        new Settings(this).saveInstagramToken(accessToken);
        finish();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(ACCESS_TOKEN_CONSTANT)) {
                aquireToken(url);
                return true;
            }

            return false;
        }
    };
}
