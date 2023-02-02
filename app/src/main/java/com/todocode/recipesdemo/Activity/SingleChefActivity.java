package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Adapter.AllRecipesAdapter;
import com.todocode.recipesdemo.Adapter.PopularRecipesMainActivityAdapter;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChefActivity extends AppCompatActivity {
    private TextView chefName, membersince, nada;
    private CircleImageView chefImage;
    private Button donate;
    private ArrayList<Object> quickArrayList;
    private AllRecipesAdapter quickRecipesAdapter;
    private String url;
    private RequestQueue queue;
    public static final int NUMBER_OF_ADS = 5;
    private AdLoader adLoader;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private SharedPreferences admobNative;
    // Bottom Banner Ads Start
    private SharedPreferences admobBanner, facebookBanner, bannerType;
    private String bannerBottomTypeStr;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    // Bottom Banner Ads End
    // Facebook Native
    private SharedPreferences facebookNative;
    private NativeAdLayout nativeAdLayout;
    private NativeBannerAd nativeBannerAd;
    private LinearLayout adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chef);
        Toolbar toolbar = findViewById(R.id.single_chef_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.chef_profile));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        nada = (TextView) findViewById(R.id.nada);
        chefName = (TextView) findViewById(R.id.chef_name);
        chefName.setText(getIntent().getStringExtra("username"));
        membersince = (TextView) findViewById(R.id.member_since);
        membersince.setText("Member Since : "+getIntent().getStringExtra("member_since"));
        chefImage = (CircleImageView) findViewById(R.id.profile_image);
        Log.e("email", getIntent().getStringExtra("email"));
        Log.e("paypal", getIntent().getStringExtra("paypal"));
        Picasso.get().load(getIntent().getStringExtra("image")).fit().centerInside().into(chefImage);
        donate = (Button) findViewById(R.id.donate_to_chef);
        if (getIntent().getStringExtra("paypal").contains("paypal.me")) {
            donate.setVisibility(View.VISIBLE);
            donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( getIntent().getStringExtra("paypal") ) );
                    startActivity( browse );
                }
            });
        } else {
            donate.setVisibility(View.GONE);
        }
        facebookNative = getSharedPreferences("facebookNative",MODE_PRIVATE);
        // Facebook Ads Native
        nativeAdLayout = findViewById(R.id.fb_native_banner_ad_container);
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("2f93a253-43f9-4629-bae5-8a529ff6d383");
        loadNativeAd();
        // Get Quick Recipes
        RecyclerView quickRecipesRecyclerView = (RecyclerView) findViewById(R.id.chef_recipes_recycler);
        quickArrayList = new ArrayList<>();
        quickRecipesAdapter = new AllRecipesAdapter(this, quickArrayList);
        quickRecipesRecyclerView.setAdapter(quickRecipesAdapter);
        quickRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        quickRecipesRecyclerView.setHasFixedSize(true);
        // Admob Native Ads in RecyclerView
        admobNative = getSharedPreferences("admobNative", MODE_PRIVATE);
        MobileAds.initialize(SingleChefActivity.this, getString(R.string.admob_app_id));
        getChefRecipes();
        loadAdmobNativeAds();
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(SingleChefActivity.this, getString(R.string.admob_app_id));
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
            AdSettings.addTestDevice("1aa71c93-1bcf-4079-b2a2-8d0cee9ebf77");
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBanner.getString("facebookBanner", null), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            adContainer.addView(facebookAdView);
            facebookAdView.loadAd();
        }
    }

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new NativeBannerAd(this, facebookNative.getString("facebookNative", null));
        // load the ad
        nativeBannerAd.loadAd();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        // nativeAdLayout = getView().findViewById(R.id.statistics_native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.facebook_native_banner_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(this, nativeBannerAd, nativeAdLayout);
        adOptionsView.setIconSizeDp(23);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.social_media_menu, menu);
        return true;
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

    private void getChefRecipes() {
        String urlApi = url+"/api/recipes/chef/"+getIntent().getStringExtra("id");
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
                                double rating = recipe.getDouble("rating");
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.facebook:
                if (getIntent().getStringExtra("facebook").contains("facebook.com")) {
                            Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( getIntent().getStringExtra("facebook") ) );
                            startActivity( browse );
                } else {
                            Toast.makeText(SingleChefActivity.this, "This Chef Does Not Share His Facebook Account!", Toast.LENGTH_SHORT).show();
                        }
                return true;
            case R.id.twitter:
                if (getIntent().getStringExtra("twitter").contains("twitter.com")) {
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( getIntent().getStringExtra("twitter") ) );
                    startActivity( browse );
                } else {
                    Toast.makeText(SingleChefActivity.this, "This Chef Does Not Share His Twitter Account!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.instagram:
                if (getIntent().getStringExtra("instagram").contains("instagram.com")) {
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( getIntent().getStringExtra("instagram") ) );
                    startActivity( browse );
                } else {
                    Toast.makeText(SingleChefActivity.this, "This Chef Does Not Share His instagram Account!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}