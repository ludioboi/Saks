package com.example.saks.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ProgressBar spinner;
    private TextView class_textview, room_textview, anwesenheit_textview, connection_feedback_textview;
    LinearLayout connection_feedback_layout;
    private LinearLayoutCompat linearLayout;
    Button try_again_button;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUpdates();
    }

    private void hideTextviews(){
        class_textview.setVisibility(View.INVISIBLE);
        room_textview.setVisibility(View.INVISIBLE);
        anwesenheit_textview.setVisibility(View.INVISIBLE);
    }

    private void showTextviews(){
        class_textview.setVisibility(View.VISIBLE);
        room_textview.setVisibility(View.VISIBLE);
        anwesenheit_textview.setVisibility(View.VISIBLE);

    }


    public void getUpdates(){
        linearLayout.setAlpha(0.3f);
        linearLayout.setClickable(false);
        connection_feedback_layout.setVisibility(View.VISIBLE);
        try_again_button.setVisibility(View.GONE);
        connection_feedback_textview.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        API_Access.getCall("/me", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try_again_button.setVisibility(View.VISIBLE);
                        connection_feedback_textview.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                    });
                }

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        connection_feedback_layout.setVisibility(View.GONE);
                        linearLayout.setAlpha(1);
                        linearLayout.setClickable(true);

                        if (response.code() == 200) {
                            if (response.body() != null) {
                                try {
                                    LinkedTreeMap data = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                                    if (data.get("class") != null) {
                                        LinkedTreeMap class_data = (LinkedTreeMap) data.get("class");
                                        class_textview.setText(class_data.get("short").toString());
                                        class_textview.setVisibility(View.VISIBLE);
                                        if (data.get("subject") != null) {
                                            LinkedTreeMap subject_data = (LinkedTreeMap) data.get("subject");
                                            room_textview.setText(subject_data.get("room_short").toString());
                                            room_textview.setVisibility(View.VISIBLE);
                                            anwesenheit_textview.setVisibility(View.VISIBLE);
                                            if ((boolean) data.get("is_present")) {
                                                anwesenheit_textview.setText(getString(R.string.present));
                                                anwesenheit_textview.setTextColor(Color.GREEN);
                                            } else {
                                                anwesenheit_textview.setText(getString(R.string.not_present));
                                                anwesenheit_textview.setTextColor(Color.RED);
                                            }
                                        }
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_home, container, false);
        spinner = inflated.findViewById(R.id.progressBar);
        class_textview = inflated.findViewById(R.id.class_textview);
        room_textview = inflated.findViewById(R.id.room_textview);
        anwesenheit_textview = inflated.findViewById(R.id.anwesenheit_textview);
        linearLayout = inflated.findViewById(R.id.linearLayoutCompat);
        connection_feedback_layout = inflated.findViewById(R.id.connection_feedback_layout);
        connection_feedback_textview = inflated.findViewById(R.id.connection_feedback_textview);
        try_again_button = inflated.findViewById(R.id.try_again_button);
        try_again_button.setOnClickListener(v -> getUpdates());
        hideTextviews();
        //getUpdates();
        return inflated;

    }
}