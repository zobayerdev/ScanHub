package com.trodev.scanhub;

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
        } else {
            return new WifiQrFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Product\nHistory";
        } else if (position == 1) {
            return "Sms\nHistory";
        } else {
            return "Wifi\nHistory";
        }

    }
}
