package com.burtratyoube.topstream.testtopstream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;

/**
 * Created by Vlad on 07.06.2018.
 */

public class VideoAdapter extends ArrayAdapter<VideoItem> {
    public VideoAdapter(Context context, List<VideoItem> videos){
        super(context,0,videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VideoItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_video_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.video_name);
        TextView uri = (TextView) convertView.findViewById(R.id.video_uri);
        // Populate the data into the template view using the data object
        name.setText(item.name);
        uri.setText(item.uri);
        convertView.setTag(item.uri);

        // Return the completed view to render on screen
        return convertView;
    }

}
