package com.ihandy.a2013011373.newsapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetRequestThread extends Thread {
    private String url;
    private StringBuilder output;
    private boolean success;

    public GetRequestThread(String url) {
        this.url = url;
        this.output = new StringBuilder();
        this.success = false;
    }

    public String getOutput() {
        return this.output.toString();
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                this.output.append(line);
            }
            in.close();
            this.success = true;
        } catch (Exception e) {
            Log.w("GetRequestThread", e);
        }
    }
}
