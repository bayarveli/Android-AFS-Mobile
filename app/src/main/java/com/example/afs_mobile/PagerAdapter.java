package com.example.afs_mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        Fragment fragment = new FragmentTab();
        Bundle args = new Bundle();


        args.putInt(FragmentTab.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return AppParamConfig.SYSTEM_POND_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "GÖREV " + (position);
    }


}
