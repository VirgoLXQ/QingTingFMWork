package com.lxqhmlwyh.qingtingfm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.fragment.AnalyseFragment;
import com.lxqhmlwyh.qingtingfm.fragment.FavouriteFragment;
import com.lxqhmlwyh.qingtingfm.fragment.SearchFragment;
import com.lxqhmlwyh.qingtingfm.service.GetFMItemJsonService;

public class MainActivity extends AppCompatActivity {

    private SearchFragment searchFragment;
    private FavouriteFragment favouriteFragment;
    private AnalyseFragment analyseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        startService();
        initView();
    }
    private void initView(){
        searchFragment=new SearchFragment();
        favouriteFragment=new FavouriteFragment();
        analyseFragment=new AnalyseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,favouriteFragment).commit();
        BottomNavigationView bottomNav = findViewById(R.id.main_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        findViewById(R.id.to_playing).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlayActivity.class)));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_menu_find://切换到SearchFragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,searchFragment).commit();
                    break;
                case R.id.nav_menu_album:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,favouriteFragment).commit();
                    break;
                case R.id.nav_menu_analyse:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,analyseFragment).commit();
                    break;
            }
            return true;
        }
    };

    private void startService(){
        Intent getFmItemJs=new Intent(this, GetFMItemJsonService.class);
        startService(getFmItemJs);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}