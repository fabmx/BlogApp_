package com.example.blogapp.Adapters;

import android.content.Context;
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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class courtviewholder extends RecyclerView.ViewHolder {

        ImageView imgCourt, imgFav;
        TextView street;
        RatingBar ratingBar;

        public courtviewholder(@NonNull View itemView) {
            super(itemView);

            imgCourt = itemView.findViewById(R.id.item_pg_img);
            street = itemView.findViewById(R.id.item_pg_street);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
