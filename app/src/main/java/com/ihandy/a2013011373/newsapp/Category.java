package com.ihandy.a2013011373.newsapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Categories")
public class Category extends Model implements Comparable<Category> {
    @Column(name = "Name", index = true, unique = true)
    private String name = "";

    @Column(name = "DisplayName")
    private String displayName = "";

    @Column(name = "Watched")
    private boolean watched = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public static Category getByName(String name) {
        return new Select().from(Category.class).where("Name = ?", name).executeSingle();
    }

    public static List<Category> getAll() {
        return new Select().from(Category.class).orderBy("Name ASC").execute();
    }

    public static List<Category> getAllWatched() {
        return new Select().from(Category.class)
                .where("Watched = ?", true).orderBy("Name ASC").execute();
    }

    public static void mergeWithRemote(List<Category> remoteCategories) {
        List<Category> categories = getAll();
        for (Category category : categories) {
            if (!remoteCategories.contains(category)) {
                category.delete();
            }
        }
        for (Category category : remoteCategories) {
            if (!categories.contains(category)) {
                category.save();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return name != null ? name.equals(category.name) : category.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(Category another) {
        return name.compareTo(another.name);
    }
}
