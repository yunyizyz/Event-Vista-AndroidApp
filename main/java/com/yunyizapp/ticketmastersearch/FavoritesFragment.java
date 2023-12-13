package com.yunyizapp.ticketmastersearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

//    All the code partly take a reference from @ChatGPT.
public class FavoritesFragment extends Fragment {


    public static List<Event> favoritesList;
    private FavoritesViewModel viewModel;
    private View rootView;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    String fav_event_name = "Ed Sheeran: +-=÷x Tour";
    String fav_event_url = "https://www.ticketmaster.com/ed-sheeran-x-tour-santa-clara-california-09-16-2023/event/1C005D3ED8B28DF0";
    String fav_event_image_url = "https://s1.ticketm.net/dam/a/7d5/97b67038-f926-4676-be88-ebf94cb5c7d5_1802151_ARTIST_PAGE_3_2.jpg";
    String fav_event_date = "09/16/2023";
    String fav_event_time = "6:00 PM";
    String fav_event_venue = "Levi's® Stadium";
    String fav_event_genre = "Music";
    String [] fav_event_artists = {"Ed Sheeran", "Russ", "Maisie Peters"};
    String fav_event_subgenre = "Music | Pop | Pop";
    String fav_event_max_price = "159";
    String fav_event_min_price = "49";
    String fav_event_status = "onsale";
    String fav_event_seatmap = "https://maps.ticketmaster.com/maps/geometry/3/event/1C005D3ED8B28DF0/staticImage?type=png&systemId=HOST";
    String fav_event_address = "4900 Marie P. DeBartolo Way";
    String fav_event_city = "Santa Clara";
    String fav_event_state = "California";
    String fav_event_contact = "415-GO-49ERS (415-464-9377)";


    String fav_event_name1 = "Ed Sheeran w/ Russ";
    String fav_event_image_url1 = "https://s1.ticketm.net/dam/a/7d5/97b67038-f926-4676-be88-ebf94cb5c7d5_1802151_ARTIST_PAGE_3_2.jpg";
    String fav_event_date1 = "09/24/2023";
    String fav_event_time1 = "6:00 PM";
    String fav_event_venue1 = "SoFi Stadium";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        LinearLayout first_search = rootView.findViewById(R.id.fav_first_search);
        LinearLayout second_search = rootView.findViewById(R.id.fav_second_search);

        TextView favNameTextView = rootView.findViewById(R.id.fav_name2);
        TextView favDateTextView = rootView.findViewById(R.id.fav_date2);
        TextView favVenueTextView = rootView.findViewById(R.id.fav_venue2);
        TextView favTimeTextView = rootView.findViewById(R.id.fav_time2);
        ImageView favImageView = rootView.findViewById(R.id.fav_image2);
        TextView favSegmentTextView = rootView.findViewById(R.id.fav_segment2);
        ImageView favFavorite =rootView.findViewById(R.id.fav_favorite2);


        favNameTextView.setText(fav_event_name);
        favDateTextView.setText(fav_event_date);
        favVenueTextView.setText(fav_event_venue);
        favTimeTextView.setText(fav_event_time);
        favSegmentTextView.setText(fav_event_genre);

        favNameTextView.post(() -> {favNameTextView.setSelected(true);});
        favVenueTextView.post(() -> {favVenueTextView.setSelected(true);});

        Glide.with(rootView.getContext())
                .load(fav_event_image_url)
                .into(favImageView);



        TextView favNameTextView1 = rootView.findViewById(R.id.fav_name);
        TextView favDateTextView1 = rootView.findViewById(R.id.fav_date);
        TextView favVenueTextView1 = rootView.findViewById(R.id.fav_venue);
        TextView favTimeTextView1 = rootView.findViewById(R.id.fav_time);
        ImageView favImageView1 = rootView.findViewById(R.id.fav_image);
        TextView favSegmentTextView1 = rootView.findViewById(R.id.fav_segment);
        ImageView favFavorite1 =rootView.findViewById(R.id.fav_favorite);

        favNameTextView1.setText(fav_event_name1);
        favDateTextView1.setText(fav_event_date1);
        favVenueTextView1.setText(fav_event_venue1);
        favTimeTextView1.setText(fav_event_time1);
        favSegmentTextView1.setText(fav_event_genre);

        favNameTextView1.post(() -> {favNameTextView1.setSelected(true);});
        favVenueTextView1.post(() -> {favVenueTextView1.setSelected(true);});

