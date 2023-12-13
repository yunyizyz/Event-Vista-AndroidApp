package com.yunyizapp.ticketmastersearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
//    All the code partly take a reference from @ChatGPT.
public class EventDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    VPAdapter2 vpAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);



        tabLayout = findViewById(R.id.tab_detail);
        viewPager2 = findViewById(R.id.viewpager2);
//        TODO:原代码vpAdapter = new VPAdapter2(this);

        Bundle bundle = new Bundle();
        // set data to bundle

        VPAdapter2 adapter = new VPAdapter2(this, bundle);
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
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

        String eventName = getIntent().getStringExtra("event_name");
        String eventUrl = getIntent().getStringExtra("event_url");
        String eventImageUrl = getIntent().getStringExtra("event_image_url");
        String eventVenue = getIntent().getStringExtra("event_venue");
        String eventDate = getIntent().getStringExtra("event_date");
        String eventTime = getIntent().getStringExtra("event_time");
        String eventGenre = getIntent().getStringExtra("event_genre");
        String[] eventArtists = getIntent().getStringArrayExtra("event_artists");
        String eventSubgenre = getIntent().getStringExtra("event_subgenre");
        String eventMaxPrice = getIntent().getStringExtra("event_max_price");
        String eventMinPrice = getIntent().getStringExtra("event_min_price");
        String eventStatus = getIntent().getStringExtra("event_status");
        String eventSeatmap = getIntent().getStringExtra("event_seatmap");
        String eventAddress = getIntent().getStringExtra("event_address");
        String eventCity = getIntent().getStringExtra("event_city");
        String eventState = getIntent().getStringExtra("event_state");
        String eventContact = getIntent().getStringExtra("event_contact");
        String eventOh = getIntent().getStringExtra("event_oh");
        String eventGr = getIntent().getStringExtra("event_gr");
        String eventCr = getIntent().getStringExtra("event_cr");

        // set all the data as arguments to the bundle
        bundle.putString("event_name", eventName);
        bundle.putString("event_url", eventUrl);
        bundle.putString("event_image_url", eventImageUrl);
        bundle.putString("event_venue", eventVenue);
        bundle.putString("event_date", eventDate);
        bundle.putString("event_time", eventTime);
        bundle.putString("event_genre", eventGenre);
        bundle.putStringArray("event_artists", eventArtists);
        bundle.putString("event_subgenre", eventSubgenre);
        bundle.putString("event_max_price", eventMaxPrice);
        bundle.putString("event_min_price", eventMinPrice);
        bundle.putString("event_status", eventStatus);
        bundle.putString("event_seatmap", eventSeatmap);
        bundle.putString("event_address", eventAddress);
        bundle.putString("event_city", eventCity);
        bundle.putString("event_state", eventState);
        bundle.putString("event_contact", eventContact);
        bundle.putString("event_oh", eventOh);
        bundle.putString("event_gr", eventGr);
        bundle.putString("event_cr", eventCr);

        DetailInfoFragment infoFragment = new DetailInfoFragment();
        infoFragment.setArguments(bundle);

        DetailArtistFragment artistFragment = new DetailArtistFragment();
        artistFragment.setArguments(bundle);

        DetailVenueFragment venueFragment = new DetailVenueFragment();
        venueFragment.setArguments(bundle);

        //TODO: ACTION BAR
        // Set custom view for ActionBar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.green_back_btn);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_action_bar);

        // Set text for the TextView in the custom ActionBar layout
        TextView titleTextView = actionBar.getCustomView().findViewById(R.id.title_textview);

        titleTextView.setText(eventName);

        titleTextView.post(() -> {titleTextView.setSelected(true);});

        actionBar.setDisplayHomeAsUpEnabled(true);

        View actionBarView = actionBar.getCustomView();
        ImageView facebookIcon = actionBarView.findViewById(R.id.facebook_icon);
        ImageView twitterIcon = actionBarView.findViewById(R.id.twitter_icon);
        ImageView detailHeart = actionBarView.findViewById(R.id.detail_heart);

        detailHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailHeart.setImageResource(R.drawable.heart_filled);
            }
        });

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + eventUrl.replace(" ", "%20")));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(intent);
            }
        });

        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Check out " + eventName + " on TicketMaster! " + eventUrl;
                String url = "https://twitter.com/intent/tweet?text=" + Uri.encode(message);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://twitter.com/intent/tweet?url=" + url.replace(" ", "%20")));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(intent);
            }
        });

    }

    private void openFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}