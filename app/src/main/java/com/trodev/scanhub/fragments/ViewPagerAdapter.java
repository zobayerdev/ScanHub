package com.trodev.scanhub.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.trodev.scanhub.fragments.ProductQrFragment;
import com.trodev.scanhub.fragments.SmsQrFragment;
import com.trodev.scanhub.fragments.WifiQrFragment;

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
        } else if (position == 3) {
            return new EmailFragment();
        } else if (position == 4) {
            return new LocationFragment();
        } else {
            return new UrlFragment();
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Product\nHistory";
        } else if (position == 1) {
            return "Sms\nHistory";
        } else if (position == 2) {
            return "Wifi\nHistory";
        } else if (position == 3) {
            return "Email\nHistory";
        } else if (position == 4) {
            return "Location\nHistory";
        } else {
            return "URL\nHistory";
        }

    }
}
