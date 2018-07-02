package com.burtratyoube.topstream.testtopstream;


import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class VideoFragment extends Fragment {

    private Uri videoUri;
    private VideoView mainVideoView;
    private MediaController mediaC;
    private int stopPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainVideoView = view.findViewById(R.id.mainVideoView);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = view.getLayoutParams();

        // change height of the params e.g. 480dp
        params.height = (int)((double)size.x / 16 * 9);

        view.setLayoutParams(params);




        mediaC = new MediaController(getContext());

        mainVideoView.setMediaController(mediaC);
        mediaC.setAnchorView(mainVideoView);


        if( videoUri != null ) {

            mainVideoView.setVideoURI(videoUri);
            mainVideoView.start();
            mainVideoView.requestFocus();
        }

    }
    public void setUri(Uri uri){
        videoUri = uri;
    }


}
