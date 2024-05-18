package com.example.saks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saks.api.API_Access;
import com.example.saks.api.Error;
import com.example.saks.api.Login;
import com.example.saks.api.Token;
import com.example.saks.databinding.ActivityLoginBinding;
import com.example.saks.time_table.TableRowAdapter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAndRemoveTask();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void initBinding() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initViews() {
        binding.loginButton.setOnClickListener(this::onClick);
        binding.shortkeyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShortKeyActivity.class);
            startActivity(intent);
        });
    }

    private void onClick(View v) {
        String id = binding.editTextMatrikelnummer.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        if (id.equals("0000") && password.equals("admin")) {
            API_Access.setToken("abcd", getApplicationContext());
            Intent myIntent = new Intent(this, MainMenuActivity.class);
            if (getIntent().getData() != null){
                myIntent.setData(getIntent().getData());
            }
            startActivity(myIntent);
            finish();
        } else {
            API_Access.putCall("/login", new Login(id, password), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Could not receive a response from the server", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() == 303) {
                        LinkedTreeMap<String, String> data = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Bitte lege ein neues Passwort fest", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, SetNewPasswordActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("token", data.get("token"));
                            startActivity(intent);
                        });
                        return;
                    }
                    if (response.isSuccessful()) {
                        Token token = new Gson().fromJson(response.body().string(), Token.class);
                        API_Access.setToken(token.token, getApplicationContext());
                        Intent myIntent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(myIntent);
                        finish();
                    } else {
                        Error error = new Gson().fromJson(response.body().string(), Error.class);

                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error-Code " + response.code() + "\nError: " + error.message, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }

    }
}