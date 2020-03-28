package com.example.blogapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ImageView profilePhoto;
    private TextView editName, editPassword, name, email;
    private EditText username, usermail, password, password2;
    private Button confirmChanges, reset;

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
        usermail = view.findViewById(R.id.textView9);
        password = view.findViewById(R.id.regPassword3);
        password2 = view.findViewById(R.id.regPassword4);
        confirmChanges = view.findViewById(R.id.confirm);
        reset = view.findViewById(R.id.reset);

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

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String passw = password.getText().toString();
                final String passw2 = password2.getText().toString();

                if(!passw2.isEmpty() && !passw.isEmpty()){

                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), passw);
                    currentUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    currentUser.updatePassword(passw2)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {

                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                } else {

                    Toast.makeText(getContext(), "Check your current password and the new password fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String uname = username.getText().toString();
                final String mail = usermail.getText().toString();
                final String passw = password.getText().toString();
                final String passw2 = password2.getText().toString();

                if(uname.isEmpty() && mail.isEmpty() && passw2.isEmpty()){

                    Toast.makeText(getContext(), "No changes detected", Toast.LENGTH_SHORT).show();
                } else if(passw.isEmpty()) {

                    Toast.makeText(getContext(), "Insert your current password to change data", Toast.LENGTH_SHORT).show();

                    } else {

                        if(!mail.isEmpty()) {

                            AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString(), passw);
                            currentUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            currentUser.updateEmail(mail)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                email.setText(currentUser.getEmail());
                                                                ((HomeActivity) getActivity()).updateNavHeaderMail(currentUser);
                                                            }

                                                            else {

                                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }

                    if(!uname.isEmpty()) {

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(uname)
                                    .build();

                            currentUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                name.setText(currentUser.getDisplayName());
                                                ((HomeActivity) getActivity()).updateNavHeaderName(currentUser);
                                            }
                                        }
                                    });
                    }
                    Toast.makeText(getContext(), "Account updated", Toast.LENGTH_SHORT).show();
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

            pickedImgUri = data.getData();
            profilePhoto.setImageURI(pickedImgUri);
            Glide.with(this).load(pickedImgUri).apply(RequestOptions.circleCropTransform()).into(profilePhoto);

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(pickedImgUri)
                    .build();

            currentUser.updateProfile(profileUpdate);

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
            final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
            UploadTask uploadTask = imageFilePath.putFile(pickedImgUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Toast.makeText(getContext(), "Error during the upload on Firebase storage", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(), "Your photo has been updated", Toast.LENGTH_SHORT).show();
                    ((HomeActivity) getActivity()).updateNavHeaderPhoto(currentUser);
                }
            });
        }
    }
}