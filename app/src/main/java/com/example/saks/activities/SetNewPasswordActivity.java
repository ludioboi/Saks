package com.example.saks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetNewPasswordActivity extends AppCompatActivity {


    private EditText matrikelnummer_edittext, password_edittext;
    Button save_password_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_new_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        save_password_button = findViewById(R.id.save_password_button);
        matrikelnummer_edittext = findViewById(R.id.matrikelnummer_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        matrikelnummer_edittext.setText(getIntent().getStringExtra("id"));
        password_edittext.requestFocus();
        save_password_button.setOnClickListener(v -> {
            API_Access.setToken(getIntent().getStringExtra("token"), this);
            HashMap<String, String> params = new HashMap<>();
            params.put("password", password_edittext.getText().toString());
            API_Access.postCall("/login", new Gson().toJson(params), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    API_Access.setToken("", SetNewPasswordActivity.this);
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        LinkedTreeMap data = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                        API_Access.setToken(data.get("token").toString(), SetNewPasswordActivity.this);
                        Intent intent = new Intent(SetNewPasswordActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            });
        });
    }
}