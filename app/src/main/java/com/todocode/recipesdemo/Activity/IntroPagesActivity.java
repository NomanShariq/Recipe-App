package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.todocode.recipesdemo.Adapter.IntroPagerAdapter;
import com.todocode.recipesdemo.Manager.PreferenceManager;
import com.todocode.recipesdemo.R;

import java.util.Locale;

public class IntroPagesActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mPager;
    private int[] layouts = {R.layout.slider1, R.layout.slider2, R.layout.slider3, R.layout.slider4, R.layout.slider5};
    private LinearLayout dotsLayout;
    public Button btnNext, btnSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        if (new PreferenceManager(IntroPagesActivity.this).checkPreference()) {
            loadHome();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_intro_pages);
        dotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        btnNext = (Button) findViewById(R.id.bnNext);
        btnSkip = (Button) findViewById(R.id.bnSkip);
        btnNext.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        IntroPagerAdapter introPagerAdapter = new IntroPagerAdapter(layouts, this);
        mPager.setAdapter(introPagerAdapter);
        createDots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if (position==layouts.length-1) {
                    btnNext.setText(getResources().getString(R.string.start));
                    btnSkip.setVisibility(View.INVISIBLE);
                } else {
                    btnNext.setText(getResources().getString(R.string.next));
                    btnSkip.setText(getResources().getString(R.string.skip));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int current_position) {
        if (dotsLayout != null)
            dotsLayout.removeAllViews();
        ImageView[] dots = new ImageView[layouts.length];
        for (int i = 0;i<layouts.length;i++) {
            dots[i] = new ImageView(IntroPagesActivity.this);
            if (i == current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(IntroPagesActivity.this,R.drawable.active_dots));
            }
            else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(IntroPagesActivity.this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dotsLayout.addView(dots[i], params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnNext :
                loadNextSlide();
                break;
            case R.id.bnSkip :
                loadHome();
                new PreferenceManager(IntroPagesActivity.this).writePreference();
                break;
        }
    }

    private void loadNextSlide() {
        int nextSlide = mPager.getCurrentItem()+1;
        if (nextSlide<layouts.length) {
            mPager.setCurrentItem(nextSlide);
        } else {
            loadHome();
            new PreferenceManager(IntroPagesActivity.this).writePreference();
        }
    }

    private void loadHome() {
        startActivity(new Intent(IntroPagesActivity.this, SplashScreenActivity.class));
        finish();
    }

}