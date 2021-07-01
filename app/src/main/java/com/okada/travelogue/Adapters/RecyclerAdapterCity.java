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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.okada.travelogue.HelperClasses.CityClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Map;


public class RecyclerAdapterCity extends RecyclerView.Adapter<RecyclerAdapterCity.CityHolder> {
    Map<Long, CityClass> cityClassMap;
    Context context;

    public RecyclerAdapterCity(Map<Long, CityClass> cityClassMap, Context context) {
        this.context = context;
        this.cityClassMap = cityClassMap;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycyler_city_item, parent, false);
        CityHolder cityHolder= new CityHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityHolder.wikipediaUrl!=null){
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(cityHolder.wikipediaUrl));
                    context.startActivity(intent);
                }
            }
        });
        return cityHolder;
    }

    static int a = 0;

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        if (a == 6) {
            a = 1;
        } else a++;

        if (cityClassMap.size() > 0) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(cityClassMap.get(Long.parseLong(position+"")).getImageUrl()).into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
            holder.ratingBar.setRating(cityClassMap.get(Long.parseLong(position + "")).getRate());
            holder.textName.setText(cityClassMap.get(Long.parseLong(position + "")).getName());
            holder.textDesc.setText(cityClassMap.get(Long.parseLong(position + "")).getDescription());
            holder.wikipediaUrl=cityClassMap.get(Long.parseLong(position+"")).getWikipediaUrl();
            switch (a) {
                case 1:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#caf7e3"));
                    break;
                case 2:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#f39189"));
                    break;
                case 3:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#edffec"));
                    break;
                case 4:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#f2edd7"));
                    break;
                case 5:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#a2b29f"));
                    break;
            }
        }

    }




    @Override
    public int getItemCount() {
        return cityClassMap.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RatingBar ratingBar;
        TextView textName, textDesc;
        CardView cityCardView;
        String wikipediaUrl;
        ProgressBar progressBar;
        public CityHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_city_image);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);
            textName = itemView.findViewById(R.id.item_city_name);
            textDesc = itemView.findViewById(R.id.item_city_desc);
            cityCardView = itemView.findViewById(R.id.item_city_card_view);
            progressBar=itemView.findViewById(R.id.recycler_city_progress);


        }
    }
}
