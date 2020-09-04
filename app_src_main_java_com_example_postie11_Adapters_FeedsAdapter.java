package com.example.postie11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postie11.Models.PostModel;
import com.example.postie11.R;

import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.Viewholder> {

    private List<PostModel> feeds;

    public FeedsAdapter(List<PostModel> feeds){
        this.feeds=feeds;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
