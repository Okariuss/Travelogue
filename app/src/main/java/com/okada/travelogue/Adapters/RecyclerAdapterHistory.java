package com.okada.travelogue.Adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.HelperClasses.FlightClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.TicketClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerAdapterHistory extends RecyclerView.Adapter<RecyclerAdapterHistory.HistoryTicketHolder> {
    private ArrayList<FlightClass> flights;
    private ArrayList<TicketClass> tickets;

    public RecyclerAdapterHistory(ArrayList<FlightClass> flights, ArrayList<TicketClass> tickets) {
        this.flights = flights;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public HistoryTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_history_item, parent, false);
        return new HistoryTicketHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull HistoryTicketHolder holder, int position) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightId().equals(tickets.get(position).getFlightId())) {
                holder.progressBar.setVisibility(View.VISIBLE);
                Picasso.get().load(flights.get(i).getImageUrl()).into(holder.logoImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                cal.setTimeInMillis(flights.get(i).getTimeLanding().getSeconds() * 1000);

                if (LanguageResourceClass.isEnglish(holder.hourText.getContext())) {
                    String year = cal.get(Calendar.YEAR) + "";
                    String month = "Date: " + (cal.get(Calendar.MONTH) + 1) + ".";
                    String day = cal.get(Calendar.DAY_OF_MONTH) + ".";
                    holder.dateText.setText(month + day + year);
                    holder.seatText.setText("Seat: " + tickets.get(position).getSeat());
                    holder.fromText.setText(flights.get(i).getFrom());
                    holder.toText.setText(flights.get(i).getTo());
                } else {
                    String year = cal.get(Calendar.YEAR) + "";
                    String month = "Tarih: " + (cal.get(Calendar.MONTH) + 1) + ".";
                    String day = cal.get(Calendar.DAY_OF_MONTH) + ".";
                    holder.dateText.setText(month + day + year);
                    holder.seatText.setText("Koltuk: " + tickets.get(position).getSeat());
                    holder.fromText.setText(flights.get(i).getFrom());
                    holder.toText.setText(flights.get(i).getTo());
                }


                cal.setTimeInMillis((flights.get(i).getTimeLanding().getSeconds() - flights.get(i).getTimeTakeOff().getSeconds()) * 1000);
                int hourTotal = cal.get(Calendar.HOUR_OF_DAY);
                int minutesTotal = cal.get(Calendar.MINUTE);
                if (LanguageResourceClass.isEnglish(holder.hourText.getContext())) {
                    if (minutesTotal != 0) {
                        holder.hourText.setText(hourTotal + "h " + minutesTotal + "min");
                    } else {
                        holder.hourText.setText(hourTotal + "h");
                    }
                    holder.priceText.setText("Price: " + flights.get(i).getPrice() + "$");
                } else {
                    if (minutesTotal != 0) {
                        holder.hourText.setText(hourTotal + "s " + minutesTotal + "dk");
                    } else {
                        holder.hourText.setText(hourTotal + "s");
                    }
                    holder.priceText.setText("Fiyat: " + flights.get(i).getPrice() + "$");
                }
                if (flights.get(i).getVehicle().equals("plane")) holder.vehicleImage
                        .setImageDrawable(holder.dateText.getResources().getDrawable(R.drawable.ic_baseline_airplanemode_active_24,null));
                else if (flights.get(i).getVehicle().equals("bus")) holder.vehicleImage
                        .setImageDrawable(holder.dateText.getResources().getDrawable(R.drawable.ic_baseline_directions_bus_24,null));
                else holder.vehicleImage.setImageDrawable(holder.dateText.getResources()
                            .getDrawable(R.drawable.ic_baseline_directions_boat_filled_24,null));

            }
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class HistoryTicketHolder extends RecyclerView.ViewHolder {
        ImageView logoImage, vehicleImage;
        TextView dateText, seatText, hourText, fromText, toText, priceText;
        ProgressBar progressBar;

        public HistoryTicketHolder(@NonNull View view) {
            super(view);
            logoImage = view.findViewById(R.id.history_item_logo);
            vehicleImage = view.findViewById(R.id.history_item_vehicle);
            dateText = view.findViewById(R.id.history_item_date);
            seatText = view.findViewById(R.id.history_item_seat);
            hourText = view.findViewById(R.id.history_item_hour);
            fromText = view.findViewById(R.id.history_item_from);
            toText = view.findViewById(R.id.history_item_to);
            priceText = view.findViewById(R.id.history_item_price);
            progressBar = view.findViewById(R.id.history_item_progress);
        }
    }
}
