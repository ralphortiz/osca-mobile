package com.example.oscaandroiddev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Home extends AppCompatActivity implements FetchDetails {

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new AuthorizeFragment();
    final Fragment fragment3 = new HistoryFragment();
    final Fragment fragment4 = new ReportFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    BottomNavigationView bottomNav;
    String oscaID, password, picture, fullName, birthDate, sex, membershipDate, contactNumber, address, nfc, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottomNav);

        if (getIntent().getExtras() != null) {
            oscaID = getIntent().getStringExtra("EXTRA_OSCA_ID");
            password = getIntent().getStringExtra("EXTRA_PASSWORD");
            picture = getIntent().getStringExtra("EXTRA_PICTURE");
            fullName = getIntent().getStringExtra("EXTRA_FULL_NAME");
            birthDate = getIntent().getStringExtra("EXTRA_BIRTH_DATE");
            sex = getIntent().getStringExtra("EXTRA_SEX");
            membershipDate = getIntent().getStringExtra("EXTRA_MEMBERSHIP_DATE");
            contactNumber = getIntent().getStringExtra("EXTRA_CONTACT_NUMBER");
            address = getIntent().getStringExtra("EXTRA_ADDRESS");
            nfc = getIntent().getStringExtra("EXTRA_NFC");
            account = getIntent().getStringExtra("EXTRA_ACCOUNT");
//            Toast.makeText(Home.this, password, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Empty Values", Toast.LENGTH_SHORT).show();
        }

        bottomNav.setOnNavigationItemSelectedListener(navListener2);
        fm.beginTransaction().add(R.id.fragmentContainer, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener2 =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;
                        case R.id.nav_authorize:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;
                        case R.id.nav_history:
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;
                        case R.id.nav_report:
                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getOscaID() {
        return oscaID;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPicture() {
        return picture;
    }

    @Override
    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public String getMembershipDate() {
        return membershipDate;
    }

    @Override
    public String getContactNumber() {
        return contactNumber;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getNFC() { return nfc; }

    @Override
    public String getAccount() { return account; }
}