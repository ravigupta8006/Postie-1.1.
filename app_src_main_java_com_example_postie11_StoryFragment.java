package com.example.postie11;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.postie11.Models.StoryModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {


    public StoryFragment() {
        // Required empty public constructor
    }

    private StoryModel storyModel;

    public StoryFragment(StoryModel storyModel) {
        this.storyModel = storyModel;
    }

    private int progressCount =0;
    private int progressBarInex = 0;
    private ImageView imageView;
    private Timer timer;
    private TimerTask timerTask;
    private LinearLayout progressContainer;
    private ProgressBar defailtProgressBar;
    private boolean timerON = true;
    private CircleImageView profile;
    private TextView username;
    private float upperlimit, lowerlimit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setProgressBars();
        Glide.with(this).load(storyModel.getImages().get(progressBarInex)).into(imageView);
        setControls();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timerTask.cancel();
    }

    private void init(View view){

        profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        imageView = view.findViewById(R.id.imageView);
        progressContainer = view.findViewById(R.id.progress_container);

        defailtProgressBar = ((ProgressBar)progressContainer.getChildAt(0));
    }

    private void setProgressBars(){
        if(storyModel.getImages()!=null) {
            for(int i=1; i<storyModel.getImages().size(); i++ ) {

                ProgressBar progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleHorizontal);
                progressBar.setLayoutParams(defailtProgressBar.getLayoutParams());
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.story_progress));
                progressContainer.addView(progressBar);
            }
        }
    }
    private  void setTimer(){

        timer =new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (timerON) {

                    //            10000ms*1 =1s
                    //            100ms*10 =1s
                    //            50ms*100 =5s
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressCount == 100) {
                                progressBarInex++;
                                if (progressBarInex < progressContainer.getChildCount()) {

                                    progressCount = 0;
                                    Glide.with(getContext()).load(storyModel.getImages().get(progressBarInex)).into(imageView);

                                } else {

                                    getActivity().finish();
                                }
                            } else {
                                progressCount++;
                                ((ProgressBar) progressContainer.getChildAt(progressBarInex)).setProgress(progressCount);
                            }
                        }
                    });

                }
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,50);

    }
    private  void setControls(){
        float widthProportion = (getResources().getDisplayMetrics().widthPixels/100.0f)*25;

        upperlimit = getResources().getDisplayMetrics().widthPixels - widthProportion;
        lowerlimit = widthProportion;


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(event.getX() <= lowerlimit && progressBarInex>0)
                        {
                            ((ProgressBar) progressContainer.getChildAt(progressBarInex)).setProgress(0);
                            progressBarInex--;
                            ((ProgressBar) progressContainer.getChildAt(progressBarInex)).setProgress(0);
                            progressCount=0;
                            Glide.with(getContext()).load(storyModel.getImages().get(progressBarInex)).into(imageView);

                        }else if (event.getX()>= upperlimit && progressBarInex<progressContainer.getChildCount()-1){
                            ((ProgressBar) progressContainer.getChildAt(progressBarInex)).setProgress(100);
                            progressBarInex++;
                            progressCount=0;
                            Glide.with(getContext()).load(storyModel.getImages().get(progressBarInex)).into(imageView);

                        }else {

                            timerON = false;
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        timerON = true;
                        return true;
                }
                return false;
            }
        });
    }
}
