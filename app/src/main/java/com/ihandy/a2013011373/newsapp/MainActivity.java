package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Context context = this;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<Category> categories = RequestManager.fetchCategories();
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPager.setAdapter(
                        new CategoryTabPagerAdapter(getSupportFragmentManager(),
                                context, categories));
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rebuildCategoryTabIfNeeded();
    }

    private void rebuildCategoryTabIfNeeded() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        CategoryTabPagerAdapter adapter = (CategoryTabPagerAdapter) viewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        List<Category> currentCategories = adapter.getCategories();
        List<Category> watchedCategories = Category.getAllWatched();
        boolean same = true;
        if (currentCategories.size() != watchedCategories.size()) {
            same = false;
        } else {
            for (Category category : currentCategories) {
                if (!watchedCategories.contains(category)) {
                    same = false;
                    break;
                }
            }
        }
        if (!same) {
            viewPager.setAdapter(new CategoryTabPagerAdapter(
                    getSupportFragmentManager(), this, null));
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), ViewFavoritesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_category_management) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), CategoryManageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_me) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
