package com.example.android.billsplittingapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by praty on 04-06-2018.
 */

public class FragmentsAdapter extends FragmentPagerAdapter {
    private Context context;
    public FragmentsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return  new FragmentOne();
        }
        else if(position==1){
            return new FragmentActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.groups);
            case 1:
                return context.getString(R.string.activities);
            default:
                return null;
        }
    }
}
