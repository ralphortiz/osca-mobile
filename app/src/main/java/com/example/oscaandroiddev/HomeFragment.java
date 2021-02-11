package com.example.oscaandroiddev;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeFragment extends Fragment {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView sidebarNav;
    TextView btnMenu, tvBanner2;
    Button btnPDF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnMenu = view.findViewById(R.id.btnMenu);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        sidebarNav = view.findViewById(R.id.sidebarNav);
        btnPDF = view.findViewById(R.id.btnPDF);
        tvBanner2 = view.findViewById(R.id.tvBanner2);
        FetchDetails fetchDetails = (FetchDetails) getActivity();
        tvBanner2.setText("We wish you a Happy Birthday, " + fetchDetails.getFullName());
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(GravityCompat.START);
                else drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PDF.class);
                startActivity(intent);
            }
        });


        sidebarNav.bringToFront();
        drawerLayout.setDrawerElevation(0);
        sidebarNav.setNavigationItemSelectedListener(navListener);
        return view;
    }

    private NavigationView.OnNavigationItemSelectedListener navListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            openProfile();
//                            Toast.makeText(getActivity(), "Profile", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_settings:
                            openSettings();
//                            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_signout:
                            getActivity().finish();
//                            Toast.makeText(getActivity(), "Sign Out", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_about:
//                            Toast.makeText(getActivity(), "About Us", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_privacy:
//                            Toast.makeText(getActivity(), "Privacy Policy", Toast.LENGTH_LONG).show();
                            break;
                    }
                    return true;
                }
            };


    @Override
    public void onResume() {
        drawerLayout.closeDrawer(Gravity.LEFT, false);
        super.onResume();
    }

    private void openProfile() {
        FetchDetails fetchDetails = (FetchDetails) getActivity();
        String oscaID = fetchDetails.getOscaID();
        String password = fetchDetails.getPassword();
        String picture = fetchDetails.getPicture();
        String fullName = fetchDetails.getFullName();
        String birthDate = fetchDetails.getBirthDate();
        String sex = fetchDetails.getSex();
        String membershipDate = fetchDetails.getMembershipDate();
        String contactNumber = fetchDetails.getContactNumber();
        String address = fetchDetails.getAddress();
        String nfc = fetchDetails.getNFC();
        String account = fetchDetails.getAccount();
        MemberDetails memberDetails = new MemberDetails(fullName, oscaID, password, picture, sex, contactNumber, birthDate, membershipDate, address, nfc, account);
        //MemberDetails memberDetails = new MemberDetails(fullName, oscaID, picture, sex, contactNumber, birthDate, membershipDate, address);
        Intent intent = new Intent(getActivity(), Profile.class);
        intent.putExtra("EXTRA_MEMBER DETAILS", memberDetails);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(getActivity(), Settings.class);
        startActivity(intent);
    }
}
