package com.yunyizapp.ticketmastersearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

//    All the code partly take a reference from @ChatGPT.
public class SearchFragment extends Fragment {

    private EditText locationInput;
    private EditText distanceInput;
    private View locationUnderline;
    private ArrayAdapter<String> keywordAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);


        locationInput = rootView.findViewById(R.id.location_input);
        locationUnderline = rootView.findViewById(R.id.location_underline);
        distanceInput = rootView.findViewById(R.id.distance_input);
        distanceInput.setText("10");

        Button searchButton = rootView.findViewById(R.id.button);

        //TODO: SPINNER
        Spinner spinner = rootView.findViewById(R.id.category_spinner);

        String[] options = getResources().getStringArray(R.array.category_options);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(requireContext(), options);
        spinner.setAdapter(adapter);

        String selectedCategory = spinner.getSelectedItemPosition() == 0 ? "all" :
                adapter.getItem(spinner.getSelectedItemPosition()).toLowerCase();

//        Spinner spinner = rootView.findViewById(R.id.category_spinner);
//        String selectedCategory = spinner.getSelectedItem().toString().toLowerCase();
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
//                R.array.category_options, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);


        //TODO:switch
        Switch switch1 = rootView.findViewById(R.id.switch1);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationInput.setVisibility(View.GONE);
                    locationUnderline.setVisibility(View.GONE);
                } else {
                    locationInput.setVisibility(View.VISIBLE);
                    locationUnderline.setVisibility(View.VISIBLE);
                }
            }
        });



        // cr: ChatGPT 开始auto-complete部分
        // TODO:Get the AutoCompleteTextView for the keyword input
        AutoCompleteTextView keywordInput = rootView.findViewById(R.id.keyword_input);

//        keywordAdapter = new CustomAutoCompleteAdapter(requireContext(), new ArrayList<>());
//
//        keywordInput.setAdapter(keywordAdapter);

        ProgressBar progressBar = rootView.findViewById(R.id.loading_auto);
        progressBar.setVisibility(View.GONE);

        keywordInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String keyword = s.toString();
//                if (keyword.length() == 1) {progressBar.setVisibility(View.VISIBLE);}
                if (keyword.length() >= 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    String url = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=3LR6PBOR13kss99ycgvFTOrzEsChrYdH&keyword=" + keyword;

                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(requireActivity());

                    // Create a JsonObjectRequest for the request.
                    Log.d(TAG, "Making request to URL: " + url);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            response -> {
                            Log.d(TAG, "Response received: " + response.toString());
                            try {
                                JSONArray results = response.getJSONObject("_embedded")
                                        .getJSONArray("attractions");

                                List<String> suggestions = new ArrayList<>();
                                for (int i = 0; i < results.length(); i++) {
                                    String name = results.getJSONObject(i).getString("name");
                                    suggestions.add(name);
                                }

                                Log.d(TAG, "Suggestions: " +  suggestions);

//                              keywordAdapter.addAll(suggestions);
                                keywordAdapter = new CustomAutoCompleteAdapter(requireContext(), suggestions);
                                keywordInput.setAdapter(keywordAdapter);
                                keywordAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);

                        },
                            error -> Log.e(TAG, "Error occurred auto-complete: " + error.toString()));


                    queue.add(jsonObjectRequest);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Add a click listener to the search button
        searchButton.setOnClickListener(v -> {

            RelativeLayout searchLayout = rootView.findViewById(R.id.search_layout);
//          TODO: 这里设置了search invisible

            // Get the input value from the location field
            String location = locationInput.getText().toString();
            String keyword = keywordInput.getText().toString();
            String distance = distanceInput.getText().toString();

            if (switch1.isChecked()) {
                location = "auto";
            }

            // Check if the input value is empty
            if (location.isEmpty()) {
                locationInput.setError("Please enter a location");
            }
            if (keyword.isEmpty()) {
                keywordInput.setError("Please enter a keyword");
            }
            if (distance.isEmpty()) {
                distanceInput.setError("Please enter a distance");
            }

            if (!location.isEmpty() && !keyword.isEmpty() && !distance.isEmpty())  {
                // If the input values are not empty, proceed with the search action
                String category = spinner.getSelectedItem().toString().toLowerCase();

                // Create a bundle with the search parameters
                Bundle args = new Bundle();

                if(args != null) {

                    args.putString("location", location);
                    args.putString("keyword", keyword);
                    args.putString("distance", distance);
                    args.putString("category", category);

                }

                // Create a new instance of the SearchResultFragment
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(args);

                // Replace the current fragment with the SearchResultFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.hide(SearchFragment.this);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                searchLayout.setVisibility(View.GONE);
            }
        });

        Button clearBtn = rootView.findViewById(R.id.button2);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重置文本框和下拉框的值
                AutoCompleteTextView keywordInput = rootView.findViewById(R.id.keyword_input);
                keywordInput.setText("");

                EditText distanceInput = rootView.findViewById(R.id.distance_input);
                distanceInput.setText("10");

                Spinner categorySpinner = rootView.findViewById(R.id.category_spinner);
                categorySpinner.setSelection(0);

                Switch autoDetectSwitch = rootView.findViewById(R.id.switch1);
                autoDetectSwitch.setChecked(false);

                EditText locationInput = rootView.findViewById(R.id.location_input);
                locationInput.setText("");
                locationInput.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        FavoritesViewModel favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);

        TextView searchAdd = rootView.findViewById(R.id.search_add);
        searchAdd.setOnClickListener(v -> {
            favoritesViewModel.addNumber();
        });

        return rootView;
    }
}


