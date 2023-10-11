package com.trodev.scanhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ProductQrFragment();
        } else if (position == 1) {
            return new SmsQrFragment();
        } else if (position == 2) {
            return new WifiQrFragment();
        } else{
            return new ProductQrFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Product History";
        } else if (position == 1) {
            return "Sms\nHistory";
        } else if (position == 2) {
            return "Wifi\nHistory";
        }  else {
            return "১০";
        }

    }
}
