package com.okada.travelogue.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.Activity.FindHotelActivity;
import com.okada.travelogue.Activity.FindTicketActivity;
import com.okada.travelogue.Adapters.RecyclerAdapterAnnouncement;
import com.okada.travelogue.Adapters.RecyclerAdapterCity;
import com.okada.travelogue.Adapters.RecyclerAdapterCompany;
import com.okada.travelogue.HelperClasses.AnnouncementClass;
import com.okada.travelogue.HelperClasses.CityClass;
import com.okada.travelogue.HelperClasses.CompanyClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

import java.util.HashMap;
import java.util.Map;


public class HomePageFragment extends Fragment {
    private RecyclerAdapterCity adapterCity;
    private RecyclerAdapterCompany adapterCompany;
    private RecyclerAdapterAnnouncement adapterAnnouncement;
    private MaterialCardView travelCard, hotelCard;
    private FirebaseFirestore firestore;
    private Map<Long, CityClass> cityClassMap;
    private Map<Long, CompanyClass> companyClassMap;
    private Map<Long, AnnouncementClass> announcementClassMap;
    private View view;
    private TextView homeFragTravel, homeFragHotel, homeFragCities, homeFragCitiesYouMay,
            homeFragCompanies, homeFragCompaniesWeWork, homeFragAnnouncement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initialization();
        addlistener();
        checkLanguage();
        fillCityRecycler();
        fillCompanyRecycler();
        fillAnnouncementRecycler();

        return view;
    }

    private void addlistener() {
        travelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), FindTicketActivity.class);
                    getActivity().startActivity(intent);
                    Animatoo.animateZoom(getActivity());
                }
            }
        });
        hotelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindHotelActivity.class);
                getActivity().startActivity(intent);
                Animatoo.animateZoom(getActivity());
            }
        });
    }

    private void initialization() {
        homeFragTravel = view.findViewById(R.id.home_frag_travel);
        homeFragHotel = view.findViewById(R.id.home_frag_hotel);
        homeFragCities = view.findViewById(R.id.home_frag_cities);
        homeFragCitiesYouMay = view.findViewById(R.id.home_frag_cities_you_may);
        homeFragCompanies = view.findViewById(R.id.home_frag_companies);
        homeFragCompaniesWeWork = view.findViewById(R.id.home_frag_companies_we_work);
        homeFragAnnouncement = view.findViewById(R.id.home_frag_announcement);
        travelCard = view.findViewById(R.id.travel_card_view);
        hotelCard = view.findViewById(R.id.hotel_card_view);
    }

    private void checkLanguage() {
        if (getActivity() != null) {
            Resources resources = LanguageResourceClass.getResource(getActivity());
            homeFragTravel.setText(resources.getString(R.string.travel));
            homeFragHotel.setText(resources.getString(R.string.hotel));
            homeFragCities.setText(resources.getString(R.string.cities));
            homeFragCitiesYouMay.setText(resources.getString(R.string.cities_you_may_want));
            homeFragCompanies.setText(resources.getString(R.string.compaines));
            homeFragCompaniesWeWork.setText(resources.getString(R.string.companies_we_work));
            homeFragAnnouncement.setText(resources.getString(R.string.news_announcement));
        }
    }

    private void fillCityRecycler() {
        cityClassMap = new HashMap<>();
        firestore = FirebaseFirestore.getInstance();
        RecyclerView cityRecyclerView = view.findViewById(R.id.city_recycler_view);
        adapterCity = new RecyclerAdapterCity(cityClassMap, getContext());
        getCitiesFromFirebase();
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cityRecyclerView.setAdapter(adapterCity);
    }

    private void fillCompanyRecycler() {
        companyClassMap = new HashMap<>();
        RecyclerView companyRecyclerView = view.findViewById(R.id.company_recycler_view);
        adapterCompany = new RecyclerAdapterCompany(companyClassMap, getContext());
        getCompaniesFromFirebase();
        companyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        companyRecyclerView.setAdapter(adapterCompany);
    }

    private void fillAnnouncementRecycler() {
        announcementClassMap = new HashMap<>();
        RecyclerView announcementRecyclerView = view.findViewById(R.id.announcement_recycler_view);
        adapterAnnouncement = new RecyclerAdapterAnnouncement(announcementClassMap, getContext());
        getAnnouncementsFromFirebase();
        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        announcementRecyclerView.setAdapter(adapterAnnouncement);
    }

    private void getCitiesFromFirebase() {


        if (getActivity() != null) {
            String storeName = "";
            if (LanguageResourceClass.isEnglish(getActivity())) {
                storeName = "Cities";
            } else {
                storeName = "Cities_TR";
            }
            CollectionReference collectionReference = firestore.collection(storeName);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        System.out.println(error.getLocalizedMessage());
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else if (value != null) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Map<String, Object> map = snapshot.getData();
                            String name = (String) map.get("Name");
                            String imageUrl = (String) map.get("ImageURL");
                            String description = (String) map.get("Description");
                            String rating = (String) map.get("Rating");
                            String wikipediaUrl = (String) map.get("WikipediaURL");
                            Long id = (Long) map.get("id");
                            cityClassMap.put(id - 1, new CityClass(imageUrl, Float.parseFloat(rating), name, description, wikipediaUrl));
                            adapterCity.notifyDataSetChanged();
                        }
                    }
                }
            });
        }


    }

    private void getCompaniesFromFirebase() {
        if (getActivity() != null) {
            String storeName = "";
            if (LanguageResourceClass.isEnglish(getActivity())) {
                storeName = "Companies";
            } else {
                storeName = "Companies_TR";

            }
            CollectionReference collectionReference1 = firestore.collection(storeName);
            collectionReference1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else if (value != null) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Map<String, Object> map = snapshot.getData();
                            String name = (String) map.get("Name");
                            String imageUrl = (String) map.get("ImageURL");
                            String description = (String) map.get("Description");
                            String wikipediaUrl = (String) map.get("WikipediaURL");
                            Long id = (Long) map.get("id");
                            companyClassMap.put(id, new CompanyClass(imageUrl, name, description, wikipediaUrl));
                            adapterCompany.notifyDataSetChanged();
                        }
                    }
                }
            });


        }

    }

    private void getAnnouncementsFromFirebase() {
        if (getActivity() != null) {
            String storeName = "";
            if (LanguageResourceClass.isEnglish(getActivity())) {
                storeName = "Announcements";
            } else {
                storeName = "Announcements_TR";
            }

            CollectionReference collectionReference2 = firestore.collection(storeName);
            collectionReference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else if (value != null) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Map<String, Object> map = snapshot.getData();
                            String title = (String) map.get("Title");
                            Long id = (Long) map.get("id");
                            String description = (String) map.get("Description");
                            announcementClassMap.put(id, new AnnouncementClass(title, description));
                            adapterAnnouncement.notifyDataSetChanged();
                        }
                    }
                }
            });


        }

    }
}