package com.yunyizapp.ticketmastersearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class VPAdapter2 extends FragmentStateAdapter {

    private Bundle bundle;

    public VPAdapter2(@NonNull FragmentActivity fragmentActivity, Bundle bundle) {
        super(fragmentActivity);
        this.bundle = bundle;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                DetailInfoFragment infoFragment = new DetailInfoFragment();
                infoFragment.setArguments(bundle);
                return infoFragment;
            case 1:
                DetailArtistFragment artistFragment = new DetailArtistFragment();
                artistFragment.setArguments(bundle);
                return artistFragment;
            case 2:
                DetailVenueFragment venueFragment = new DetailVenueFragment();
                venueFragment.setArguments(bundle);
                return venueFragment;
            default:
                return new DetailInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
