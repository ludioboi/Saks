package com.example.saks.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShortKeyActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView select, class_name;
    EditText short_key_edittext;
    private List<LinkedTreeMap> data;
    private Button accept_shortkey_button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_short_key);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinner = findViewById(R.id.spinner2);
        select = findViewById(R.id.textView);
        class_name = findViewById(R.id.class_name);
        accept_shortkey_button = findViewById(R.id.accept_shortkey_button);
        short_key_edittext = findViewById(R.id.short_key_edittext);

        accept_shortkey_button.setOnClickListener(v -> {
            String classID = data.get(spinner.getSelectedItemPosition()).get("id").toString();
            String shortkey = short_key_edittext.getText().toString();
            API_Access.getCall("/shortkey/" + classID + "/" + shortkey, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        LinkedTreeMap data = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                        String userID = data.get("user_id").toString();
                        String token = data.get("token").toString();
                        Intent intent = new Intent(ShortKeyActivity.this, SetNewPasswordActivity.class);
                        intent.putExtra("id", String.valueOf(Math.round(Float.parseFloat(userID))));
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                }
            });
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinkedTreeMap d = data.get(position);
                runOnUiThread(() -> {
                    class_name.setText(d.get("description").toString());
                    accept_shortkey_button.setEnabled(true);
                    short_key_edittext.setEnabled(true);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> list = new ArrayList<>();

        API_Access.getCall("/classes", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                data = new Gson().fromJson(response.body().string(), List.class);
                for (LinkedTreeMap d : data) {
                    list.add(d.get("short").toString());
                }
                ArrayAdapter<String> adp1 = new ArrayAdapter<>(ShortKeyActivity.this,
                        android.R.layout.simple_list_item_1, list);
                runOnUiThread(() -> {
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adp1);
                });
            }
        });

    }
}