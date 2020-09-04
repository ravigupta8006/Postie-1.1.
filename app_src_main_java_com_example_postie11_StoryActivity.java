package com.example.postie11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.postie11.Adapters.StoryPageTransition;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends FragmentActivity {




    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new StoryPageTransition());
        pagerAdapter = new StorySlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private class StorySlidePagerAdapter extends FragmentStateAdapter {
        public StorySlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new StoryFragment(HomeFragment.list.get(position));
        }

        @Override
        public int getItemCount() {
            return HomeFragment.list.size();
        }


    }

}
