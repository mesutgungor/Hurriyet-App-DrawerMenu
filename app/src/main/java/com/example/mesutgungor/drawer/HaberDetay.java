package com.example.mesutgungor.drawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HaberDetay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detay);
        WebView webView = (WebView)findViewById(R.id.webview);
        String haberurl = getIntent().getStringExtra("HABERURL");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(haberurl);


    }
}
