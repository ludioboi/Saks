package com.example.saks.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    ImageView icon_feedback;
    ProgressBar loadingBar;
    TextView text_feedback;
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
        icon_feedback = findViewById(R.id.icon_feedback);
        text_feedback = findViewById(R.id.text_feedback);
        text_feedback.setVisibility(View.GONE);
        loadingBar = findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);
        icon_feedback.setVisibility(View.GONE);
        if (getIntent().getData() != null) {
            handleUri(getIntent().getData());
        }
    }


    public void handleUri(Uri uri){
        API_Access.postCall(uri.getPath() + "?" + uri.getQuery(), "{}", new Callback() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    loadingBar.setVisibility(View.GONE);
                    icon_feedback.setVisibility(View.VISIBLE);
                    text_feedback.setVisibility(View.VISIBLE);
                    text_feedback.setText("Es kam keine Antwort vom Server");
                    icon_feedback.setImageResource(R.drawable.baseline_warning_24);
                });
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    loadingBar.setVisibility(View.GONE);
                });
                if (response.isSuccessful()){
                    runOnUiThread(() -> {
                        icon_feedback.setVisibility(View.VISIBLE);
                        icon_feedback.setImageResource(R.drawable.baseline_assignment_turned_in_24);
                    });
                }

            }
        });

    }
}