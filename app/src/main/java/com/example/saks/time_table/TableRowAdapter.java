package com.example.saks.time_table;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saks.R;

import java.util.ArrayList;
import java.util.List;

public class TableRowAdapter extends RecyclerView.Adapter<TableRowAdapter.ViewHolder> {

    private Activity activity;
    private List<TableRow> tableRows;

   public TableRowAdapter(Activity activity, List<TableRow> list) {
       this.activity = activity;
       this.tableRows = list;
   }

    public TableRowAdapter(Activity activity) {
        this.activity = activity;
        this.tableRows = new ArrayList<>();
    }

   public void addTableRow(TableRow tableRow) {
       tableRows.add(tableRow);
       activity.runOnUiThread(this::notifyDataSetChanged);
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.table_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tableRows != null && !tableRows.isEmpty()) {
            TableRow tableRow = tableRows.get(position);
            holder.uhrzeit_tv.setText(tableRow.getUhrzeit());
            holder.datum_tv.setText(tableRow.getFach());
        }
    }

    public void clearPresenceUsers() {
        tableRows.clear();
    }

    @Override
    public int getItemCount() {
        return tableRows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView uhrzeit_tv, datum_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            uhrzeit_tv = itemView.findViewById(R.id.uhrzeit_tv);
            datum_tv = itemView.findViewById(R.id.fach_tv);
        }
    }
}
