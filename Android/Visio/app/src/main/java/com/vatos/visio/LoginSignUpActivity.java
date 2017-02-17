package com.vatos.visio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by bilal on 15-02-2017.
 */

public class LoginSignUpActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

    }


    class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new LoginFragment();
                case 1:
                    return new SignUpFragment();
            }
            return null;
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

}
