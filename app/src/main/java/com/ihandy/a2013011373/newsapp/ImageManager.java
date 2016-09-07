package com.ihandy.a2013011373.newsapp;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private Map<String, Bitmap> images;

    private static ImageManager instance = new ImageManager();

    public static ImageManager getInstance() {
        return instance;
    }

    private ImageManager() {
        images = Collections.synchronizedMap(new HashMap<String, Bitmap>());
    }

    public boolean hasImage(String url) {
        return images.containsKey(url);
    }

    public Bitmap getImage(String url) {
        return images.get(url);
    }

    public void putImage(String url, Bitmap image) {
        images.put(url, image);
    }
}
