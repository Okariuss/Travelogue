package com.okada.travelogue.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.HelperClasses.CompanyClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Map;


public class RecyclerAdapterCompany extends RecyclerView.Adapter<RecyclerAdapterCompany.CompanyHolder> {
    private Context context;
    private Map<Long, CompanyClass> companyClassMap;

    public RecyclerAdapterCompany(Map<Long, CompanyClass> companyClassMap, Context context) {
        this.context = context;
        this.companyClassMap = companyClassMap;
    }

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_company_item, parent, false);
        CompanyHolder companyHolder = new CompanyHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyHolder.wikipediaUrl != null) {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(companyHolder.wikipediaUrl));
                    context.startActivity(intent);
                }

            }
        });
        return companyHolder;
    }

    public static int numOfComp = 0;

    @Override
    public void onBindViewHolder(@NonNull CompanyHolder holder, int position) {
        if (companyClassMap.size() > 0) {
            if (numOfComp == 6)
                numOfComp = 1;
            else
                numOfComp -= -1;
            switch (numOfComp) {
                case 1:
                    holder.companyCardView.setCardBackgroundColor(Color.parseColor("#cae7e3"));
                    break;
                case 2:
                    holder.companyCardView.setCardBackgroundColor(Color.parseColor("#f39199"));
                    break;
                case 3:
                    holder.companyCardView.setCardBackgroundColor(Color.parseColor("#ecffec"));
                    break;
                case 4:
                    holder.companyCardView.setCardBackgroundColor(Color.parseColor("#f2eda7"));
                    break;
                case 5:
                    holder.companyCardView.setCardBackgroundColor(Color.parseColor("#a2b09f"));
                    break;
            }
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.wikipediaUrl = companyClassMap.get(Long.parseLong(position + "")).getWikipediaUrl();
            Picasso.get().load(companyClassMap.get(Long.parseLong(position + "")).getImageUrl()).into(holder.companyLogo, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onError(Exception e) {

                }
            });


            holder.companyName.setText(companyClassMap.get(Long.parseLong(position + "")).getName());
            holder.companyDesc.setText(companyClassMap.get(Long.parseLong(position + "")).getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return companyClassMap.size();
    }

    class CompanyHolder extends RecyclerView.ViewHolder {

        ImageView companyLogo;
        TextView companyName, companyDesc;
        CardView companyCardView;
        String wikipediaUrl;
        ProgressBar progressBar;
        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.company_image);
            companyName = itemView.findViewById(R.id.company_item_name);
            companyDesc = itemView.findViewById(R.id.company_item_desc);
            companyCardView = itemView.findViewById(R.id.company_card_view);
            progressBar=itemView.findViewById(R.id.recycler_company_progress);
        }
    }
}
