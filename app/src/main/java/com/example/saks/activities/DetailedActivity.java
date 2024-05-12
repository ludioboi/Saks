package com.example.saks.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saks.R;

public class DetailedActivity extends AppCompatActivity {

    TextView name;
    TextView status;
    TextView date;
    TextView time;
    TextView klasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.detailName);
        status = findViewById(R.id.detailStatus);
        date = findViewById(R.id.detailDate);
        time = findViewById(R.id.detailTime);
        klasse = findViewById(R.id.detailKlasse);

        name.setText("Hans");
        klasse.setText("FOI2A");
    }


}