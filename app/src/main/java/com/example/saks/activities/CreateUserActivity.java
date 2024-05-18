package com.example.saks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.example.saks.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateUserActivity extends AppCompatActivity {

    EditText firstname_edittext, lastname_edittext, secondname_edittext, id_edittext, shortname_edittext, permlevel_edittext, role_edittext;
    View binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        binding = findViewById(R.id.layout_result);
        Button createUserButton = findViewById(R.id.createuserButton);
        firstname_edittext = binding.findViewById(R.id.editTextFirstName);
        lastname_edittext = binding.findViewById(R.id.editLastName);
        secondname_edittext = binding.findViewById(R.id.editSecondname);
        id_edittext = binding.findViewById(R.id.editTextID);
        shortname_edittext = binding.findViewById(R.id.editTextShortName);
        permlevel_edittext = binding.findViewById(R.id.editTextPermLevel);
        role_edittext = binding.findViewById(R.id.editTextRole);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstname_edittext.getText().toString().isEmpty()) {
                    firstname_edittext.setError("Bitte Vorname eingeben");
                    firstname_edittext.requestFocus();
                    return;
                }
                if (lastname_edittext.getText().toString().isEmpty()) {
                    lastname_edittext.setError("Bitte Nachname eingeben");
                    lastname_edittext.requestFocus();
                    return;
                }
                HashMap<String, String> data = new HashMap<>();
                data.put("firstname", firstname_edittext.getText().toString());
                data.put("lastname", lastname_edittext.getText().toString());
                data.put("secondname", secondname_edittext.getText().toString());
                data.put("id", id_edittext.getText().toString());
                data.put("short_name", shortname_edittext.getText().toString());
                data.put("perm_level", permlevel_edittext.getText().toString());
                data.put("role", role_edittext.getText().toString());
                createUser(data);
            }
        });
    }

    private void createUser(HashMap<String, String> data) {
        HashMap<String, String> copy = new HashMap<>(data);
        for (String key : copy.keySet()) {
            if (data.get(key).isEmpty()) {
                data.remove(key);
            }
        }
        String json = new Gson().toJson(data);
        API_Access.postCall("/user", json, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Server-Timeout", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Fehler beim Erstellen des Benutzers", Toast.LENGTH_SHORT).show();
                    });
                }
                runOnUiThread(() -> {
                    LinkedTreeMap map;
                    try {
                        map = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                        String user_id = String.valueOf(Math.round(Double.parseDouble(map.get("user_id").toString())));
                        Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {

                    }
                });
            }
        });
    }

}