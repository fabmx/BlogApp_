package com.example.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.blogapp.Activities.ChatActivity;
import com.example.blogapp.Activities.ImageActivity;
import com.example.blogapp.Models.Court;
import com.example.blogapp.R;

import java.util.List;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.courtviewholder> {

    private Context mContext;
    List<Court> mData;

    public CourtAdapter(Context mContext, List<Court> mData) {

        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public courtviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_playground, parent, false);

        return new courtviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull courtviewholder holder, int position) {

        Glide.with(mContext)
                .load(mData.get(position).getDrawableResource())
                .transforms(new CenterCrop(), new RoundedCorners(16))
                .into(holder.imgCourt);

        final Court court = mData.get(position);

        holder.street.setText(mData.get(position).getStreet());
        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgActivity = new Intent(mContext.getApplicationContext(), ImageActivity.class);
                imgActivity.putExtra("courtId", court.getStreet());
                mContext.startActivity(imgActivity);
            }
        });

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatActivity = new Intent(mContext.getApplicationContext(), ChatActivity.class);
                chatActivity.putExtra("courtId", court.getStreet());
                mContext.startActivity(chatActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class courtviewholder extends RecyclerView.ViewHolder {

        ImageView imgCourt, addImg, chat;
        TextView street;
        RatingBar ratingBar;

        public courtviewholder(@NonNull View itemView) {
            super(itemView);

            imgCourt = itemView.findViewById(R.id.item_pg_img);
            street = itemView.findViewById(R.id.item_pg_street);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            addImg = itemView.findViewById(R.id.add_image);
            chat = itemView.findViewById(R.id.chat);
        }
    }
}
