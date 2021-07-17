package com.example.gochat.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gochat.R;
import com.example.gochat.databinding.ActivityMainBinding;
import com.example.gochat.fragments.CallsFragment;
import com.example.gochat.fragments.ChatsFragment;
import com.example.gochat.fragments.StatusFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInit();
    }

    private void doInit() {
        setSupportActionBar(mainBinding.toolbar);
        setUpWithViewPager();
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);
    }

    private void setUpWithViewPager() {
        MainActivity.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new ChatsFragment(), "Chats");
        adapter.add(new StatusFragment(), "Status");
        adapter.add(new CallsFragment(), "Calls");
        mainBinding.viewPager.setAdapter(adapter);
        mainBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeFabIcon(final int index){
        new Handler().post(() -> {
            switch (index){
                case 0:
                    mainBinding.fabAction.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_message)
                    );
                    break;
                case 1:
                    mainBinding.fabAction.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_camera)
                    );
                    break;
                case 2:
                    mainBinding.fabAction.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_add_phone)
                    );
                    break;

            }
        });
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragmentList.get(0);
                case 1:
                    return fragmentList.get(1);
                case 2:
                    return fragmentList.get(2);
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void add(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more_menu:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}