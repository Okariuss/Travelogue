package com.okada.travelogue.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.R;

import java.util.ArrayList;

public class RecyclerAdapterMessage extends RecyclerView.Adapter<RecyclerAdapterMessage.MessageHolder> {
    ArrayList<String> keys,values;
    Context context;
    public RecyclerAdapterMessage(ArrayList<String> keys, ArrayList<String> values, Context context) {
        this.keys = keys;
        this.values = values;
        this.context=context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.recycler_message_item,parent,false);
        return new MessageHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.textView.setText(values.get(position));
        if (keys.get(position).contains("admin")){
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.admin_message_circle_drawable,null));
            holder.linearLayout.setGravity(Gravity.START);
        }else {
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.user_message_circle_drawable,null));
            holder.linearLayout.setGravity(Gravity.END);
        }

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout linearLayout;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.recycler_message_item_text_view);
            linearLayout=itemView.findViewById(R.id.recycler_message_item_layout);
        }
    }
}
