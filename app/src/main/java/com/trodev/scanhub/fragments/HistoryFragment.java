package com.trodev.scanhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.trodev.scanhub.R;
import com.trodev.scanhub.ViewPagerAdapter;

public class HistoryFragment extends Fragment {
    TabLayout tab;
    ViewPager viewPager;
    public HistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        /*init views*/
        tab = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);

        return view;

    }

}