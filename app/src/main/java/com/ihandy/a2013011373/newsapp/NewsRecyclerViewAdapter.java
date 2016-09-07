package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTextView;
        TextView originTexView;
        ImageView imageView;
        String imageUrl;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.news_card);
            titleTextView = (TextView) itemView.findViewById(R.id.news_title);
            originTexView = (TextView) itemView.findViewById(R.id.news_origin);
            imageView = (ImageView) itemView.findViewById(R.id.news_image);
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    News news = (News) v.getTag();
                    if (news.getUrl() == null || news.getUrl().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Error");
                        builder.setMessage("This news does not provide a link.");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        return;
                    }
                    Intent intent= new Intent();
                    intent.setClass(v.getContext(), ViewNewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", news.getUrl());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private List<News> newsList;
    private Context context;
    private static Bitmap placeholder;

    public NewsRecyclerViewAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        if (placeholder == null) {
            placeholder = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.placeholder);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.cardView.setTag(news);
        holder.titleTextView.setText(news.getTitle());
        holder.originTexView.setText(news.getOrigin());
        holder.imageUrl = null;
        holder.imageView.setImageBitmap(placeholder);
        if (news.getImageUrls().size() > 0) {
            String url = news.getImageUrls().get(0);
            holder.imageUrl = url;
            ImageManager imageManager = ImageManager.getInstance();
            if (imageManager.hasImage(url)) {
                Bitmap image = imageManager.getImage(url);
                if (image != null) {
                    holder.imageView.setImageBitmap(image);
                }
            } else {
                new DownloadImageTask(holder, url).execute(url);
            }
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private NewsViewHolder holder;
        private String url;

        public DownloadImageTask(NewsViewHolder holder, String url) {
            this.holder = holder;
            this.url = url;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.w("DownloadImageTask", e);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.putImage(url, result);
            if (url.equals(holder.imageUrl) && result != null) {
                holder.imageView.setImageBitmap(result);
            }
        }
    }
}
