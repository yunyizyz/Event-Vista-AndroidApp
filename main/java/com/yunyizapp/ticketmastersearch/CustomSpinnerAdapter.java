package com.yunyizapp.ticketmastersearch;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
//    All the code partly take a reference from @ChatGPT.
public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] options;

    public CustomSpinnerAdapter(Context context, String[] options) {
        super(context, R.layout.spinner_item, options);
        this.context = context;
        this.options = options;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        return view;
    }

    @Override
    public String getItem(int position) {
        return position == 0 ? context.getString(R.string.all) : options[position];
    }
}