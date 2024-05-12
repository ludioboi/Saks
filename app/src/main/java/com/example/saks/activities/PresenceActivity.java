package com.example.saks.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PresenceActivity extends AppCompatActivity {

    ImageView icon_feedback;
    ProgressBar loadingBar;
    TextView text_feedback;
    Button remove_present_button, set_absence_button, set_new_presence_button;
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
        loadingBar = findViewById(R.id.loading_bar);
        remove_present_button = findViewById(R.id.remove_present_button);
        set_absence_button = findViewById(R.id.set_absence_button);
        set_new_presence_button = findViewById(R.id.set_new_presence_button);
        waitForFeedback();
        if (getIntent().getData() != null) {
            handleUri(getIntent().getData(), "{}", "Du wurdest erfolgreich als ANWESEND markiert");
            HashMap<String, String> body = new HashMap<>();
            remove_present_button.setOnClickListener((view) -> {
                body.put("action", "set_absent");
                handleUri(getIntent().getData(), new Gson().toJson(body), "Deine Anwesenheit wurde erfolgreich entfernt");
            });
            set_absence_button.setOnClickListener((view) -> {
                body.put("action", "set_present_until");
                handleUri(getIntent().getData(), new Gson().toJson(body), "Deine Abwesenheit wurde erfolgreich gesetzt");
            });
            set_new_presence_button.setOnClickListener((view) -> {
                body.put("action", "set_present_from");
                handleUri(getIntent().getData(), new Gson().toJson(body), "Deine Anwesenheit wurde erfolgreich aktualisiert");
            });
        }
    }

    private void showButtons(){
        remove_present_button.setVisibility(View.VISIBLE);
        set_absence_button.setVisibility(View.VISIBLE);
        set_new_presence_button.setVisibility(View.VISIBLE);
    }
    private void waitForFeedback(){
        text_feedback.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);
        icon_feedback.setVisibility(View.INVISIBLE);
        remove_present_button.setVisibility(View.INVISIBLE);
        set_absence_button.setVisibility(View.INVISIBLE);
        set_new_presence_button.setVisibility(View.INVISIBLE);
    }

    private void setFeedbackText(String text){
        text_feedback.setVisibility(View.VISIBLE);
        text_feedback.setText(text);
    }

    private void setFeedbackBackground(Drawable drawable){
        text_feedback.setBackground(drawable);
    }
    public void handleUri(Uri uri, String json, String successText){
        waitForFeedback();
        API_Access.postCall(uri.getPath() + "?" + uri.getQuery(), json, new Callback() {
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    loadingBar.setVisibility(View.GONE);
                    icon_feedback.setVisibility(View.VISIBLE);
                    setFeedbackText("Es kam keine Antwort vom Server");
                    setFeedbackBackground(getDrawable(R.drawable.feedback_red));
                    icon_feedback.setImageResource(R.drawable.baseline_warning_24);
                });
            }

            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {


                loadingBar.setVisibility(View.GONE);
                if (response.code() == 200){
                    icon_feedback.setVisibility(View.VISIBLE);
                    icon_feedback.setImageResource(R.drawable.baseline_assignment_turned_in_24);
                    setFeedbackText(successText);
                    setFeedbackBackground(getDrawable(R.drawable.feedback_text_green));
                } else {
                    if (response.code() == 330) {
                        icon_feedback.setVisibility(View.VISIBLE);
                        icon_feedback.setImageResource(R.drawable.baseline_assignment_late_24);
                        setFeedbackText("Du bist bereits als ANWESEND markiert");
                        setFeedbackBackground(getDrawable(R.drawable.feedback_red));
                        showButtons();
                    } else if (response.code() == 331) {
                        icon_feedback.setVisibility(View.VISIBLE);
                        icon_feedback.setImageResource(R.drawable.baseline_assignment_late_24);
                        setFeedbackText("Du bist keiner Klasse zugewiesen");
                        setFeedbackBackground(getDrawable(R.drawable.feedback_red));
                    } else if (response.code() == 332) {
                        icon_feedback.setVisibility(View.VISIBLE);
                        icon_feedback.setImageResource(R.drawable.baseline_assignment_late_24);
                        setFeedbackText("Deine Klasse konnte nicht gefunden werden");
                        setFeedbackBackground(getDrawable(R.drawable.feedback_red));
                    } else  {
                        icon_feedback.setVisibility(View.VISIBLE);
                        icon_feedback.setImageResource(R.drawable.baseline_warning_24);
                        try {
                            setFeedbackText(getResources().getString(R.string.server_connection_failed) + "\n" + response.code() + "\n" + response.body().string());
                        } catch (IOException e) {

                        }
                        setFeedbackBackground(getDrawable(R.drawable.feedback_red));
                    }

                }
                });
            }
        });

    }
}