package com.example.postie11;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.postie11.Adapters.FeedsAdapter;
import com.example.postie11.Adapters.StoriesAdapter;
import com.example.postie11.Models.PostModel;
import com.example.postie11.Models.StoryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private StoriesAdapter storiesAdapter;
    private RecyclerView stories,feeds;
    private FeedsAdapter feedsAdapter;
    public static List<StoryModel> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        LinearLayoutManager storiesLayoutManager= new LinearLayoutManager(getContext());
        storiesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stories.setLayoutManager(storiesLayoutManager);

        LinearLayoutManager feedsLayoutManager= new LinearLayoutManager(getContext());
        feedsLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feeds.setLayoutManager(feedsLayoutManager);

        List<String> images = new ArrayList<>();
        images.add("https://images.unsplash.com/photo-1557296387-5358ad7997bb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        images.add("https://www.tones7.com/img/uploads/TIMG_20180504164056.jpg");
        images.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTYsYF_LkJjnmpxw6LxLQHzhquczUQYvz8Iow&usqp=CAU");
        images.add("https://i1.wp.com/imageindia.xyz/wp-content/uploads/2019/11/3d-wallpaper-for-android-mobile-free-download.jpg?w=825&ssl=1");
        images.add("data:image/jpeg;base64,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");
        images.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRj22ftL55BQuhChPl_SCDRcm0Txbrnl0m-7w&usqp=CAU");



        list = new ArrayList<>();
        list.add(new StoryModel(images,"Princes Priya"));
        list.add(new StoryModel(images,"Rock Rahul"));
        list.add(new StoryModel(images,"Rock Shivam"));
        list.add(new StoryModel(images,"Rock Amit"));
        list.add(new StoryModel(images,"Rock Rohan"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));
        list.add(new StoryModel(images,"Rock Ravi"));



        storiesAdapter = new StoriesAdapter(list);
        stories.setAdapter(storiesAdapter);

        List<PostModel> feedslist = new ArrayList<>();
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","","",""));





        feedsAdapter = new FeedsAdapter(feedslist);
        feeds.setAdapter(feedsAdapter);

    }

    private void init(View view){
        stories= view.findViewById(R.id.stories);
        feeds = view.findViewById(R.id.feeds);
    }

}
