package com.yunyizapp.ticketmastersearch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;
//    All the code partly take a reference from @ChatGPT.
public class CustomAutoCompleteAdapter extends ArrayAdapter<String> {

    private final Context context;

    public CustomAutoCompleteAdapter(Context context, List<String> items) {
        super(context, R.layout.spinner_item, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        return view;
    }
}
