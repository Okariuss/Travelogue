package com.okada.travelogue.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.okada.travelogue.Activity.BookingActivity;
import com.okada.travelogue.HelperClasses.HelperClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

import java.util.ArrayList;


public class RecyclerAdapterSeat extends RecyclerView.Adapter<RecyclerAdapterSeat.SeatHolder> {
    private int passengerCount;
    private int selectedCount;
    private ArrayList<String> busySeatsArrayList;
    private Button selectButton;
    private String[] selectedSeats;
    private FirebaseFirestore firestore;
    private String currentFlightId;
    private ArrayList<String> currentSelectedSeats;
    private ComponentActivity as;
    private boolean bool;

    public RecyclerAdapterSeat(int passengerCount, ArrayList<String> busySeatsArrayList,
                               Button selectButton, String[] selectedSeats, String currentFlightId,
                                ComponentActivity as) {
        this.passengerCount = passengerCount;
        selectedCount = 0;
        this.busySeatsArrayList = busySeatsArrayList;
        this.selectButton = selectButton;
        this.selectedSeats = selectedSeats;
        this.currentFlightId = currentFlightId;
        currentSelectedSeats = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        this.as=as;
        bool=true;
        for (int i = 0; i < selectedSeats.length; i++) {
           busySeatsArrayList.remove(selectedSeats[i]);
        }
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
    }

    public void select() {
        if (selectedCount != passengerCount) {
            Toast.makeText(selectButton.getContext(), LanguageResourceClass.getResource(as.getBaseContext()).getString(R.string.seat_choose) + " " + passengerCount, Toast.LENGTH_SHORT).show();
        } else {
            selectedSeats = new String[passengerCount];
            String busySeats = "";
            for (int i = 0; i < busySeatsArrayList.size(); i++) {
                busySeats += busySeatsArrayList.get(i) + ",";
            }
            for (int i = 0; i < currentSelectedSeats.size(); i++) {
                if (i != currentSelectedSeats.size() - 1) {
                    busySeats += currentSelectedSeats.get(i) + ",";
                } else busySeats += currentSelectedSeats.get(i);
                selectedSeats[i]=currentSelectedSeats.get(i);

            }
            HelperClass.setSelectedSeats(selectedSeats);
            HelperClass.setBusySeats(busySeats);
            HelperClass.setIsTrue(true);
            firestore.collection("Flights").document(currentFlightId).update("busySeats", busySeats);
            as.onBackPressed();
            as.finish();

        }
    }

