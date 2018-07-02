package com.burtratyoube.topstream.testtopstream;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UploadsFragment extends Fragment {

    private ListView listView;
    private DatabaseReference baseRef;
    private List<VideoItem> listVideoItem;
    private FirebaseUser user;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uploads, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        this.view = view;
        super.onViewCreated(view, savedInstanceState);

        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            baseRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("uploads");
        }catch( Exception e ){

        }
        listView = (ListView) view.findViewById(R.id.listViewUploads);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                addFragment((String)view.getTag());
            }
        });



        Toast.makeText(getContext(), baseRef.toString(), Toast.LENGTH_SHORT).show();

        baseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueEventListener(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void valueEventListener(DataSnapshot dataSnapshot){
        GenericTypeIndicator<List<VideoItem>> t = new GenericTypeIndicator<List<VideoItem>>(){};
        ListView listView = (ListView) view.findViewById(R.id.listViewUploads);

        listVideoItem = new LinkedList<VideoItem>();

        for (DataSnapshot videoItemSnapshot: dataSnapshot.getChildren()) {
            listVideoItem.add(videoItemSnapshot.getValue(VideoItem.class));
        }

        VideoAdapter adapter = new VideoAdapter(getContext(),listVideoItem);
        listView.setAdapter(adapter);
    }

    public void addFragment(String str){
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            VideoFragment vf = new VideoFragment();
            Uri videoUri = Uri.parse(str);
            vf.setUri(videoUri);
            transaction.add(R.id.videoFrameUploads, vf);
            transaction.commit();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
