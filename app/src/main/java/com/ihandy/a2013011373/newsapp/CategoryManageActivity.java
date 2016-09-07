package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import java.util.ArrayList;
import java.util.List;

public class CategoryManageActivity extends AppCompatActivity {
    private CategoryEntriesAdapter watchedEntriesAdapter;
    private CategoryEntriesAdapter unwatchedEntriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Category> categories = Category.getAll();
        List<Category> watchedCategories = new ArrayList<>();
        List<Category> unwatchedCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.isWatched()) {
                watchedCategories.add(category);
            } else {
                unwatchedCategories.add(category);
            }
        }

        LinearListView watchedListView = (LinearListView) findViewById(R.id.watched_list);
        LinearListView unwatchedListView = (LinearListView) findViewById(R.id.unwatched_list);
        watchedEntriesAdapter = new CategoryEntriesAdapter(
                this, R.drawable.ic_clear_black_24dp, watchedCategories);
        unwatchedEntriesAdapter = new CategoryEntriesAdapter(
                this, R.drawable.ic_add_black_24dp, unwatchedCategories);
        watchedListView.setAdapter(watchedEntriesAdapter);
        unwatchedListView.setAdapter(unwatchedEntriesAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void toggleCategoryWatchStatus(Category category) {
        if (category.isWatched()) {
            watchedEntriesAdapter.remove(category);
            unwatchedEntriesAdapter.insertByOrder(category);
        } else {
            unwatchedEntriesAdapter.remove(category);
            watchedEntriesAdapter.insertByOrder(category);
        }
        category.setWatched(!category.isWatched());
        category.save();
    }

    private class CategoryEntriesAdapter extends ArrayAdapter<Category> {
        private int buttonImageResourceId;

        public CategoryEntriesAdapter(Context context, int buttonImageResourceId,
                                      List<Category> categories) {
            super(context, R.layout.category_entry, categories);
            this.buttonImageResourceId = buttonImageResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Category category = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.category_entry, parent, false);
                ImageButton button = (ImageButton) convertView.findViewById(R.id.button);
                button.setImageResource(buttonImageResourceId);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleCategoryWatchStatus((Category) v.getTag());
                    }
                });
            }
            TextView textView = (TextView) convertView.findViewById(R.id.category_name);
            ImageButton button = (ImageButton) convertView.findViewById(R.id.button);
            textView.setText(category.getDisplayName());
            button.setTag(category);
            return convertView;
        }

        public void insertByOrder(Category category) {
            int i = 0;
            while (i < getCount()) {
                if (category.compareTo(getItem(i)) < 0) {
                    break;
                }
                i++;
            }
            insert(category, i);
        }
    }
}