package com.example.saks.presence_list;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.saks.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class PresenceUserListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<PresenceUser> presenceUsers;

    public PresenceUserListAdapter(Activity activity) {
        this.activity = activity;
        presenceUsers = new ArrayList<>();
    }

    public void addPresenceUser(PresenceUser presenceUser) {
        presenceUsers.add(presenceUser);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }
    public void clearPresenceUsers() {
        presenceUsers.clear();
    }
    public void removePresenceUser(PresenceUser presenceUser) {
        presenceUsers.remove(presenceUser);
    }

    @Override
    public int getCount() {
        return presenceUsers.size();
    }

    @Override
    public PresenceUser getItem(int position) {
        return presenceUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = activity.getLayoutInflater().inflate(com.example.saks.R.layout.list_item, null);
        PresenceUser presenceUser = presenceUsers.get(position);
        TextView nameTextView = convertView.findViewById(R.id.listName);
        TextView classTextView = convertView.findViewById(R.id.listKlasse);
        TextView presentTextView = convertView.findViewById(R.id.present_text);
        classTextView.setText(presenceUser.getClass_());
        nameTextView.setText(presenceUser.getName());
        ShapeableImageView imageView = convertView.findViewById(R.id.present_mini_icon);
        if (presenceUser.isPresent()){
            imageView.setImageResource(R.drawable.baseline_assignment_turned_in_24);
            imageView.setColorFilter(activity.getColor(R.color.icon_green));
            presentTextView.setText(activity.getString(R.string.present));
        } else {
            imageView.setImageResource(R.drawable.remove_presence_icon);
            presentTextView.setText(activity.getString(R.string.not_present));
        }
        return convertView;
    }
}
