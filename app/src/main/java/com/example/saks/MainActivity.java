package com.example.saks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.saks.api.API_Access;
import com.example.saks.api.Error;
import com.example.saks.api.Login;
import com.example.saks.api.Person;
import com.example.saks.api.Token;
import com.example.saks.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

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
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), (isGranted) -> {
                if(isGranted) {
                    showCamera();
                }
            });

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // QR Scanning success
            Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

        } else {
            // QR Scanning failed
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    });


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



    private void initViews() {
        binding.fab.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
        });

        binding.loginButton.setOnClickListener(v -> {
            API_Access.putCall("/login", new Login(binding.editTextMatrikelnummer.getText().toString(), binding.editTextPassword.getText().toString()), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Could not receive a response from the server", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        Token token = new Gson().fromJson(response.body().string(), Token.class);
                        API_Access.token = token.token;;
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, token.token, Toast.LENGTH_SHORT).show();
                        });


                        API_Access.getCall("/me", new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()){
                                    String res = response.body().string();
                                    runOnUiThread(() -> {
                                        binding.textResult.setText(res);
                                    });
                                    Person me = new Gson().fromJson(res, Person.class);
                                    runOnUiThread(() -> {
                                        Toast.makeText(MainActivity.this, me.toString(), Toast.LENGTH_LONG).show();
                                    });
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(MainActivity.this, "Could not get /me", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        });

                    } else {
                        Error error = new Gson().fromJson(response.body().string(), Error.class);

                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Error-Code " + response.code() + "\nError: " + error.error, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }

    }



}