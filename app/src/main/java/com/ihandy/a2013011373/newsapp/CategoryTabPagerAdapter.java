package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CategoryTabPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> categoryTitles = new ArrayList<>();

    public CategoryTabPagerAdapter(FragmentManager fm,
                                   Context context, Map<String, String> remoteCategories) {
        super(fm);
        this.context = context;
        if (remoteCategories != null) {
            categories.addAll(remoteCategories.keySet());
            Collections.sort(categories);
            for (String category : categories) {
                categoryTitles.add(remoteCategories.get(category));
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (0 <= position && position < categories.size()) {
            return CategoryPageFragment.newInstance(categories.get(position), context);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (0 <= position && position < categories.size()) {
            return categoryTitles.get(position);
        } else {
            return null;
        }
    }
}
