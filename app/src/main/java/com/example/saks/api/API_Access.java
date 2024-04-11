package com.example.saks.api;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API_Access {

    static String baseurl = "https://laser-terrible-martin-additions.trycloudflare.com";
    static OkHttpClient client = new OkHttpClient();
    public static <T> T PutCall(String path, RequestBody body, T t) throws IOException {


        Request request = new Request.Builder()
                .url(baseurl + path)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        T obj= new Gson().fromJson(response.body().string(), (Type) t);
        return obj;
    }

    public static String login(int id, String password) throws IOException, IllegalAccessException, InstantiationException {
        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("password", password)
                .build();
        Token t = PutCall("/login", formBody, Token.class).newInstance();
        return t.token;
    }
}
