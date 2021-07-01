package com.okada.travelogue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.okada.travelogue.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    ArrayList<String> items;
    public SpinnerAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
        items= (ArrayList<String>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_res,parent,false    );
        }
        TextView textView=convertView.findViewById(R.id.spinner_item_text);
        textView.setText(items.get(position));
        return convertView;

    }
}
