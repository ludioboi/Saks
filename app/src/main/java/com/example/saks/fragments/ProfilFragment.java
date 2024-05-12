package com.example.saks.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saks.R;
import com.example.saks.presence_list.PresenceUser;
import com.example.saks.presence_list.PresenceUserListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_profil, container, false);

        PresenceUserListAdapter presenceUserListAdapter = new PresenceUserListAdapter(getActivity());
        presenceUserListAdapter.addPresenceUser(new PresenceUser("Hans", 1, "FOI2A", 1715464800000L, 63420000L, 68400000L));

        ListView listView = binding.findViewById(R.id.listView);
        listView.setAdapter(presenceUserListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            PresenceUser presenceUser = presenceUserListAdapter.getItem(position);
            presenceUser.setName(String.valueOf(id));
            presenceUserListAdapter.notifyDataSetChanged();
        });

        return binding;
    }
}