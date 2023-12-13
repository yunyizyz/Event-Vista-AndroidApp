package com.yunyizapp.ticketmastersearch;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
//    All the code partly take a reference from @ChatGPT.
public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    VPAdapter vpAdapter;

//    All the code partly take a reference from @ChatGPT.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#4CAF50'>EventFinder</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1a1a1a")));

        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewpager);
        vpAdapter = new VPAdapter(this);
        viewPager2.setAdapter(vpAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

                FrameLayout searchResultLayout = findViewById(R.id.search_result_layout);
                // Hide the search layout if the Favorites tab is selected
                if (tab.getPosition() == 1) {
                    if (searchResultLayout != null) {
                        // The view is not null, so it is safe to access and manipulate it
                        searchResultLayout.setVisibility(View.GONE);
                    }
                }
                // Show the search layout if the Search tab is selected
                else if (tab.getPosition() == 0) {
                    if (searchResultLayout != null) {
                        // The view is not null, so it is safe to access and manipulate it
                        searchResultLayout.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


    }
}