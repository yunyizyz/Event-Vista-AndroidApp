package com.yunyizapp.ticketmastersearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//    All the code partly take a reference from @ChatGPT.
public class SearchResultFragment extends Fragment {
    private static final String TAG = SearchResultFragment.class.getSimpleName();

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);


        ProgressBar progressBar = rootView.findViewById(R.id.loading1);
        progressBar.setVisibility(View.VISIBLE);

        // Get the search query parameters from the arguments
        assert getArguments() != null;
        String location = getArguments().getString("location");
        String keyword = getArguments().getString("keyword");
        String distance = getArguments().getString("distance");
        String category = getArguments().getString("category");


        // Build the search URL
        String url = "https://yunyiztktandroidapp.wl.r.appspot.com/search?location=" + location +
                "&keyword=" + keyword + "&distance=" + distance + "&category=" + category;

        Log.d(TAG, "Search URL: " + url);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());


        // Create a StringRequest for the request.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    // Display the response.
                    Log.d(TAG, "Response is: " + response);
                    // Extract data from the response
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        rootView.findViewById(R.id.no_event).setVisibility(View.GONE);

                        if(!jsonResponse.has("_embedded")) {
                            rootView.findViewById(R.id.no_event).setVisibility(View.VISIBLE);
                        }

                        JSONArray eventsArray = jsonResponse.getJSONObject("_embedded").getJSONArray("events");

                        List<Event> eventsList = new ArrayList<>();
                        for (int i = 0; i < eventsArray.length(); i++) {
                            JSONObject eventObj = eventsArray.getJSONObject(i);

                            String eventName = eventObj.optString("name", "Not Found!");
                            String eventUrl = eventObj.optString("url", "Not Found!");
                            String eventImageUrl = eventObj.optJSONArray("images").optJSONObject(1).optString("url", "Not Found!");
                            String eventVenue = eventObj.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optString("name", "Not Found!");
                            String eventGenre = eventObj.optJSONArray("classifications").optJSONObject(0).optJSONObject("segment").optString("name", "Not Found!");

                            // Format the date string
                            String eventDateStr = eventObj.optJSONObject("dates").optJSONObject("start").optString("localDate", "Not Found!");
                            String formattedDateStr = "";
                            if (!eventDateStr.equals("Not Found!")) {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    Date date = inputFormat.parse(eventDateStr);
                                    formattedDateStr = outputFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                formattedDateStr = "Not Found!";
                            }
                            String eventDate = formattedDateStr;

                            // Format the time string
                            String eventTimeStr = eventObj.optJSONObject("dates").optJSONObject("start").optString("localTime", "Not Found!");
                            String formattedTimeStr = "";
                            if (!eventTimeStr.equals("Not Found!")) {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
                                try {
                                    Date time = inputFormat.parse(eventTimeStr);
                                    formattedTimeStr = outputFormat.format(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                formattedTimeStr = "Not Found!";
                            }
                            String eventTime = formattedTimeStr;


//TODO:传递至Detail
                            String maxPrice = "N/A";
                            String minPrice = "N/A";
                            String status = "Not Found!";
                            String seatMap = "Not Found!";
                            String address = "Not Found!";
                            String city = "Not Found!";
                            String state = "Not Found!";
                            String contact = "Not Found!";
                            String oh = "Not Found!";
                            String gr = "Not Found!";
                            String cr = "Not Found!";

                            // Check if priceRanges array exists and has length greater than 0
                            if (eventObj.has("priceRanges") && eventObj.getJSONArray("priceRanges").length() > 0) {
                                JSONObject priceObj = eventObj.getJSONArray("priceRanges").optJSONObject(0);
                                maxPrice = priceObj.optString("max", "Not Found!");
                                minPrice = priceObj.optString("min", "Not Found!");
                            }

                            // Check if dates object exists and has status field
                            if (eventObj.has("dates") && eventObj.getJSONObject("dates").has("status")) {
                                status = eventObj.getJSONObject("dates").getJSONObject("status").optString("code", "Not Found!");
                            }

                            // Check if seatmap object exists and has staticUrl field
                            if (eventObj.has("seatmap") && !eventObj.isNull("seatmap")) {
                                seatMap = eventObj.getJSONObject("seatmap").optString("staticUrl", "Not Found!");
                            }

                            // Check if _embedded object exists and has venues array with length greater than 0
                            if (eventObj.has("_embedded") && eventObj.getJSONObject("_embedded").has("venues") && eventObj.getJSONObject("_embedded").getJSONArray("venues").length() > 0) {
                                JSONObject venueObj = eventObj.getJSONObject("_embedded").getJSONArray("venues").optJSONObject(0);

                                if (venueObj != null) {
                                    address = venueObj.optJSONObject("address").optString("line1", "Not Found!");
                                    city = venueObj.optJSONObject("city").optString("name", "Not Found!");
                                    state = venueObj.optJSONObject("state").optString("name", "Not Found!");

                                    JSONObject boxOfficeInfo = venueObj.optJSONObject("boxOfficeInfo");
                                    if (boxOfficeInfo != null) {
                                        contact = boxOfficeInfo.optString("phoneNumberDetail", "Not Found!");
                                        oh = boxOfficeInfo.optString("openHoursDetail", "Not Found!");
                                    }

                                    JSONObject generalInfo = venueObj.optJSONObject("generalInfo");
                                    if (generalInfo != null) {
                                        gr = generalInfo.optString("generalRule", "Not Found!");
                                        cr = generalInfo.optString("childRule", "Not Found!");
                                    }
                                }
                            }

                            JSONArray attractionsArray = eventObj.optJSONObject("_embedded").optJSONArray("attractions");
                            String[] artists = new String[attractionsArray.length()];
                            for (int s = 0; s < attractionsArray.length(); s++) {
                                JSONObject attractionObj = attractionsArray.optJSONObject(s);
                                String artistName = attractionObj.optString("name", "Not Found!");
                                artists[s] = artistName;
                            }

                            JSONObject classificationsObj = eventObj.optJSONArray("classifications").optJSONObject(0);
                            String subgenre1 = classificationsObj.optJSONObject("segment").optString("name", "Not Found!");
                            String subgenre2 = classificationsObj.optJSONObject("genre").optString("name", "Not Found!");
                            String subgenre3 = classificationsObj.optJSONObject("subGenre").optString("name", "Not Found!");
                            String subgenre = subgenre1+ " | " + subgenre2 + " | "+subgenre3;

//TODO:传递至Detail

                            Event event = new Event(eventName, eventUrl, eventImageUrl, eventDate, eventTime, eventVenue, eventGenre, artists, subgenre, subgenre1, subgenre2, subgenre3, maxPrice, minPrice, status, seatMap, address, city, state, contact, oh, gr, cr);
                            eventsList.add(event);


                        }



                        for (Event event : eventsList) {
                            Log.d(TAG, "event_name: " + event.getName());
                            Log.d(TAG, "event_url: " + event.getUrl());
                            Log.d(TAG, "event_image_url: " + event.getImageUrl());
                            Log.d(TAG, "event_date: " + event.getDate());
                            Log.d(TAG, "event_time: " + event.getTime());
                            Log.d(TAG, "event_venue: " + event.getVenue());
                            Log.d(TAG, "event_genre: " + event.getGenre());
                            Log.d(TAG, "event_artists: " + Arrays.toString(event.getArtists()));
                            Log.d(TAG, "event_subgenre: " + event.getSubgenre());
                            Log.d(TAG, "event_max_price: " + event.getMaxPrice());
                            Log.d(TAG, "event_min_price: " + event.getMinPrice());
                            Log.d(TAG, "event_status: " + event.getStatus());
                            Log.d(TAG, "event_seatmap: " + event.getSeatmap());
                            Log.d(TAG, "event_address: " + event.getAddress());
                            Log.d(TAG, "event_city: " + event.getCity());
                            Log.d(TAG, "event_state: " + event.getState());
                            Log.d(TAG, "event_contact: " + event.getContact());
                            Log.d(TAG, "Artists: " + Arrays.toString(event.getArtists()));

                        }

                        // Set up RecyclerView
                        RecyclerView recyclerView = rootView.findViewById(R.id.eventRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        EventAdapter adapter = new EventAdapter(eventsList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.GONE);

                }, error -> {
            // Handle errors here.
            Log.e(TAG, "Error occurred search result" + error.toString());
            progressBar.setVisibility(View.GONE);
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        FavoritesViewModel favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);

        TextView favAdd = rootView.findViewById(R.id.fav_add);
        favAdd.setOnClickListener(v -> {
            favoritesViewModel.addNumber();
        });

        TextView backToSearch=rootView.findViewById(R.id.back_to_search);
        // Add a click listener to the TextView component
        backToSearch.setOnClickListener(v -> {
            // Get the FragmentManager
            FragmentManager fragmentManager = getParentFragmentManager();

            // Pop the current fragment from the back stack
            fragmentManager.popBackStack();

            // Show the search layout in the SearchFragment
            View searchLayout = getActivity().findViewById(R.id.search_layout);
            searchLayout.setVisibility(View.VISIBLE);
        });
        return rootView;

        //TODO:ONCREATE END

    }

    public static class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {


        private List<Event> mEvents;


        public EventAdapter(List<Event> events) {

            mEvents = events;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View eventView = inflater.inflate(R.layout.item_event, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(eventView);
            return viewHolder;
        }



        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Get the data model based on position
            Event event = mEvents.get(position);

            // Set item views based on your views and data model
            holder.eventNameTextView.setText(event.getName());
            holder.eventDateTextView.setText(event.getDate());
            holder.eventVenueTextView.setText(event.getVenue());
//            holder.eventUrlTextView.setText(event.getUrl());
            holder.eventTimeTextView.setText(event.getTime());
            holder.eventSegmentTextView.setText(event.getGenre());

            Glide.with(holder.itemView.getContext())
                    .load(event.getImageUrl())
                    .into(holder.eventImageView);

//TODO:点击爱心

            holder.eventFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.eventFavorite.setImageResource(R.drawable.heart_filled);

                    String message = event.getName() + " added to favorites";
                    Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.eventFavorite.setImageResource(R.drawable.heart_outline);
                        }
                    }, 6000);
                }
            });

            holder.eventSegmentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.eventFavorite.setImageResource(R.drawable.heart_filled);
                        }
                    }, 6000); // 6000 milliseconds = 6 seconds
                }
            });


            //TODO:点击event
            holder.eventNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent to start EventDetailActivity
                    Intent intent = new Intent(v.getContext(), EventDetailActivity.class);

                    // Pass the event data to the activity using extras
                    intent.putExtra("event_name", event.getName());
                    intent.putExtra("event_url", event.getUrl());
                    intent.putExtra("event_image_url", event.getImageUrl());
                    intent.putExtra("event_venue", event.getVenue());
                    intent.putExtra("event_date", event.getDate());
                    intent.putExtra("event_time", event.getTime());
                    intent.putExtra("event_genre", event.getGenre());
                    intent.putExtra("event_artists", event.getArtists());
                    intent.putExtra("event_subgenre", event.getSubgenre());
                    intent.putExtra("event_max_price", event.getMaxPrice());
                    intent.putExtra("event_min_price", event.getMinPrice());
                    intent.putExtra("event_status", event.getStatus());
                    intent.putExtra("event_seatmap", event.getSeatmap());
                    intent.putExtra("event_address", event.getAddress());
                    intent.putExtra("event_city", event.getCity());
                    intent.putExtra("event_state", event.getState());
                    intent.putExtra("event_contact", event.getContact());
                    intent.putExtra("event_oh", event.getOh());
                    intent.putExtra("event_gr", event.getGr());
                    intent.putExtra("event_cr", event.getCr());



                    // Start the activity
                    v.getContext().startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView eventNameTextView;
            public TextView eventDateTextView;
            public TextView eventVenueTextView;
            public TextView eventUrlTextView;
            public TextView eventTimeTextView;
            public ImageView eventImageView;
            public TextView eventSegmentTextView;
            public ImageView eventFavorite;



            public ViewHolder(View itemView) {
                super(itemView);

                // Get references to views
                eventNameTextView = itemView.findViewById(R.id.event_name);
                eventDateTextView = itemView.findViewById(R.id.event_date);
                eventVenueTextView = itemView.findViewById(R.id.event_venue);
                eventTimeTextView = itemView.findViewById(R.id.event_time);
                eventImageView = itemView.findViewById(R.id.event_image);
                eventSegmentTextView = itemView.findViewById(R.id.event_segment);
                eventFavorite = itemView.findViewById(R.id.event_favorite);

                eventNameTextView.post(() -> {eventNameTextView.setSelected(true);});
                eventVenueTextView.post(() -> {eventVenueTextView.setSelected(true);});

            }
        }
    }
}

