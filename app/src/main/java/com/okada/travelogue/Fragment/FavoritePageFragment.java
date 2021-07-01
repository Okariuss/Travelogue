package com.okada.travelogue.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.Activity.HomeActivity;
import com.okada.travelogue.Activity.SignInStartActivity;
import com.okada.travelogue.Adapters.RecyclerAdapterFavorite;
import com.okada.travelogue.HelperClasses.FlightClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

import java.util.ArrayList;
import java.util.Map;


public class FavoritePageFragment extends Fragment {
    private Button btnToSignIn;
    private LayoutInflater layoutInflater;
    private View view;
    private ViewGroup viewContainer;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> favoriteFlightIdList,passengerCountList;
    private ArrayList<FlightClass> favoriteFlights;
    private RecyclerAdapterFavorite adapterFavorite;
    private RecyclerView recyclerView;
    private TextView pleaseLoginTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        viewContainer = container;
        firebaseAuth = FirebaseAuth.getInstance();
        getData();
        return view;
    }

    public void checkLanguage() {
        if (getActivity() != null) {
            Resources resources = LanguageResourceClass.getResource(getActivity());
            btnToSignIn.setText(resources.getString(R.string.sign_in));
            pleaseLoginTextView.setText(resources.getString(R.string.please_login_to_view));
        }
    }

    private void getData() {
        if (firebaseAuth.getCurrentUser() != null) {
            view = layoutInflater.inflate(R.layout.fragment_favorite_page, viewContainer, false);
            favoriteFlights=new ArrayList<>();
            favoriteFlightIdList=new ArrayList<>();
            passengerCountList=new ArrayList<>();
            recyclerView=view.findViewById(R.id.recycler_favorite);
            adapterFavorite=new RecyclerAdapterFavorite(favoriteFlights,passengerCountList,getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapterFavorite);
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteFlightIdList.clear();
                    for (DataSnapshot ds : snapshot.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("favourites").getChildren()) {
                        favoriteFlightIdList.add(ds.getKey());
                        passengerCountList.add(ds.getValue().toString());
                    }
                    getFlights();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view = layoutInflater.inflate(R.layout.sign_in_for_vindow_layout, viewContainer, false);
            initialization1();
            checkLanguage();
            btnToSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toSignInStartActivity = new Intent(getActivity(), SignInStartActivity.class);
                    startActivity(toSignInStartActivity);
                    Animatoo.animateInAndOut(getActivity());
                }
            });
        }

    }

    private void getFlights() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Flights");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    favoriteFlights.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        if (favoriteFlightIdList.contains(map.get("flightId"))){
                            String imageUrl=(String) map.get("image");
                            Timestamp timeLanding=(Timestamp) map.get("timelanding");
                            Timestamp timeTakeOff=(Timestamp) map.get("timetakeoff");
                            String from=(String) map.get("from");
                            String to=(String) map.get("to");
                            String vehicle=(String) map.get("vehicle");
                            String companyName=(String) map.get("companyName");
                            long price=(long) map.get("price");
                            String detail=(String) map.get("detail");
                            String flightId=(String) map.get("flightId");
                            favoriteFlights.add(new FlightClass(imageUrl,from,to,vehicle,timeTakeOff,timeLanding,Integer.parseInt(price+""),companyName,detail,flightId));
                        }
                    }
                    adapterFavorite.notifyDataSetChanged();
                }
            }
        });
    }

    public void initialization1() {
        btnToSignIn = view.findViewById(R.id.please_login_sign_in);
        pleaseLoginTextView = view.findViewById(R.id.please_login_text_view);

    }
}