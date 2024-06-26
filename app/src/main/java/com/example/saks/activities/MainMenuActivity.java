package com.example.saks.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.example.saks.databinding.ActivityMainMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainMenuActivity extends AppCompatActivity {

    ActivityMainMenuBinding binding;
    NavController navController;
    int permLevel = 1;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCamera();
                }
                else {
                    Toast.makeText(this,"Keine Kamera Berechtigung", Toast.LENGTH_SHORT).show();
                }
            });

    private  ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
       if (result.getContents() != null) {
           setResult(result.getContents());
       }
    });

    private void setResult(String contents) {
        Uri uri = Uri.parse(contents);
        if (uri == null) {
            Toast.makeText(this, "QR Code konnte nicht erkannt werden. Bitte versuche es erneut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri.getHost() == null) {
            Toast.makeText(this, "QR Code konnte nicht erkannt werden. Bitte versuche es erneut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri.getHost().contains("saks-bbs2.de")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.example.saks");
            startActivity(intent);
        } else {
            Toast.makeText(this, "QR Code konnte nicht erkannt werden. Bitte versuche es erneut", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR Code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }



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

        Uri uri = getIntent().getData();
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).getNavController();

        if (API_Access.getToken(getApplicationContext()).isEmpty()){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            if (uri != null) {
                loginIntent.setData(uri);
            }
            startActivity(loginIntent);
            finish();
            return;
        }
        if (uri != null){
            Intent presenceIntent = new Intent(this, PresenceActivity.class);
            presenceIntent.setData(uri);
            startActivity(presenceIntent);
        }
        API_Access.getCall("/me", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Fehler beim Laden der Daten", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    LinkedTreeMap data = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                    if (data.containsKey("role") && data.get("role") != null) {
                        permLevel = Math.round(Float.parseFloat(data.get("role").toString()));
                    }
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // Handle the NDEF data here
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    private void initViews() {
        binding.fab.setOnClickListener(view -> checkPermissionAndShowActivity(this));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuNavigationView);
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).getNavController();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mmHome) {
                navController.navigate(R.id.homeFragment);
            }
            if (item.getItemId() == R.id.mmProfile) {
                if (permLevel != 3) {
                    navController.navigate(R.id.profilFragment);
                } else {
                    navController.navigate(R.id.adminFragment);
                }

            }
            if (item.getItemId() == R.id.mmSettings) {
                navController.navigate(R.id.settingsFragment);
            }
            if (item.getItemId() == R.id.mmTimeTable) {
                navController.navigate(R.id.timeTableFragment);
            }
            return true;
        });

    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Kamera Berechtigung benötigt", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    public void initBinding() {
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}