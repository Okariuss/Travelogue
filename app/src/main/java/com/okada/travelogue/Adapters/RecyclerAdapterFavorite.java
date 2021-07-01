package com.okada.travelogue.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.okada.travelogue.Activity.BookingActivity;
import com.okada.travelogue.HelperClasses.FlightClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerAdapterFavorite extends RecyclerView.Adapter<RecyclerAdapterFavorite.FavoriteHolder> {
    private ArrayList<FlightClass> flights;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String > passengerCountList;
    private Activity activity;
    public RecyclerAdapterFavorite(ArrayList<FlightClass> flights, ArrayList<String >passengerCountList, Activity activity) {
        this.flights = flights;
        firebaseAuth=FirebaseAuth.getInstance();
        this.passengerCountList=passengerCountList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ticket_item, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {

        holder.nothing.setText("red");
        holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart_red, null));

        holder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart, null));
                        holder.nothing.setText("black");
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(firebaseAuth.getCurrentUser().getUid() + "")
                                .child("favourites")
                                .child(flights.get(position).getFlightId())
                                .removeValue();

            }
        });
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(flights.get(position).getImageUrl()).into(holder.logo, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(flights.get(position).getTimeTakeOff().getSeconds() * 1000);
        int hourTakeOff = cal.get(Calendar.HOUR_OF_DAY) + 4;
        int minutesTakeOff = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis(flights.get(position).getTimeLanding().getSeconds() * 1000);
        int hourLanding = cal.get(Calendar.HOUR_OF_DAY) + 4;
        int minutesLanding = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis((flights.get(position).getTimeLanding().getSeconds() - flights.get(position).getTimeTakeOff().getSeconds()) * 1000);
        int hourTotal = cal.get(Calendar.HOUR_OF_DAY);
        int minutesTotal = cal.get(Calendar.MINUTE);
        if (minutesTakeOff != 0)
            holder.startTime.setText(hourTakeOff + ":" + minutesTakeOff);
        else
            holder.startTime.setText(hourTakeOff + ":00");
        if (minutesLanding != 0)
            holder.endTime.setText(hourLanding + ":" + minutesLanding);
        else
            holder.endTime.setText(hourLanding + ":00");
        if (LanguageResourceClass.isEnglish(holder.endTime.getContext())) {
            if (minutesTotal != 0) {
                holder.totalTime.setText(hourTotal + "h " + minutesTotal + "min");
            } else {
                holder.totalTime.setText(hourTotal + "h");
            }
            holder.price.setText("Price: " + flights.get(position).getPrice() + "$");
        } else {
            if (minutesTotal != 0) {
                holder.totalTime.setText(hourTotal + "s " + minutesTotal + "dk");
            } else {
                holder.totalTime.setText(hourTotal + "s");
            }
            holder.price.setText("Fiyat: " + flights.get(position).getPrice() + "$");
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.relativeLayout.getContext(), BookingActivity.class);
                intent.putExtra("ticketId", flights.get(position).getFlightId());
                intent.putExtra("price", flights.get(position).getPrice());
                intent.putExtra("hourTakeOff", hourTakeOff);
                intent.putExtra("minutesTakeOff", minutesTakeOff);
                intent.putExtra("hourLanding", hourLanding);
                intent.putExtra("minutesLanding", minutesLanding);
                intent.putExtra("hourTotal", hourTotal);
                intent.putExtra("minutesTotal", minutesTotal);
                intent.putExtra("imageUrl", flights.get(position).getImageUrl());
                intent.putExtra("passengerCount", Integer.parseInt(passengerCountList.get(position)));

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(holder.ticketLogoLayout, "ticketLogoLayout");
                pairs[1] = new Pair<View, String>(holder.price, "ticketPrice");
                pairs[2] = new Pair<View, String>(holder.relativeLayout, "ticketItemLayout");
                pairs[3] = new Pair<View, String>(holder.ticketRoadImage, "roadImage");
                pairs[4] = new Pair<View, String>(holder.startTime, "startTime");
                pairs[5] = new Pair<View, String>(holder.endTime, "endTime");
                pairs[6] = new Pair<View, String>(holder.totalTime, "totalTime");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                holder.relativeLayout.getContext().startActivity(intent, activityOptions.toBundle());

            }
        });

    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        ImageView logo, ticketRoadImage;
        TextView price, startTime, endTime, totalTime, nothing;
        ImageButton heartButton;
        ProgressBar progressBar;
        RelativeLayout relativeLayout, ticketLogoLayout;

        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.ticket_logo);
            price = itemView.findViewById(R.id.ticket_price);
            startTime = itemView.findViewById(R.id.ticket_start_time);
            endTime = itemView.findViewById(R.id.ticket_end_time);
            totalTime = itemView.findViewById(R.id.ticket_total_time);
            heartButton = itemView.findViewById(R.id.ticket_heart_button);
            progressBar = itemView.findViewById(R.id.ticket_progress);
            relativeLayout = itemView.findViewById(R.id.ticket_item_layout);
            nothing = itemView.findViewById(R.id.nothing);

            ticketLogoLayout = itemView.findViewById(R.id.ticket_logo_layout);
            ticketRoadImage = itemView.findViewById(R.id.ticket_road_image); }

    }
}
