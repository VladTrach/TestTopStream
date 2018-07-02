package com.burtratyoube.topstream.testtopstream;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 200;
    private Button upload;
    private FrameLayout main_view;
    private BottomNavigationView nav_view;
    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private UploadsFragment uploadsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_view = (FrameLayout) findViewById(R.id.nav_frame);
        nav_view = (BottomNavigationView) findViewById(R.id.nav_view);

        init_fragments();

        setNavigationListener();

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void setNavigationListener(){
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        setFragment(homeFragment);
                        return true;
                    case R.id.favorites :
                        setFragment(favoritesFragment);
                        return true;
                    case R.id.uploads :
                        setFragment(uploadsFragment);
                        return true;

                    default:
                        return false;
                }

            }
        });
    }
    private void init_fragments(){
        homeFragment = new HomeFragment();
        favoritesFragment = new FavoritesFragment();
        uploadsFragment = new UploadsFragment();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_frame, fragment);
        fragmentTransaction.commit();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    final Uri uri = data.getData();

                    final DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference().child("allVideo");

                    final String key = baseRef.push().getKey();

                    final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("allVideo/"+key+uri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getBaseContext(),"upload failed!!!",Toast.LENGTH_SHORT).show();// Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            /*

*/
                            Toast.makeText(getBaseContext(),"uploaded successful!!!",Toast.LENGTH_SHORT).show();
                        }
                    }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                VideoItem videoItem = new VideoItem(uri.getLastPathSegment(),downloadUri.toString());

                                baseRef.child(key).setValue(videoItem);
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
