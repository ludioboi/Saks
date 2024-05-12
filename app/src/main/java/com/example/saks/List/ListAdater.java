package com.example.saks.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saks.R;

import java.util.ArrayList;

public class ListAdater extends ArrayAdapter<ListData> {

    public ListAdater(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView listName = view.findViewById(R.id.listName);
        TextView listKlasse = view.findViewById(R.id.listKlasse);

        listName.setText(listData.name);
        listKlasse.setText(listData.klasse);

        return view;
    }
}
