package com.example.saks;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saks.api.API_Access;
import com.example.saks.api.Error;
import com.example.saks.api.Login;
import com.example.saks.api.Token;
import com.example.saks.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        binding.loginButton.setOnClickListener(v -> API_Access.putCall("/login", new Login(binding.editTextMatrikelnummer.getText().toString(), binding.editTextPassword.getText().toString()), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Could not receive a response from the server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    Token token = new Gson().fromJson(response.body().string(), Token.class);
                    API_Access.token = token.token;
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, token.token, Toast.LENGTH_SHORT).show());
                } else {
                    Error error = new Gson().fromJson(response.body().string(), Error.class);

                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error-Code " + response.code() + "\nError: " + error.error, Toast.LENGTH_SHORT).show());
                }
            }
        }));
    }


}