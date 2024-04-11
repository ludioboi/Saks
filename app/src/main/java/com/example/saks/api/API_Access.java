package com.example.saks.api;

import android.util.Log;

import androidx.navigation.PopUpToBuilder;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API_Access {

    static String baseurl = "http://192.168.178.3:8080";
    public static String token;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    public static void putCall(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseurl + url)
                .put(body)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void putCall(String url, Object json, Callback callback) {
        putCall(url, new Gson().toJson(json), callback);
    }

    public static void postCall(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseurl + url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void postCall(String url, Object json, Callback callback) {
        postCall(url, new Gson().toJson(json), callback);
    }

    public static void getCall(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(baseurl + url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteCall(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(baseurl + url)
                .delete()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteCall(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseurl + url)
                .delete(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteCall(String url, Object json, Callback callback) {
        postCall(url, new Gson().toJson(json), callback);
    }
}
