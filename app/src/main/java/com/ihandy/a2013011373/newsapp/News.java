package com.ihandy.a2013011373.newsapp;

import java.util.ArrayList;

public class News implements Comparable<News> {
    private long id;
    private String title = "";
    private Category category;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private String origin = "";
    private String url = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return id == news.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int compareTo(News another) {
        if (id > another.id) {
            return -1;
        } else if (id < another.id) {
            return 1;
        }
        return 0;
    }
}