        TextView noFavTextView = rootView.findViewById(R.id.no_fav);

        Glide.with(rootView.getContext())
                .load(fav_event_image_url)
                .into(favImageView1);

        favFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                first_search.setVisibility(View.GONE);
                noFavTextView.setVisibility(View.VISIBLE);

                String message = "Ed Sheeran: +-=÷x Tour removed from favorites";
                Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();

            }
        });

        favNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to start EventDetailActivity
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);

                // Pass the event data to the activity using extras
                intent.putExtra("event_name", fav_event_name);
                intent.putExtra("event_url", fav_event_url);
                intent.putExtra("event_image_url", fav_event_image_url);
                intent.putExtra("event_venue", fav_event_venue);
                intent.putExtra("event_date", fav_event_date);
                intent.putExtra("event_time", fav_event_time);
                intent.putExtra("event_genre", fav_event_genre);
                intent.putExtra("event_artists", fav_event_artists);
                intent.putExtra("event_subgenre", fav_event_subgenre);
                intent.putExtra("event_max_price", fav_event_max_price);
                intent.putExtra("event_min_price", fav_event_min_price);
                intent.putExtra("event_status", fav_event_status);
                intent.putExtra("event_seatmap", fav_event_seatmap);
                intent.putExtra("event_address", fav_event_address);
                intent.putExtra("event_city", fav_event_city);
                intent.putExtra("event_state", fav_event_state);
                intent.putExtra("event_contact", fav_event_contact);

                // Start the activity
                v.getContext().startActivity(intent);
            }
        });

        second_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideOut = new TranslateAnimation(0, -v.getWidth(), 0, 0);
                slideOut.setDuration(600);
                slideOut.setInterpolator(new AccelerateInterpolator());
                slideOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        second_search.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                second_search.startAnimation(slideOut);

                String message = "Ed Sheeran w/ Russ removed to favorites";
                Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
            }
        });



        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);


        TextView noFavTextView = view.findViewById(R.id.no_fav);

        LinearLayout first_search = view.findViewById(R.id.fav_first_search);
        LinearLayout second_search = view.findViewById(R.id.fav_second_search);

        TextView favVenueTextView1 = view.findViewById(R.id.fav_venue);
        TextView favVenueTextView = view.findViewById(R.id.fav_venue2);
        TextView favNameTextView = view.findViewById(R.id.fav_name2);
        TextView favNameTextView1 = view.findViewById(R.id.fav_name);


        favNameTextView1.post(() -> {favNameTextView1.setSelected(true);});
        favVenueTextView1.post(() -> {favVenueTextView1.setSelected(true);});
        favNameTextView.post(() -> {favNameTextView.setSelected(true);});
        favVenueTextView.post(() -> {favVenueTextView.setSelected(true);});



        viewModel.getNumber().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {

                if (number == 1) {
                    noFavTextView.setVisibility(View.GONE);
                    first_search.setVisibility(View.VISIBLE);
                    second_search.setVisibility(View.GONE);
                    favNameTextView1.post(() -> {favNameTextView1.setSelected(true);});
                    favVenueTextView1.post(() -> {favVenueTextView1.setSelected(true);});
                    favNameTextView.post(() -> {favNameTextView.setSelected(true);});
                    favVenueTextView.post(() -> {favVenueTextView.setSelected(true);});
                } else if (number == 0) {
                    noFavTextView.setVisibility(View.VISIBLE);
                    first_search.setVisibility(View.GONE);
                    second_search.setVisibility(View.GONE);
                    favNameTextView1.post(() -> {favNameTextView1.setSelected(true);});
                    favVenueTextView1.post(() -> {favVenueTextView1.setSelected(true);});
                    favNameTextView.post(() -> {favNameTextView.setSelected(true);});
                    favVenueTextView.post(() -> {favVenueTextView.setSelected(true);});
                } else if (number == 2) {
                    noFavTextView.setVisibility(View.GONE);
                    first_search.setVisibility(View.VISIBLE);
                    second_search.setVisibility(View.VISIBLE);
                    favNameTextView1.post(() -> {favNameTextView1.setSelected(true);});
                    favVenueTextView1.post(() -> {favVenueTextView1.setSelected(true);});
                    favNameTextView.post(() -> {favNameTextView.setSelected(true);});
                    favVenueTextView.post(() -> {favVenueTextView.setSelected(true);});
                }
            }
        });
    }



}