package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CategoryPageFragment extends Fragment {
    private static final String ARG_CATEGORY = "ARG_CATEGORY";

    private Context context;
    private Category category;
    private View rootView;

    public static CategoryPageFragment newInstance(Category category, Context context) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, category);
        CategoryPageFragment fragment = new CategoryPageFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        return fragment;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = (Category) getArguments().getSerializable(ARG_CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_page, container, false);
        rootView = view;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<News> newsList = RequestManager.fetchNews(category, -1);
                if (newsList != null) {
                    addNewsCards(newsList);
                }
            }
        });
        return view;
    }

    private void addNewsCards(List<News> newsList) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NewsRecyclerViewAdapter(newsList, context));
    }
}
