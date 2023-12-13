package com.yunyizapp.ticketmastersearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//    All the code partly take a reference from @ChatGPT.
public class DetailInfoFragment extends Fragment {

    public DetailInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_detail_info, container, false);

        // Get the data from the bundle
        Bundle bundle = getArguments();

        if (bundle != null) {

            Log.e(TAG, "Bundle is not null - info.");

        String eventUrl = bundle.getString("event_url");
        String eventVenue = bundle.getString("event_venue");
        String eventDate = bundle.getString("event_date");
        String eventTime = bundle.getString("event_time");
        String eventGenre = bundle.getString("event_genre");
        String[] eventArtists = bundle.getStringArray("event_artists");
        String eventSubgenre = bundle.getString("event_subgenre");
        String eventMaxPrice = bundle.getString("event_max_price");
        String eventMinPrice = bundle.getString("event_min_price");
        String eventStatus = bundle.getString("event_status");
        String eventSeatmap = bundle.getString("event_seatmap");


        // Set the text of the TextViews using the retrieved data
        TextView artistTeams = rootview.findViewById(R.id.info_artist_teams);
        TextView venue = rootview.findViewById(R.id.info_venue);
        TextView date = rootview.findViewById(R.id.info_date);
        TextView time = rootview.findViewById(R.id.info_time);
        TextView subgenre = rootview.findViewById(R.id.info_genres);
        TextView priceRange = rootview.findViewById(R.id.info_price_range);
        TextView ticketStatus = rootview.findViewById(R.id.info_ticket_status);
        TextView buyTicketAt = rootview.findViewById(R.id.info_buy_ticket_at);
        ImageView seatMap = rootview.findViewById(R.id.seatmap_image);

        artistTeams.post(() -> {artistTeams.setSelected(true);});
        venue.post(() -> {venue.setSelected(true);});
        subgenre.post(() -> {subgenre.setSelected(true);});
        buyTicketAt.post(() -> {buyTicketAt.setSelected(true);});

            buyTicketAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = buyTicketAt.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent);
                }
            });


        Glide.with(this)
                .load(eventSeatmap)
                .placeholder(R.drawable.loading) // placeholder image while loading
                .error(R.drawable.page_not_found) // error image if loading fails
                .into(seatMap);

        artistTeams.setText(TextUtils.join(" | ", eventArtists));
        venue.setText(eventVenue);
        date.setText(eventDate);
        time.setText(eventTime);
        subgenre.setText(eventSubgenre);
        priceRange.setText(eventMinPrice + " - " + eventMaxPrice + " USD");

            if (eventStatus.equals("onsale")) {
                ticketStatus.setText("On Sale");
                ticketStatus.setBackgroundResource(R.drawable.status_on);
            } else if (eventStatus.equals("offsale")) {
                ticketStatus.setText("Off Sale");
                ticketStatus.setBackgroundResource(R.drawable.status_off);
            } else {
                ticketStatus.setText(eventStatus);
            }

        buyTicketAt.setText(eventUrl);

        } else {
            Log.e(TAG, "Bundle in info is null.");
        }

        // Return the inflated layout
        return rootview;
    }
}