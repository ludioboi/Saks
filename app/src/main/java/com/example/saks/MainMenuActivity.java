package com.example.saks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saks.databinding.ActivityMainBinding;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.saks.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void qrCode() {

    }
}