    @NonNull
    @Override
    public SeatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_seat_item, parent, false);
        return new SeatHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull SeatHolder holder, int position) {

        holder.seatA.setText("A" + (position + 1));
        holder.seatB.setText("B" + (position + 1));
        holder.seatC.setText("C" + (position + 1));
        holder.seatD.setText("D" + (position + 1));
        holder.seatE.setText("E" + (position + 1));
        holder.seatF.setText("F" + (position + 1));
        holder.seatNum.setText((position + 1) + "");

        if (position % 5 == 0) {
            LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams) holder.seatLineLayout.getLayoutParams();
            layoutparams.setMargins(0, 100, 0, 0);
            holder.seatLineLayout.setLayoutParams(layoutparams);
            holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
            holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
            holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
            holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
            holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
            holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
        } else {
            LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams) holder.seatLineLayout.getLayoutParams();
            layoutparams.setMargins(0, 0, 0, 0);
            holder.seatLineLayout.setLayoutParams(layoutparams);
            holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
            holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
            holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
            holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
            holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
            holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
        }

        for (int i = 0; i < busySeatsArrayList.size(); i++) {
            if (holder.seatA.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatA.setHint("red");
            }
            if (holder.seatB.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatB.setHint("red");
            }
            if (holder.seatC.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatC.setHint("red");
            }
            if (holder.seatD.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatD.setHint("red");
            }
            if (holder.seatE.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatE.setHint("red");
            }
            if (holder.seatF.getText().toString().equals(busySeatsArrayList.get(i))) {
                holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_red, null));
                holder.seatF.setHint("red");
            }
        }

        if (selectedSeats[0]!=null && !HelperClass.isIsTrue()){
            for (int i = 0; i < selectedSeats.length; i++) {
                if (holder.seatA.getText().toString().equals(selectedSeats[i])) {
                    holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatA.getText().toString());
                    holder.seatA.setHint("green");
                }
                if (holder.seatB.getText().toString().equals(selectedSeats[i])) {
                    holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatB.getText().toString());
                    holder.seatB.setHint("green"); }
                if (holder.seatC.getText().toString().equals(selectedSeats[i])) {
                    holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatC.getText().toString());
                    holder.seatC.setHint("green"); }
                if (holder.seatD.getText().toString().equals(selectedSeats[i])) {
                    holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatD.getText().toString());
                    holder.seatD.setHint("green");  }
                if (holder.seatE.getText().toString().equals(selectedSeats[i])) {
                    holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatE.getText().toString());
                    holder.seatE.setHint("green");}
                if (holder.seatF.getText().toString().equals(selectedSeats[i])) {
                    holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                    selectedCount++;
                    currentSelectedSeats.add(holder.seatF.getText().toString());
                    holder.seatF.setHint("green");  }
            }
        }
        holder.seatA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.seatA.getHint() != "green" && holder.seatA.getHint() != "red" && bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatA.setHint("green");
                        holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        currentSelectedSeats.add(holder.seatA.getText().toString());
                        selectedCount++;
                    }
                } else if (holder.seatA.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatA.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatA.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatA.getText().toString());
                }
            }
        });
        holder.seatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seatB.getHint() != "green" && holder.seatB.getHint() != "red"&& bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatB.setHint("green");
                        holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        selectedCount++;
                        currentSelectedSeats.add(holder.seatB.getText().toString());
                    }
                } else if (holder.seatB.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatB.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatB.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatB.getText().toString());
                }
            }
        });
        holder.seatC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seatC.getHint() != "green" && holder.seatC.getHint() != "red"&& bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatC.setHint("green");
                        holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        selectedCount++;
                        currentSelectedSeats.add(holder.seatC.getText().toString());
                    }
                } else if (holder.seatC.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatC.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatC.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatC.getText().toString());
                }
            }
        });
        holder.seatD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seatD.getHint() != "green" && holder.seatD.getHint() != "red"&& bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatD.setHint("green");
                        holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        selectedCount++;
                        currentSelectedSeats.add(holder.seatD.getText().toString());
                    }
                } else if (holder.seatD.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatD.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatD.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatD.getText().toString());
                }
            }
        });
        holder.seatE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seatE.getHint() != "green" && holder.seatE.getHint() != "red"&& bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatE.setHint("green");
                        holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        selectedCount++;
                        currentSelectedSeats.add(holder.seatE.getText().toString());
                    }
                } else if (holder.seatE.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatE.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatE.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatE.getText().toString());
                }
            }
        });
        holder.seatF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seatF.getHint() != "green" && holder.seatF.getHint() != "red"&& bool) {
                    if (canSelect(holder.seatA.getContext())) {
                        holder.seatF.setHint("green");
                        holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_green, null));
                        selectedCount++;
                        currentSelectedSeats.add(holder.seatF.getText().toString());
                    }
                } else if (holder.seatF.getHint() != "red"&& bool) {
                    if (position % 5 == 0) {
                        holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res_blue, null));
                    } else {
                        holder.seatF.setBackground(holder.itemView.getResources().getDrawable(R.drawable.border_res, null));
                    }
                    holder.seatF.setHint("");
                    selectedCount--;
                    currentSelectedSeats.remove(holder.seatF.getText().toString());
                }
            }
        });
    }

    public boolean canSelect(Context context) {
        if (selectedCount == passengerCount) {
            Toast.makeText(context, LanguageResourceClass.getResource(as.getBaseContext()).getString(R.string.seat_count), Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class SeatHolder extends RecyclerView.ViewHolder {
        TextView seatA, seatB, seatC, seatD, seatE, seatF, seatNum;
        LinearLayout seatLineLayout;

        public SeatHolder(@NonNull View itemView) {
            super(itemView);
            seatA = itemView.findViewById(R.id.seat_a);
            seatB = itemView.findViewById(R.id.seat_b);
            seatC = itemView.findViewById(R.id.seat_c);
            seatD = itemView.findViewById(R.id.seat_d);
            seatE = itemView.findViewById(R.id.seat_e);
            seatF = itemView.findViewById(R.id.seat_f);
            seatNum = itemView.findViewById(R.id.seat_num);
            seatLineLayout = itemView.findViewById(R.id.seat_line_layout);
        }
    }
}
