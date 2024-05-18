package com.example.saks.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.saks.R;
import com.example.saks.activities.CreateUserActivity;
import com.example.saks.api.API_Access;
import com.example.saks.search.SearchUserAdapter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    public View binding;
    private ListView listView;

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getUsers(String query) {
        if (query.isEmpty()) {
            API_Access.getCall("/users", new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            ArrayList<LinkedTreeMap<String, Object>> data = new Gson().fromJson(response.body().string(), ArrayList.class);
                            getActivity().runOnUiThread(() -> listView.setAdapter(new SearchUserAdapter(getActivity(), data)));
                        }
                    }
                }
            });
        } else {
            API_Access.getCall("/user/search?q=" + query, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            ArrayList<LinkedTreeMap<String, Object>> data = new Gson().fromJson(response.body().string(), ArrayList.class);
                            getActivity().runOnUiThread(() -> listView.setAdapter(new SearchUserAdapter(getActivity(), data)));
                        }
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflater.inflate(R.layout.fragment_admin, container, false);
        SearchView searchView = binding.findViewById(R.id.searchView);
        listView = binding.findViewById(R.id.userListView);
        binding.findViewById(R.id.create_user_button).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateUserActivity.class);
            startActivity(intent);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getUsers(newText);
                return false;
            }
        });
        getUsers("");
        return binding;
    }
}