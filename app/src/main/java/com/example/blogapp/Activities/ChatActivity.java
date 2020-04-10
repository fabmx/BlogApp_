package com.example.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blogapp.Adapters.ChatAdapter;
import com.example.blogapp.Models.Message;
import com.example.blogapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;

    Button btnAddComment;
    EditText editTextComment;
    ImageView userPic;

    List<Message> messageList;
    RecyclerView RvChat;
    ChatAdapter chatAdapter;

    String MESSAGE_KEY = "Messages";
    String courtId = "court";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);

        SharedPreferences appSettingsPref = this.getSharedPreferences("AppSettingsPref", 0);
        Boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);

        if(isNightModeOn){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RvChat = findViewById(R.id.rv_chat);

        editTextComment = findViewById(R.id.chat_detail_comment);
        btnAddComment = findViewById(R.id.chat_detail_add_comment_btn);
        userPic = findViewById(R.id.chat_detail_currentuser_img);

        if(currentUser.getPhotoUrl() != null){

            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(userPic);
        }else
            Glide.with(this).load(R.drawable.userphoto).into(userPic);

        courtId = getIntent().getStringExtra("courtId");


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference commentReference = firebaseDatabase.getReference(MESSAGE_KEY).child(courtId).push();
                String message_content = editTextComment.getText().toString();
                String uid = currentUser.getUid();
                String uname = currentUser.getDisplayName();

                Message message;

                if(currentUser.getPhotoUrl() != null){

                    message = new Message(message_content, uid, currentUser.getPhotoUrl().toString(), uname);
                }else {

                    message = new Message(message_content, uid, null, uname);
                }

                commentReference.setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("message added");
                        editTextComment.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : " + e.getMessage());
                    }
                });
            }
        });

        iniRvMessage();
    }

    private void iniRvMessage() {

        RvChat.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(MESSAGE_KEY).child(courtId);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {

                    Message message = snap.getValue(Message.class);
                    messageList.add(message) ;
                }
                chatAdapter = new ChatAdapter(getApplicationContext(), messageList);
                RvChat.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {

        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
