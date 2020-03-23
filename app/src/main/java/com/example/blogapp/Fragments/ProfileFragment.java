package com.example.blogapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blogapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView profilePhoto;
    private TextView editName, editPassword, name, email;
    private EditText username, password, password2;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private MainFragment.OnFragmentInteractionListener mListener;


    public ProfileFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        return view;
    }
}
