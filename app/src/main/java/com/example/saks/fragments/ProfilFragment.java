package com.example.saks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.saks.R;
import com.example.saks.api.API_Access;
import com.example.saks.presence_list.PresenceUser;
import com.example.saks.presence_list.PresenceUserListAdapter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    private Socket ioSocket;

    public ProfilFragment() {
    }


    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }

    View binding;

    private void closeIO(){
        if (ioSocket != null) {
            if (ioSocket.connected()) {
                ioSocket.disconnect();
            }
            ioSocket.close();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        closeIO();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeIO();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeIO();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeIO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_profil, container, false);

        PresenceUserListAdapter presenceUserListAdapter = new PresenceUserListAdapter(getActivity());

        ListView listView = binding.findViewById(R.id.listView);
        listView.setAdapter(presenceUserListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            PresenceUser presenceUser = presenceUserListAdapter.getItem(position);
            Toast.makeText(getContext(), presenceUser.toString(), Toast.LENGTH_LONG).show();
        });
        try {
            ioSocket = IO.socket("ws://192.168.178.3:3030");
            ioSocket.on(Socket.EVENT_CONNECT, args -> {
                ioSocket.emit("token", API_Access.getToken(getContext()));
            });
            ioSocket.on("student", args1 -> {
                LinkedTreeMap presence = new Gson().fromJson(args1[0].toString(), LinkedTreeMap.class);
                int studentID = Math.round(Float.parseFloat(presence.get("user").toString()));
                double present_from = (double) presence.get("present_from");
                double present_until = (double) presence.get("present_until");
                double date = (double) presence.get("date");
                API_Access.getCall("/users/" + studentID, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null){
                            LinkedTreeMap student = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                            String className = "-";
                            String firstName = (String) student.get("firstname");
                            String lastName = (String) student.get("lastname");
                            String fullname = firstName;
                            String secondname = (String) student.get("secondname");
                            if (secondname != null && !secondname.equals("null")) {
                                fullname += " " + secondname;
                            }
                            fullname += " " + lastName;

                            if (student.containsKey("class")) {
                                LinkedTreeMap userClass = (LinkedTreeMap) student.get("class");
                                className = (String) userClass.get("short");
                            }
                            presenceUserListAdapter.addPresenceUser(new PresenceUser(fullname, studentID, className, Double.valueOf(date).longValue(), Double.valueOf(present_from).longValue(), Double.valueOf(present_until).longValue()));
                        }
                    }
                });
            });
            ioSocket.connect();

        } catch (URISyntaxException e) {

        }
        return binding;
    }
}