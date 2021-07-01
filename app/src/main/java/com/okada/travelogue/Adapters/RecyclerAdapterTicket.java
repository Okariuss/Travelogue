package com.okada.travelogue.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.okada.travelogue.Activity.BookingActivity;
import com.okada.travelogue.Activity.SignUp1Activity;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.FlightClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerAdapterTicket extends RecyclerView.Adapter<RecyclerAdapterTicket.TicketHolder> {
    private ArrayList<FlightClass> tickets;
    private int passengerCount;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String > favouritesList;
    private Activity activity;
    public RecyclerAdapterTicket(ArrayList<FlightClass> tickets, int passengerCount, ArrayList<String > favouritesList, Activity activity) {
        this.tickets = tickets;
        this.passengerCount = passengerCount;
        firebaseAuth = FirebaseAuth.getInstance();
        this.favouritesList=favouritesList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_ticket_item, parent, false);
        return new TicketHolder(view);
    }

    @SuppressLint({"SetTextI18n","UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        if (favouritesList.size()>0){
            if (favouritesList.contains(tickets.get(position).getFlightId())){
                holder.nothing.setText("red");
                holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart_red, null));
            }else {
                holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart, null));
                holder.nothing.setText("black");
            }
        }
        holder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (holder.nothing.getText().toString().equals("black")) {
                        holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart_red, null));
                        holder.nothing.setText("red");
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(firebaseAuth.getCurrentUser().getUid() + "")
                                .child("favourites").child(tickets.get(position)
                                .getFlightId()).setValue(passengerCount);

                    } else {
                        holder.heartButton.setImageDrawable(holder.heartButton.getResources().getDrawable(R.drawable.heart, null));
                        holder.nothing.setText("black");

                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(firebaseAuth.getCurrentUser().getUid() + "")
                                .child("favourites")
                                .child(tickets.get(position).getFlightId())
                                .removeValue();

                    }


                }else {
                    Toast.makeText(holder.endTime.getContext(),LanguageResourceClass.getResource(v.getContext()).getString(R.string.sign_favorite),Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(tickets.get(position).getImageUrl()).into(holder.logo, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(tickets.get(position).getTimeTakeOff().getSeconds() * 1000);
        int hourTakeOff = cal.get(Calendar.HOUR_OF_DAY) + 4;
        int minutesTakeOff = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis(tickets.get(position).getTimeLanding().getSeconds() * 1000);
        int hourLanding = cal.get(Calendar.HOUR_OF_DAY) + 4;
        int minutesLanding = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis((tickets.get(position).getTimeLanding().getSeconds() - tickets.get(position).getTimeTakeOff().getSeconds()) * 1000);
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
            holder.price.setText("Price: " + tickets.get(position).getPrice() + "$");
        } else {
            if (minutesTotal != 0) {
                holder.totalTime.setText(hourTotal + "s " + minutesTotal + "dk");
            } else {
                holder.totalTime.setText(hourTotal + "s");
            }
            holder.price.setText("Fiyat: " + tickets.get(position).getPrice() + "$");
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.relativeLayout.getContext(), BookingActivity.class);
                intent.putExtra("ticketId", tickets.get(position).getFlightId());
                intent.putExtra("price", tickets.get(position).getPrice());
                intent.putExtra("hourTakeOff", hourTakeOff);
                intent.putExtra("minutesTakeOff", minutesTakeOff);
                intent.putExtra("hourLanding", hourLanding);
                intent.putExtra("minutesLanding", minutesLanding);
                intent.putExtra("hourTotal", hourTotal);
                intent.putExtra("minutesTotal", minutesTotal);
                intent.putExtra("imageUrl", tickets.get(position).getImageUrl());
                intent.putExtra("passengerCount", passengerCount);
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
        return tickets.size();
    }

    class TicketHolder extends RecyclerView.ViewHolder {
        ImageView logo, ticketRoadImage;
        TextView price, startTime, endTime, totalTime, nothing;
        ImageButton heartButton;
        ProgressBar progressBar;
        RelativeLayout relativeLayout, ticketLogoLayout;


        public TicketHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.ticket_logo);
            price = itemView.findViewById(R.id.ticket_price);
            startTime = itemView.findViewById(R.id.ticket_start_time);
            endTime = itemView.findViewById(R.id.ticket_end_time);
            totalTime = itemView.findViewById(R.id.ticket_total_time);
            heartButton = itemView.findViewById(R.id.ticket_heart_button);
            progressBar = itemView.findViewById(R.id.ticket_progress);
            relativeLayout = itemView.findViewById(R.id.ticket_item_layout);
            ticketLogoLayout = itemView.findViewById(R.id.ticket_logo_layout);
            ticketRoadImage = itemView.findViewById(R.id.ticket_road_image);
            nothing = itemView.findViewById(R.id.nothing);
        }
    }
}
