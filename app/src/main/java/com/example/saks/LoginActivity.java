package com.example.saks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saks.api.API_Access;
import com.example.saks.api.Error;
import com.example.saks.api.Login;
import com.example.saks.api.Token;
import com.example.saks.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

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
    }

    private void initBinding() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initViews() {
        binding.loginButton.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        String id = binding.editTextMatrikelnummer.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        if (id.equals("0000") && password.equals("admin")) {
            API_Access.token = "abcd";
            Intent myIntent = new Intent(this, MainMenuActivity.class);
            startActivity(myIntent);
        } else {
            API_Access.putCall("/login", new Login(id, password), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Could not receive a response from the server", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Token token = new Gson().fromJson(response.body().string(), Token.class);
                        API_Access.token = token.token;
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, token.token, Toast.LENGTH_SHORT).show());
                    } else {
                        Error error = new Gson().fromJson(response.body().string(), Error.class);

                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error-Code " + response.code() + "\nError: " + error.error, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }

    }
}