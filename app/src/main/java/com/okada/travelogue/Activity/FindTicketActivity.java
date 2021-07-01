package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.okada.travelogue.Fragment.FindBusTicketFragment;
import com.okada.travelogue.Fragment.FindPlaneTicketFragment;
import com.okada.travelogue.Fragment.FindShipTicketFragment;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class FindTicketActivity extends AppCompatActivity {
    ChipNavigationBar chipNav;
    TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ticket);
        initialization();
        addListener();
        getSupportFragmentManager().beginTransaction().add(R.id.find_ticket_layout,new FindPlaneTicketFragment()).commit();
        chipNav.setItemSelected(R.id.menu_plane_item,true);
        checkLanguage();
    }

    private void checkLanguage() {

        if (LanguageResourceClass.isEnglish(this)) {
            header.setText("Find Your Ticket");
            chipNav.setMenuResource(R.menu.menu_chip_navigation);
            chipNav.setItemSelected(R.id.menu_plane_item,true);
        }else {
            header.setText("Biletinizi Bulun");
            chipNav.setMenuResource(R.menu.menu_chip_navigation_turkish);
            chipNav.setItemSelected(R.id.menu_plane_item,true);
        }
    }

    private void addListener() {
        chipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                if (i==R.id.menu_plane_item){
                    fragment=new FindPlaneTicketFragment();
                }else if (i==R.id.menu_bus_item){
                    fragment=new FindBusTicketFragment();
                }else {
                    fragment=new FindShipTicketFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.find_ticket_layout,fragment).commit();
            }
        });
    }

    private void initialization() {
        chipNav=findViewById(R.id.chip_navigation);
        header = findViewById(R.id.settings_header);
    }

    public void goBack(View view){
        onBackPressed();
    }

}