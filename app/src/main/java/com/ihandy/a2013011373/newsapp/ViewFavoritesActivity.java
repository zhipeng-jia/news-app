package com.ihandy.a2013011373.newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewFavoritesActivity extends AppCompatActivity {
    private List<News> newsList;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsList = News.getAllFavorites();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new NewsRecyclerViewAdapter(newsList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeNoLongerFavoriteNews();
    }

    private void removeNoLongerFavoriteNews() {
        List<News> favoriteNews = News.getAllFavorites();
        Set<Long> favoriteNewsIds = new HashSet<>();
        for (News news : favoriteNews) {
            favoriteNewsIds.add(news.getNewsId());
        }
        for (int i = 0; i < newsList.size(); ) {
            if (!favoriteNewsIds.contains(newsList.get(i).getNewsId())) {
                newsList.remove(i);
                recyclerViewAdapter.notifyItemRemoved(i);
            } else {
                i++;
            }
        }
    }
}
