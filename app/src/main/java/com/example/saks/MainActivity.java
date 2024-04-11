package com.example.saks;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.saks.api.API_Access;
import com.example.saks.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    protected EditText editTextMatrikelnummer, editTextPassword;
    protected Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextMatrikelnummer = (EditText) findViewById(R.id.editTextMatrikelnummer);

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

        loginButton.setOnClickListener(v -> {
            String token = null;
            try {
                token = API_Access.login(editTextMatrikelnummer.getText().toString(), editTextPassword.toString());
                Toast.makeText(this, token, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("DEBUG", "IOException", e);
            } catch (IllegalAccessException e) {
                Log.e("DEBUG", "IllegalAccessException", e);
            } catch (InstantiationException e) {
                Log.e("DEBUG", "InstantiationException", e);
            }




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