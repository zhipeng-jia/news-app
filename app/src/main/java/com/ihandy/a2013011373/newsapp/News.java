package com.ihandy.a2013011373.newsapp;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "News")
public class News extends Model implements Comparable<News> {
    @Column(name = "NewsId", index = true, unique = true,
            onUniqueConflict = Column.ConflictAction.IGNORE)
    private long newsId;

    @Column(name = "Title")
    private String title = "";

    @Column(name = "Category")
    private Category category;

    @Column(name = "ImageUrl")
    private String imageUrl = "";

    @Column(name = "Origin")
    private String origin = "";

    @Column(name = "Url")
    private String url = "";

    @Column(name = "Favorite")
    private boolean favorite = false;

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public static News getByNewsId(long newsId) {
        return new Select().from(News.class).where("NewsId = ?", newsId).executeSingle();
    }

    public static List<News> getByCategory(Category category) {
        return new Select().from(News.class)
                .where("Category = ?", category.getId()).orderBy("NewsId DESC").execute();
    }

    public static List<News> getAllFavorites() {
        return new Select().from(News.class)
                .where("Favorite = ?", true).orderBy("NewsId DESC").execute();
    }

    public static void insertAll(List<News> newsList) {
        ActiveAndroid.beginTransaction();
        try {
            for (News news : newsList) {
                news.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return newsId == news.newsId;

    }

    @Override
    public int hashCode() {
        return (int) (newsId ^ (newsId >>> 32));
    }

    @Override
    public int compareTo(News another) {
        if (newsId > another.newsId) {
            return -1;
        } else if (newsId < another.newsId) {
            return 1;
        }
        return 0;
    }
}
