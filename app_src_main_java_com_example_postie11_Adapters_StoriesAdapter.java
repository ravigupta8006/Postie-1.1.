package com.example.postie11.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.postie11.Models.StoryModel;
import com.example.postie11.R;
import com.example.postie11.StoryActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewholder> {

    private List<StoryModel> list;

    public StoriesAdapter(List<StoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,parent,false);
        return new StoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewholder holder, final int position) {

        Glide.with(holder.itemView.getContext()).load(list.get(position).getImages().get(0)).placeholder(R.drawable.ic_account_profile).into(holder.thumbnail);

        holder.name.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storyIntent = new Intent(holder.itemView.getContext(), StoryActivity.class);
                holder.itemView.getContext().startActivity(storyIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StoryViewholder extends RecyclerView.ViewHolder{
        private CircleImageView thumbnail;
        private TextView name;

        public StoryViewholder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.profile);
            name=itemView.findViewById(R.id.name);
        }
    }
}
