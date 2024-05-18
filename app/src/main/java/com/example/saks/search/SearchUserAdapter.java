package com.example.saks.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.saks.R;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class SearchUserAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<SearchUserEntry> users;

    public SearchUserAdapter(Activity activity, ArrayList<LinkedTreeMap<String, Object>> data) {
        this.activity = activity;
        users = new ArrayList<>();
        for (LinkedTreeMap<String, Object> user : data) {
            SearchUserEntry entry = new SearchUserEntry(user);
            users.add(entry);
        }
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.user_item, null);
        SearchUserEntry entry = users.get(position);
        TextView nameTextView = convertView.findViewById(R.id.user_name_textview);
        TextView idTextView = convertView.findViewById(R.id.user_id_textview);
        TextView roleTextView = convertView.findViewById(R.id.user_role_textview);
        TextView shortNameTextView = convertView.findViewById(R.id.short_name_textview);
        shortNameTextView.setText(entry.getShort_name());
        nameTextView.setText(entry.getFullName());
        idTextView.setText(entry.getId());
        roleTextView.setText(entry.getRoleName());
        return convertView;
    }
}
