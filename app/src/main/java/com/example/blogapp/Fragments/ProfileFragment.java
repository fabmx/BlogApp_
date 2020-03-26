package com.example.blogapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blogapp.Activities.HomeActivity;
import com.example.blogapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ImageView profilePhoto;
    private TextView editName, editPassword, name, email;
    private EditText username, password, password2;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    Uri pickedImgUri;

    public ProfileFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences appSettingsPref = this.getActivity().getSharedPreferences("AppSettingsPref", 0);
        Boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);

        if(isNightModeOn){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        profilePhoto = view.findViewById(R.id.profilePhoto);
        name = view.findViewById(R.id.textView5);
        email = view.findViewById(R.id.textView6);
        editName = view.findViewById(R.id.textView2);
        editPassword = view.findViewById(R.id.textView4);
        username = view.findViewById(R.id.textView3);
        password = view.findViewById(R.id.regPassword3);
        password2 = view.findViewById(R.id.regPassword4);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        email.setText(currentUser.getEmail());
        name.setText(currentUser.getDisplayName());
        if(currentUser.getPhotoUrl() != null){

            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePhoto);
        }else
            Glide.with(this).load(R.drawable.userphoto).into(profilePhoto);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPermission();
                }
                else {

                    openGallery();
                }
            }
        });

        return view;
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(getContext(),"Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else

            openGallery();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {

            //getActivity().recreate();

            pickedImgUri = data.getData();
            profilePhoto.setImageURI(pickedImgUri);
            Glide.with(this).load(pickedImgUri).apply(RequestOptions.circleCropTransform()).into(profilePhoto);

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(pickedImgUri)
                    .build();

            currentUser.updateProfile(profileUpdate);

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
            final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
            imageFilePath.putFile(pickedImgUri);


            ((HomeActivity) getActivity()).updateNavHeaderPhoto(currentUser);

            //getActivity().recreate();

            //getActivity().onConfigurationChanged();

        }

    }
}