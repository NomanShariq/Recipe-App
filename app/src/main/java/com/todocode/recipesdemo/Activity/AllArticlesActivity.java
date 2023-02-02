package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.todocode.recipesdemo.Adapter.ArticlesAdapter;
import com.todocode.recipesdemo.Model.Article;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AllArticlesActivity extends AppCompatActivity {
    private ArrayList<Article> articlesArrayList;
    private ArticlesAdapter articlesAdapter;
    private RequestQueue queue;
    private String url;
    // Bottom Banner Ads Start
    private SharedPreferences admobBanner, facebookBanner, bannerType;
    private String bannerBottomTypeStr;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    // Bottom Banner Ads End
    // Interstitial Ads
    private SharedPreferences facebookInterstitial, admobInterstitial, interstitialType;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_articles);
        Toolbar toolbar = findViewById(R.id.all_articles_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.all_articles));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        RecyclerView articlesRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler);
        articlesArrayList = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(this, articlesArrayList);
        articlesRecyclerView.setAdapter(articlesAdapter);
        articlesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getArticles();
        // Interstitial Ads
        interstitialType = getSharedPreferences("interstitialType",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        if (interstitialType.getString("interstitialType", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialType.getString("interstitialType", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        articlesAdapter.setOnItemClickListener(new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                                article.putExtra("id", articlesArrayList.get(position).getId());
                                article.putExtra("title", articlesArrayList.get(position).getTitle());
                                article.putExtra("text", articlesArrayList.get(position).getText());
                                article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                                article.putExtra("image", articlesArrayList.get(position).getImage());
                                article.putExtra("date", articlesArrayList.get(position).getDate());
                                startActivity(article);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                                article.putExtra("id", articlesArrayList.get(position).getId());
                                article.putExtra("title", articlesArrayList.get(position).getTitle());
                                article.putExtra("text", articlesArrayList.get(position).getText());
                                article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                                article.putExtra("image", articlesArrayList.get(position).getImage());
                                article.putExtra("date", articlesArrayList.get(position).getDate());
                                startActivity(article);
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                facebookInterstitialAd.show();
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };
                        facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(listener).build());
                        prepareInterstitialAd();
                    } else {
                        Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                        article.putExtra("id", articlesArrayList.get(position).getId());
                        article.putExtra("title", articlesArrayList.get(position).getTitle());
                        article.putExtra("text", articlesArrayList.get(position).getText());
                        article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                        article.putExtra("image", articlesArrayList.get(position).getImage());
                        article.putExtra("date", articlesArrayList.get(position).getDate());
                        startActivity(article);
                    }

                } else if(interstitialType.getString("interstitialType", "").equals("admob")) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                                mInterstitialAd.show();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                                article.putExtra("id", articlesArrayList.get(position).getId());
                                article.putExtra("title", articlesArrayList.get(position).getTitle());
                                article.putExtra("text", articlesArrayList.get(position).getText());
                                article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                                article.putExtra("image", articlesArrayList.get(position).getImage());
                                article.putExtra("date", articlesArrayList.get(position).getDate());
                                startActivity(article);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                                article.putExtra("id", articlesArrayList.get(position).getId());
                                article.putExtra("title", articlesArrayList.get(position).getTitle());
                                article.putExtra("text", articlesArrayList.get(position).getText());
                                article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                                article.putExtra("image", articlesArrayList.get(position).getImage());
                                article.putExtra("date", articlesArrayList.get(position).getDate());
                                startActivity(article);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent article = new Intent(AllArticlesActivity.this, SingleArticleActivity.class);
                        article.putExtra("id", articlesArrayList.get(position).getId());
                        article.putExtra("title", articlesArrayList.get(position).getTitle());
                        article.putExtra("text", articlesArrayList.get(position).getText());
                        article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                        article.putExtra("image", articlesArrayList.get(position).getImage());
                        article.putExtra("date", articlesArrayList.get(position).getDate());
                        startActivity(article);
                    }
                }
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(AllArticlesActivity.this, getString(R.string.admob_app_id));
            admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
            adsLinear = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBanner.getString("admobBanner", ""));
            bannerAdmobAdView.setAdSize(AdSize.FULL_BANNER);
            adsLinear.addView(bannerAdmobAdView);
            adsLinear.setGravity(Gravity.CENTER_HORIZONTAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerAdmobAdView.loadAd(adRequest);
            bannerAdmobAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    adsLinear.setVisibility(View.VISIBLE);
                }
            });
        } else if(bannerBottomTypeStr.equals("facebook")) {
            facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
            AudienceNetworkAds.initialize(this);
            AdSettings.addTestDevice("e5076253-9872-42cc-a0bd-fa714b019713");
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBanner.getString("facebookBanner", null), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            adContainer.addView(facebookAdView);
            facebookAdView.loadAd();
        }
    }

    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitial.getString("admobInterstitial", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void prepareInterstitialAd() {
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("2f93a253-43f9-4629-bae5-8a529ff6d383");
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitial.getString("facebookInterstitial", ""));
        facebookInterstitialAd.loadAd();
    }

    private void getArticles() {
        String test = url+"/api/articles/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, test, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                int id = recipe.getInt("id");
                                String title = recipe.getString("title");
                                String image = recipe.getString("image");
                                String text = recipe.getString("text");
                                String textSmall = recipe.getString("text_small");
                                String date = recipe.getString("date");
                                articlesArrayList.add(new Article(String.valueOf(id),title,textSmall, text, image, date));
                            }
                            articlesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}