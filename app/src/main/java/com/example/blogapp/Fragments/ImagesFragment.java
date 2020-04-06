package com.example.blogapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.Adapters.PostAdapter;
import com.example.blogapp.Models.Post;
import com.example.blogapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesFragment extends Fragment {

    public ImagesFragment (){

    }

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    List<Post> postList;

    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    String courtId = "court";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_images, container, false);
        postRecyclerView = view.findViewById(R.id.rv_post);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance();
        courtId = getActivity().getIntent().getStringExtra("courtId");
        myRef = mDatabase.getReference("Court_images/" + courtId);


        SharedPreferences appSettingsPref = this.getActivity().getSharedPreferences("AppSettingsPref", 0);
        Boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);

        if(isNightModeOn){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                for(DataSnapshot postsnap: dataSnapshot.getChildren()){

                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter = new PostAdapter(getActivity(), postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
