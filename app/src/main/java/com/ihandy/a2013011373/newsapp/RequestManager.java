package com.ihandy.a2013011373.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RequestManager {
    private static final String CATEGORY_REQUEST_URL = "http://assignment.crazz.cn/news/en/category";
    private static final String NEWS_REQUEST_URL = "http://assignment.crazz.cn/news/query";

    public static List<Category> fetchCategories() {
        try {
            GetRequestThread requestThread = new GetRequestThread(
                    CATEGORY_REQUEST_URL + "?timestamp=" + System.currentTimeMillis());
            requestThread.start();
            requestThread.join();
            if (!requestThread.isSuccess()) {
                return null;
            }
            JSONObject response = new JSONObject(requestThread.getOutput());
            List<Category> categories = new ArrayList<>();
            JSONObject target = response.getJSONObject("data").getJSONObject("categories");
            Iterator iterator = target.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = target.getString(key);
                Category category = new Category();
                category.setName(key);
                category.setDisplayName(value);
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            Log.w("RequestManager", e);
        }
        return null;
    }

    public static List<News> fetchNews(Category category, long maxNewsId) {
        try {
            String requestUrl = NEWS_REQUEST_URL + "?locale=en";
            requestUrl += "&category=" + category.getName();
            if (maxNewsId != -1) {
                requestUrl += "&max_news_id=" + maxNewsId;
            }
            GetRequestThread requestThread = new GetRequestThread(requestUrl);
            requestThread.start();
            requestThread.join();
            if (!requestThread.isSuccess()) {
                return null;
            }
            JSONObject response = new JSONObject(requestThread.getOutput());
            List<News> newsList = new ArrayList<>();
            JSONArray target = response.getJSONObject("data").getJSONArray("news");
            for (int i = 0; i < target.length(); i++) {
                JSONObject entry = target.getJSONObject(i);
                News news = new News();
                news.setNewsId(entry.getLong("news_id"));
                news.setTitle(entry.getString("title"));
                news.setCategory(category);
                news.setOrigin(entry.getString("origin"));
                if (!entry.isNull("source")) {
                    news.setUrl(entry.getJSONObject("source").getString("url"));
                }
                JSONArray images = entry.getJSONArray("imgs");
                if (images.length() > 0) {
                    news.setImageUrl(images.getJSONObject(0).getString("url"));
                }
                newsList.add(news);
            }
            Collections.sort(newsList);
            return newsList;
        } catch (Exception e) {
            Log.w("RequestManager", e);
        }
        return null;
    }
}
