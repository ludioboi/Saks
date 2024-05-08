package com.example.saks.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saks.R;
import com.example.saks.api.API_Access;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PresenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_presence);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getIntent().getData() != null) {
            handleUri(getIntent().getData());
        }
    }


    public void handleUri(Uri uri){
        String data = uri.toString();
        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(Color.RED);
        textView.setText(uri.getPath() + "?" + uri.getQuery());
        API_Access.postCall(uri.getPath() + "?" + uri.getQuery(), "{}", new Callback() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                textView.setTextColor(Color.GREEN);
                textView.setText(response.body().string());
            }
        });

    }
}