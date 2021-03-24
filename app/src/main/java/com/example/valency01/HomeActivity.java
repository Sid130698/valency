package com.example.valency01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inittializefields();
        bottomNavigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeFramelayout,new HomePageFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment=null;
                if(item.getItemId()==R.id.homePage){
                    currentFragment=new HomePageFragment();
                }
                else if(item.getItemId()==R.id.searchPage){
                    currentFragment=new SearchPageFragment();
                }
                else if(item.getItemId()==R.id.addPostPage){
                    currentFragment=new PostPageFragment();
                }
                else if(item.getItemId()==R.id.messagesPage){
                    currentFragment=new MessagesPageFragment();
                }else if(item.getItemId()==R.id.searchFriendsPage){
                    currentFragment=new FriendsPageFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,currentFragment).commit();
                return true;
            }
        });
    }

    private void inittializefields() {
        bottomNavigationView=findViewById(R.id.bottomNavigationBar);
    }
}