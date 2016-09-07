package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CategoryPageFragment extends Fragment {
    private static final String ARG_CATEGORY = "ARG_CATEGORY";

    private Context context;
    private Category category;
    private List<News> newsList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter recyclerViewAdapter;

    private boolean ongoingFetchMoreNews = false;

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
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new NewsRecyclerViewAdapter(newsList, context);
        recyclerView.setAdapter(recyclerViewAdapter);

        initSwipeRefreshLayout();
        initRecyclerView();
        refreshNews();

        return view;
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        fetchMoreNews();
                    }
                }
            }
        });
    }

    private void refreshNews() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<News> remoteNewsList = RequestManager.fetchNews(category, -1);
                if (remoteNewsList != null) {
                    newsList.clear();
                    newsList.addAll(remoteNewsList);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchMoreNews() {
        if (ongoingFetchMoreNews) {
            return;
        }
        ongoingFetchMoreNews = true;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                long lastNewsId = -1;
                if (newsList.size() > 0) {
                    lastNewsId = newsList.get(newsList.size() - 1).getId() - 1;
                }
                List<News> remoteNewsList = RequestManager.fetchNews(category, lastNewsId);
                if (remoteNewsList != null) {
                    for (News news : remoteNewsList) {
                        newsList.add(news);
                        recyclerViewAdapter.notifyItemChanged(newsList.size() - 1);
                    }
                }
                ongoingFetchMoreNews = false;
            }
        });
    }
}
