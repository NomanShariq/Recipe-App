package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.todocode.recipesdemo.Adapter.AllRecipesAdapter;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BrowseByMealActivity extends AppCompatActivity {
    private String url;
    private SharedPreferences userEmailSharedPrefs;
    private RequestQueue queue;
    private AllRecipesAdapter quickRecipesAdapter;
    private ArrayList<Object> quickArrayList;
    public static final int NUMBER_OF_ADS = 5;
    private AdLoader adLoader;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private SharedPreferences admobNative;
    private TextView nada;
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
        setContentView(R.layout.activity_browse_by_meal);
        Toolbar toolbar = findViewById(R.id.browse_category_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.browse_by_meal));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        admobNative = getSharedPreferences("admobNative", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        nada = (TextView) findViewById(R.id.nada);
        queue = Volley.newRequestQueue(this);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        // Get Quick Recipes
        RecyclerView quickRecipesRecyclerView = (RecyclerView) findViewById(R.id.browse_category_recycler);
        quickArrayList = new ArrayList<>();
        quickRecipesAdapter = new AllRecipesAdapter(this, quickArrayList);
        quickRecipesRecyclerView.setAdapter(quickRecipesAdapter);
        quickRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        quickRecipesRecyclerView.setHasFixedSize(true);
        // Admob Native Ads in RecyclerView
        MobileAds.initialize(BrowseByMealActivity.this, getString(R.string.admob_app_id));
        searchRecipes();
        loadAdmobNativeAds();
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(BrowseByMealActivity.this, getString(R.string.admob_app_id));
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

    private void searchRecipes() {
        String urlApi = url+"/api/recipes/browse/meal/"+getIntent().getStringExtra("meal")+"/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                int id = recipe.getInt("id");
                                String idStr = String.valueOf(id);
                                String title = recipe.getString("title");
                                String description = recipe.getString("description");
                                String review = recipe.getString("review");
                                String time = recipe.getString("time");
                                String servings = recipe.getString("servings");
                                String calories = recipe.getString("calories");
                                String image_url = recipe.getString("image_url");
                                String video_url = recipe.getString("video_url");
                                int rating = recipe.getInt("rating");
                                String ratingStr = String.valueOf(rating);
                                int category_id = recipe.getInt("category_id");
                                String category_id_str = String.valueOf(category_id);
                                String category_name = recipe.getString("category_name");
                                String category_image = recipe.getString("category_image");
                                int chef_id = recipe.getInt("chef_id");
                                String chef_id_str = String.valueOf(chef_id);
                                String chef_username = recipe.getString("chef_username");
                                String chef_image = recipe.getString("chef_image");
                                String chef_trusted = recipe.getString("chef_trusted");
                                String vegetarian_or_not = recipe.getString("vegetarian_or_not");
                                int meal_id = recipe.getInt("meal_id");
                                String meal_id_str = String.valueOf(meal_id);
                                String meal_name = recipe.getString("meal_name");
                                String meal_image = recipe.getString("meal_image");
                                int cuisine_id = recipe.getInt("cuisine_id");
                                String cuisine_id_str = String.valueOf(cuisine_id);
                                String cuisine_name = recipe.getString("cuisine_name");
                                String cuisine_image = recipe.getString("cuisine_image");
                                String chef_paypal = recipe.getString("paypal");
                                String chef_email = recipe.getString("email");
                                String chef_gender = recipe.getString("gender");
                                String chef_vegetarian = recipe.getString("vegetarian");
                                String chef_facebook = recipe.getString("facebook");
                                String chef_twitter = recipe.getString("twitter");
                                String chef_instagram = recipe.getString("instagram");
                                String chef_member_since = recipe.getString("member_since");
                                int views = recipe.getInt("views");
                                quickArrayList.add(new Recipe(idStr,title,description,review,time,servings,calories,image_url,video_url,ratingStr,category_id_str,category_name, category_image,
                                        chef_id_str,chef_username, chef_image, chef_trusted, chef_paypal, chef_email, vegetarian_or_not, meal_id_str,meal_name, meal_image,
                                        cuisine_id_str, cuisine_name, cuisine_image, views, chef_gender, chef_vegetarian, chef_facebook, chef_twitter, chef_instagram, chef_member_since));
                            }
                            quickRecipesAdapter.notifyDataSetChanged();
                            if (quickArrayList.size()==0) {
                                nada.setVisibility(View.VISIBLE);
                            }
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

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 3) {
            return;
        }
        if (quickArrayList.size() > 3) {
            int offset = (quickArrayList.size() / mNativeAds.size())+1;
            int index = 2;
            for (UnifiedNativeAd ad : mNativeAds) {
                quickArrayList.add(index, ad);
                index = index + offset;
            }
            quickRecipesAdapter.notifyDataSetChanged();
        }
    }

    private void loadAdmobNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, admobNative.getString("admobNative", ""));

        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                            Log.e("adloader", "done");
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                            Log.e("adloader", "done");
                        }
                    }
                }).build();
        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}