package com.example.saks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.example.saks.time_table.TableRow;
import com.example.saks.time_table.TableRowAdapter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {

    private Socket ioSocket;

    public TimeTableFragment() {
    }

    public static TimeTableFragment newInstance() {
        TimeTableFragment fragment = new TimeTableFragment();
        return fragment;
    }

    View binding;
    RecyclerView recycler_view;
    TableRowAdapter tableRowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_time_table, container, false);

        recycler_view = binding.findViewById(R.id.recycler_view);

        setRecyclerView();

        API_Access.getCall("/me/schedule", new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                getActivity().runOnUiThread(() -> {
                    if (response.body() != null){
                        Toast.makeText(getContext(), json, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Body is null", Toast.LENGTH_SHORT).show();
                    }
                });
                if (response.code() != 200) {
                    return;
                }
                ArrayList<LinkedTreeMap<String, String>> data = new Gson().fromJson(json, ArrayList.class);

                for(LinkedTreeMap<String, String> fach : data) {
                    String start_timeString = fach.get("start_time");
                    String end_timeString = fach.get("end_time");

                    long start_time = Long.parseLong(start_timeString);
                    long end_time = Long.parseLong(end_timeString);

                    int timeId = Integer.parseInt(fach.get("time_id"));


                    long hours = TimeUnit.MILLISECONDS.toHours(start_time);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(start_time);
                    String room_short = fach.get("room_short");
                    String subject = (String) fach.get("subject");
                    boolean double_lesson = Objects.equals(fach.get("double_lesson"), "1");
                    tableRowAdapter.addTableRow(new TableRow(hours + ":" + minutes, subject, "Lehrer", 1, room_short, "Montag", double_lesson));
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
            });

            return binding;
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        tableRowAdapter = new TableRowAdapter(getActivity(), getList());
        recycler_view.setAdapter(tableRowAdapter);
    }

    private List<TableRow> getList() {
        List<TableRow> list = new ArrayList<>();
        return list;
    }
}