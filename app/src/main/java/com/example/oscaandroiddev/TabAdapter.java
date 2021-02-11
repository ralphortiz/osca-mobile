package com.example.oscaandroiddev;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    Context mContext;
    int totalTabs;
    String url1 = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/transportation.php";
    String url2 = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/food.php";
    String url3 = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/pharmacy.php";

//    String url1 = "http://192.168.1.6/arca_-master/mobile_app/transportation.php";
//    String url2 = "http://192.168.1.6/arca_-master/mobile_app/food.php";
//    String url3 = "http://192.168.1.6/arca_-master/mobile_app/pharmacy.php";

    public TabAdapter(@NonNull Context mContext, FragmentManager fm, int totalTabs) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment tabFragment1 = new TabFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("URL", url1);
                tabFragment1.setArguments(bundle1);
                return tabFragment1;
            case 1:
                TabFragment tabFragment2 = new TabFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("URL", url2);
                tabFragment2.setArguments(bundle2);
                return tabFragment2;
            case 2:
                TabFragment tabFragment3 = new TabFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putString("URL", url3);
                tabFragment3.setArguments(bundle3);
                return tabFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof TabFragment ) {
            ((TabFragment )object).updateData();
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
