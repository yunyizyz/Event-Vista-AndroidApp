package com.yunyizapp.ticketmastersearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//    All the code partly take a reference from @ChatGPT.
//    Progress bar code resource: https://www.youtube.com/watch?v=YsHHXg1vbcc&list=PLTI3A7t9EYIdy4YmMEyF655EEXl1ad6wh&index=7&t=381s
public class DetailArtistFragment extends Fragment {
    public class ArtistData {
        private String name;
        private String followers;
        private String popularity;
        private String spotifyUrl;
        private String imageUrl;
        private List<String> topTracks;

        public ArtistData(String name, String followers, String popularity, String spotifyUrl, String imageUrl, List<String> topTracks) {
            this.name = name;
            this.followers = followers;
            this.popularity = popularity;
            this.spotifyUrl = spotifyUrl;
            this.imageUrl = imageUrl;
            this.topTracks = topTracks;
        }

        public String getName() {
            return name;
        }

        public String getFollowers() {
            return followers;
        }

        public String getPopularity() {
            return popularity;
        }

        public String getSpotifyUrl() {
            return spotifyUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public List<String> getTopTracks() {
            return topTracks;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<ArtistData> artistDataList = new ArrayList<>();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_artist, container, false);

        ProgressBar progressBar = rootView.findViewById(R.id.loading2);
        progressBar.setVisibility(View.VISIBLE);

        // Get the data from the bundle
        Bundle bundle = getArguments();

        if (bundle != null) {

            Log.e(TAG, "Bundle is not null - artist.");


            String[] eventArtists = bundle.getStringArray("event_artists");
            Log.e(TAG, "Event_artists:"+ Arrays.toString(eventArtists));

            // Define a Volley RequestQueue
            RequestQueue queue = Volley.newRequestQueue(requireContext());

            // Loop through each artist in eventArtists
            for (String artistName : eventArtists) {
                String artistUrl = "https://yunyiztktandroidapp.wl.r.appspot.com/callsptf/" + artistName;

                // Define a StringRequest to make a GET request to the artist URL
                StringRequest stringRequest = new StringRequest(Request.Method.GET, artistUrl,
                        response -> {
                            try {
                                Log.e(TAG, "Start Volley Artist: "+artistName);
                                // Parse the JSON response
                                JSONObject artistJson = new JSONObject(response);

                                String checkName = artistJson.getString("name");


                                int followersInt = artistJson.getInt("followers");
                                String followers;
                                if (followersInt > 1000000) {
                                    followers = Math.round(followersInt / 1000000f) + " M";
                                } else {
                                    followers = String.valueOf(followersInt);
                                }
                                String popularity = artistJson.getString("popularity");
                                String spotifyUrl = artistJson.getString("spotifyUrl");
                                String imageUrl = artistJson.getJSONArray("images").getJSONObject(0).getString("url");

                                JSONArray topTracksJson = artistJson.getJSONArray("topTracks");
                                List<String> topTracks = new ArrayList<>();
                                for (int i = 0; i < topTracksJson.length() && i < 3; i++) {
                                    JSONObject trackJson = topTracksJson.getJSONObject(i);
                                    // extract the image URL from the album
                                    String albumImageUrl = trackJson.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                                    topTracks.add(albumImageUrl);
                                }

                                // Create a new ArtistData object and add it to the list
                                ArtistData artistData = new ArtistData(artistName, followers, popularity, spotifyUrl, imageUrl, topTracks);

                                if (artistName.equals(checkName)) {artistDataList.add(artistData);}

                                Log.e(TAG, "Event_artists:"+ artistDataList);

                                if (artistDataList.isEmpty()) {
                                    rootView.findViewById(R.id.no_artist).setVisibility(View.VISIBLE);
                                } else {
                                    rootView.findViewById(R.id.no_artist).setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "Error retrieving artist data 0");
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                        },


                        error -> Log.e(TAG, "Error retrieving artist data: " + error.getMessage()));
                        progressBar.setVisibility(View.VISIBLE);


                // Add the request to the RequestQueue
                queue.add(stringRequest);
            }

            // Set up RecyclerView
            RecyclerView recyclerView = rootView.findViewById(R.id.artRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            DetailArtistFragment.ArtAdapter adapter = new DetailArtistFragment.ArtAdapter(artistDataList);
            recyclerView.setAdapter(adapter);

        } else {
            Log.e(TAG, "Bundle in venue is null.");
        }

        // Return the inflated layout
        return rootView;
    }

    public static class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ViewHolder> {

        private List<ArtistData> artistDataList;

        public ArtAdapter(List<ArtistData> artistDataList) {
            this.artistDataList = artistDataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_art, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ArtistData artistData = artistDataList.get(position);

            holder.nameTextView.setText(artistData.getName());
            holder.followersTextView.setText(artistData.getFollowers()+ " Followers");

            String popularity = artistData.getPopularity();
            holder.art_pop_circle.setProgress(Integer.parseInt(popularity));
            holder.art_pop_num.setText(popularity);


            Picasso.get().load(artistData.getImageUrl()).into(holder.imageView);

            holder.spotifyUrlTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistData.getSpotifyUrl()));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    holder.itemView.getContext().startActivity(browserIntent);
                    holder.itemView.getContext().startActivity(Intent.createChooser(browserIntent, "Open with"));


//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistData.getSpotifyUrl()));
//                    holder.itemView.getContext().startActivity(browserIntent);
                    //TODO:1
                }
            });


            // Load the album images into the ImageView elements
            List<String> topTracks = artistData.getTopTracks();
            if (topTracks.size() > 0) {
                Picasso.get().load(topTracks.get(0)).into(holder.album1ImageView);
            }
            if (topTracks.size() > 1) {
                Picasso.get().load(topTracks.get(1)).into(holder.album2ImageView);
            }
            if (topTracks.size() > 2) {
                Picasso.get().load(topTracks.get(2)).into(holder.album3ImageView);
            }
        }

        @Override
        public int getItemCount() {
            return artistDataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView nameTextView;
            public TextView followersTextView;
            public TextView spotifyUrlTextView;
            public ImageView album1ImageView;
            public ImageView album2ImageView;
            public ImageView album3ImageView;
            public ProgressBar art_pop_circle;
            public TextView art_pop_num;


            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.artist_image);
                nameTextView = view.findViewById(R.id.artist_name);
                followersTextView = view.findViewById(R.id.artist_follower);
                art_pop_circle = view.findViewById(R.id.art_pop1);
                art_pop_num = view.findViewById(R.id.art_pop2);
                spotifyUrlTextView = view.findViewById(R.id.event_segment);
                album1ImageView = view.findViewById(R.id.album1);
                album2ImageView = view.findViewById(R.id.album2);
                album3ImageView = view.findViewById(R.id.album3);
            }
        }
    }

}





