package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CategoryTabPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Category> categories;

    public CategoryTabPagerAdapter(FragmentManager fm,
                                   Context context, List<Category> remoteCategories) {
        super(fm);
        this.context = context;
        if (remoteCategories != null) {
            Category.mergeWithRemote(remoteCategories);
        }
        categories = Category.getAllWatched();
    }

    public List<Category> getCategories() {
        return categories;
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
            return categories.get(position).getDisplayName();
        } else {
            return null;
        }
    }
}
