package com.rs.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recordscreen.R;
import com.rs.pojo.Record;

import java.util.List;

public class RecordsAdapter extends ArrayAdapter {

    private int resource;

    public RecordsAdapter(@NonNull Context context, int resource, List<Record> list) {
        super(context, resource, list);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Record each = (Record) getItem(position);
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.name = view.findViewById(R.id.rname);
            holder.duration = view.findViewById(R.id.duration);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(each.getFileName());
        holder.duration.setText(each.getDuration());

        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView duration;
    }
}
