package com.example.oscaandroiddev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class HistoryFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        String transportation = getResources().getString(R.string.transportation);
        String food = getResources().getString(R.string.food);
        String pharmacy = getResources().getString(R.string.pharmacy);

        tabLayout.addTab(tabLayout.newTab().setText(transportation));
        tabLayout.addTab(tabLayout.newTab().setText(food));
        tabLayout.addTab(tabLayout.newTab().setText(pharmacy));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabAdapter tabAdapter = new TabAdapter(getContext(), getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);
        int limit = (tabAdapter.getCount() > 1 ? tabAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
