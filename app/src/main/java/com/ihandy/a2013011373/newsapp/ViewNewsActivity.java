package com.ihandy.a2013011373.newsapp;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewNewsActivity extends AppCompatActivity {
    private News news;
    private Drawable modifiedFavoriteIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable icon = getResources().getDrawable(R.drawable.ic_favorite_white_24dp);
        ColorStateList colorSelector = getResources().getColorStateList(R.color.colorFavorite);
        DrawableCompat.setTintList(icon, colorSelector);
        modifiedFavoriteIcon = icon;

        Bundle bundle = getIntent().getExtras();
        news = News.getByNewsId(bundle.getLong("newsId"));

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(news.getUrl());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        if (news.isFavorite()) {
            menu.findItem(R.id.favorite).setIcon(modifiedFavoriteIcon);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            return true;
        }

        if (id == R.id.favorite) {
            news.setFavorite(!news.isFavorite());
            news.save();
            if (news.isFavorite()) {
                item.setIcon(modifiedFavoriteIcon);
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
