package com.example.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blogapp.Activities.PostDetailActivity;
import com.example.blogapp.Models.Post;
import com.example.blogapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new postViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {

        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

        String userImg = mData.get(position).getUserPhoto();
        if(userImg != null){

            Glide.with(mContext).load(mData.get(position).getUserPhoto())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgPostProfile);
        }else {
            Glide.with(mContext).load(R.drawable.userphoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgPostProfile);
        }

        final Post post = mData.get(position);

        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postDetailActivity = new Intent(mContext.getApplicationContext(), PostDetailActivity.class);

                postDetailActivity.putExtra("title", post.getTitle());
                postDetailActivity.putExtra("postImage", post.getPicture());
                postDetailActivity.putExtra("description", post.getDescription());
                postDetailActivity.putExtra("postKey", post.getPostKey());
                postDetailActivity.putExtra("userPhoto", post.getUserPhoto());
                // will fix this later i forgot to add user name to post object
                //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                if(post.getTimeStamp() != null) {
                    long timestamp = (long) post.getTimeStamp();
                    postDetailActivity.putExtra("postDate", timestamp);
                }

                mContext.startActivity(postDetailActivity);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView imgPost;
        ImageView imgPostProfile;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
        }
    }
}