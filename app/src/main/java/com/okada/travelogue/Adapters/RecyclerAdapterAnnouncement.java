package com.okada.travelogue.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.HelperClasses.AnnouncementClass;
import com.okada.travelogue.R;

import java.util.Map;

public class RecyclerAdapterAnnouncement extends RecyclerView.Adapter<RecyclerAdapterAnnouncement.AnnouncemenetHolder> {
    private Context context;
    private Map<Long, AnnouncementClass> announcementClassMap;

    public RecyclerAdapterAnnouncement(Map<Long, AnnouncementClass> announcementClassMap, Context context) {
        this.announcementClassMap = announcementClassMap;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncemenetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_announcement_item, parent, false);
        AnnouncemenetHolder holder=new AnnouncemenetHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(parent.getContext());

                dialog.setContentView(R.layout.announcement_dialog_resource_file);
                ImageButton closeBtn=dialog.findViewById(R.id.ann_dialog_close_button);
                System.out.println(closeBtn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView header=dialog.findViewById(R.id.ann_dialog_header);
                TextView description=dialog.findViewById(R.id.ann_dialog_description);
                header.setText(holder.textView.getText().toString());
                if (holder.description!=null){
                    description.setText(holder.description);
                }
                dialog.show();
            }
        });
        return holder;
    }

    private int num = 0;

    @Override
    public void onBindViewHolder(@NonNull AnnouncemenetHolder holder, int position) {
        if (announcementClassMap.size() > 0) {
            if (num == 4) {
                num = 1;
            } else num -= -1;
            switch (num) {
                case 1:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel1card);
                    break;
                case 2:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel2card);
                    break;
                case 3:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel3card);
                    break;
            }
            holder.textView.setText(announcementClassMap.get(Long.parseLong(position+"")).getTitle());
            holder.description=announcementClassMap.get(Long.parseLong(position+"")).getDescription();
        }
    }


    @Override
    public int getItemCount() {
        return announcementClassMap.size();
    }

    class AnnouncemenetHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        String description;

        public AnnouncemenetHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.announcement_linear_layout);
            textView = itemView.findViewById(R.id.announcement_desc_text);
        }
    }
}
