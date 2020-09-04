package com.example.postie11;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private Button add;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    Toolbar toolbar;
    private List<Fragment> fragmentList;
    private  int tabpoistion =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);

        //add = findViewById(R.id.add_btn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // checkUsername();

        fragmentList= new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new NotificationFragment());
        fragmentList.add(new NotificationFragment());
        fragmentList.add(new ProfileFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() !=2){
                    tabpoistion = tab.getPosition();
                    setFragment(tab.getPosition());
                }else{
                    tabLayout.getTabAt(tabpoistion).select();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()!=2){

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).select();
        setFragment(0);
    }

    private  void init(){
        frameLayout = findViewById(R.id.framelayout);
        tabLayout = findViewById(R.id.tablayout);
    }
    public void setFragment(int poistion){


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(),fragmentList.get(poistion));
        fragmentTransaction.commit();
    }

}
