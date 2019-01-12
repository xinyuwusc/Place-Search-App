package com.example.myfirstapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.myfirstapp.Favorite;
import com.example.myfirstapp.Results;

public class PagerAdapterMain extends FragmentStatePagerAdapter {
    int NoOfTabs;
    public PagerAdapterMain(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Results resultstab = new Results();
                return resultstab;
            case 1:
                Favorite favoritetab = new Favorite();
                return favoritetab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
