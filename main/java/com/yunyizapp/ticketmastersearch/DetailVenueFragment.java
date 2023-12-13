package com.yunyizapp.ticketmastersearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//    All the code partly take a reference from @ChatGPT.
public class DetailVenueFragment extends Fragment {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_detail_venue, container, false);

        // Get the data from the bundle
        Bundle bundle = getArguments();

        if (bundle != null) {

            Log.e(TAG, "Bundle is not null - venue.");


            String eventVenue = bundle.getString("event_venue");
            String venueAddress = bundle.getString("event_address");
            String city = bundle.getString("event_city");
            String state = bundle.getString("event_state");
            String contact = bundle.getString("event_contact");
            String eventOh = bundle.getString("event_oh");
            String eventGr = bundle.getString("event_gr");
            String eventCr = bundle.getString("event_cr");
            String address = bundle.getString("event_address");

            // Set the text of the TextViews using the retrieved data
            TextView venuename = rootview.findViewById(R.id.venue_name);
            TextView vennueAddress = rootview.findViewById(R.id.venue_address);
            TextView venuecs = rootview.findViewById(R.id.venue_cs);
            TextView venuecontact = rootview.findViewById(R.id.venue_contact);
            TextView oh = rootview.findViewById(R.id.open_hours);
            TextView gr = rootview.findViewById(R.id.general_rule);
            TextView cr = rootview.findViewById(R.id.child_rule);

            // TODO:Load the map in the WebView
            webView = rootview.findViewById(R.id.map);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setVisibility(View.VISIBLE);

            loadMap(address, city, state);

            venuename.post(() -> {venuename.setSelected(true);});
            vennueAddress.post(() -> {vennueAddress.setSelected(true);});
            venuecs.post(() -> {venuecs.setSelected(true);});
            venuecontact.post(() -> {venuecontact.setSelected(true);});


            venuename.setText(eventVenue);
            vennueAddress.setText(venueAddress);
            venuecs.setText(city + ", " + state);
            venuecontact.setText(contact);
            oh.setText(eventOh);
            gr.setText(eventGr);
            cr.setText(eventCr);

        } else {
            Log.e(TAG, "Bundle in venue is null.");
        }


        LinearLayout venue2Layout = rootview.findViewById(R.id.venue2);

        venue2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                v.setLayoutParams(params);
            }
        });


        // Return the inflated layout
        return rootview;
    }




    private void loadMap(String address, String city, String state) {

        // Build the URL for the map
        String mapUrl = "https://www.google.com/maps/place/" + address + " "+ city +" "+ state;

        Log.e(TAG, "MapUrl MAP:" + mapUrl);

        // Load the map in the WebView
        webView.loadUrl(mapUrl);
    }


}