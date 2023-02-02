package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashokslsk.androidabcd.squarerating.SquareRatingView;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Adapter.AllChefsAdapter;
import com.todocode.recipesdemo.Adapter.ArticlesAdapter;
import com.todocode.recipesdemo.Adapter.CuisinesAdapter;
import com.todocode.recipesdemo.Adapter.MealsAdapter;
import com.todocode.recipesdemo.Adapter.PopularCategoriesAdapter;
import com.todocode.recipesdemo.Adapter.PopularRecipesMainActivityAdapter;
import com.todocode.recipesdemo.Adapter.RecipesMainViewPagerAdapter;
import com.todocode.recipesdemo.Adapter.TrustedChefAdapter;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Manager.MyApplication;
import com.todocode.recipesdemo.Model.Article;
import com.todocode.recipesdemo.Model.Category;
import com.todocode.recipesdemo.Model.Chef;
import com.todocode.recipesdemo.Model.Cuisine;
import com.todocode.recipesdemo.Model.Meal;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SharedPreferences userEmailSharedPrefs, userIdSharedPrefs, userUsernameSharedPrefs, userGenderSharedPrefs, userVegetarianSharedPrefs, userAvatarUrlSharedPrefs, userPaypalSharedPrefs, userTrustedSharedPrefs, userFacebookSharedPrefs, userTwitterSharedPrefs, userInstagramSharedPrefs, userMemberSince;
    private String url;
    private List<Recipe> recipesPagerList;
    private RecipesMainViewPagerAdapter recipesMainViewPagerAdapter;
    private ViewPager2 recipesViewPager;
    private RequestQueue queue;
    ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    Timer timer;
    private SquareRatingView ratingBar;
    public Dialog rankDialog;
    private PopularRecipesMainActivityAdapter popularRecipesAdapter, quickRecipesAdapter;
    private MealsAdapter mealsAdapter;
    private TrustedChefAdapter trustedChefAdapter;
    private ArrayList<Recipe> recipesArrayList, quickArrayList;
    private ArrayList<Meal> mealsArrayList;
    private ArrayList<Chef> chefsArrayList;
    private ArrayList<Category> categoriesArrayList;
    private PopularCategoriesAdapter popularCategoriesAdapter;
    private ArrayList<Cuisine> cuisinesArrayList;
    private CuisinesAdapter cuisinesAdapter;
    private ArrayList<Article> articlesArrayList;
    private ArticlesAdapter articlesAdapter;
    private CircleImageView profileImage;
    private TextView currentUserNameHeader, currentEmailHeader;
    private CircleImageView currentProfileImageHeader;
    private GoogleSignInClient mGoogleSignInClient;
    private FloatingActionButton floatingActionButton;
    // Bottom Banner Ads Start
    private SharedPreferences admobBanner, facebookBanner, bannerType, facebookNative;
    private String bannerBottomTypeStr;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    // Google Admob Native Ad Start
    private UnifiedNativeAd nativeAd;
    private UnifiedNativeAdView adViewNative;
    private FrameLayout frameLayout;
    private SharedPreferences admobNative;
    // Facebook Native
    private NativeAdLayout nativeAdLayout;
    private NativeBannerAd nativeBannerAd;
    private LinearLayout adView;
    // Interstitial Ads
    private SharedPreferences facebookInterstitial, admobInterstitial, interstitialType;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private InterstitialAd mInterstitialAd;
    // Video Ad
    private SharedPreferences videoType, admobVideo, facebookVideo;
    private RewardedVideoAd rewardedVideoAd;
    private String TAG = "video";
    private com.google.android.gms.ads.reward.RewardedVideoAd rewardedAd;
    MyApplication mMyApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyApplication = MyApplication.getmInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_btn);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Interstitial Ads
        interstitialType = getSharedPreferences("interstitialType",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        if (interstitialType.getString("interstitialType", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialType.getString("interstitialType", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        facebookNative = getSharedPreferences("facebookNative",MODE_PRIVATE);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        userUsernameSharedPrefs = getSharedPreferences("userUsernameSharedPrefs", MODE_PRIVATE);
        userGenderSharedPrefs = getSharedPreferences("userGenderSharedPrefs", MODE_PRIVATE);
        userVegetarianSharedPrefs = getSharedPreferences("userVegetarianSharedPrefs", MODE_PRIVATE);
        userAvatarUrlSharedPrefs = getSharedPreferences("userAvatarUrlSharedPrefs", MODE_PRIVATE);
        userPaypalSharedPrefs = getSharedPreferences("userPaypalSharedPrefs", MODE_PRIVATE);
        userTrustedSharedPrefs = getSharedPreferences("userTrustedSharedPrefs", MODE_PRIVATE);
        userFacebookSharedPrefs = getSharedPreferences("userFacebookSharedPrefs", MODE_PRIVATE);
        userTwitterSharedPrefs = getSharedPreferences("userTwitterSharedPrefs", MODE_PRIVATE);
        userInstagramSharedPrefs = getSharedPreferences("userInstagramSharedPrefs", MODE_PRIVATE);
        userMemberSince = getSharedPreferences("userMemberSince", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        // Daily Inspiration View Pager
        recipesViewPager = findViewById(R.id.recipesMainViewPager);
        recipesPagerList = new ArrayList<>();
        recipesMainViewPagerAdapter = new RecipesMainViewPagerAdapter(recipesPagerList);
        recipesViewPager.setAdapter(recipesMainViewPagerAdapter);
        getLatestRecipes();
        recipesMainViewPagerAdapter.setOnItemClickListener(new RecipesMainViewPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(MainActivity.this, SingleRecipeActivity.class);
                recipe.putExtra("id", recipesPagerList.get(position).getId());
                recipe.putExtra("title", recipesPagerList.get(position).getTitle());
                recipe.putExtra("description", recipesPagerList.get(position).getDescription());
                recipe.putExtra("time", recipesPagerList.get(position).getTime());
                recipe.putExtra("servings", recipesPagerList.get(position).getServings());
                recipe.putExtra("calories", recipesPagerList.get(position).getCalories());
                recipe.putExtra("image_url", recipesPagerList.get(position).getImage_url());
                recipe.putExtra("video_url", recipesPagerList.get(position).getVideo_url());
                recipe.putExtra("rating", recipesPagerList.get(position).getRating());
                recipe.putExtra("category_id", recipesPagerList.get(position).getCategory_id());
                recipe.putExtra("category_name", recipesPagerList.get(position).getCategory_name());
                recipe.putExtra("chef_username", recipesPagerList.get(position).getChef_username());
                recipe.putExtra("chef_id", recipesPagerList.get(position).getChef_id());
                recipe.putExtra("chef_image", recipesPagerList.get(position).getChef_image());
                recipe.putExtra("chef_trusted", recipesPagerList.get(position).getChef_trusted());
                recipe.putExtra("meal_name", recipesPagerList.get(position).getMeal_name());
                recipe.putExtra("cuisine_name", recipesPagerList.get(position).getCuisine_name());
                recipe.putExtra("views", recipesPagerList.get(position).getViews());
                recipe.putExtra("paypal", recipesPagerList.get(position).getChef_paypal());
                recipe.putExtra("email", recipesPagerList.get(position).getChef_email());
                recipe.putExtra("gender", recipesPagerList.get(position).getChef_gender());
                recipe.putExtra("vegetarian", recipesPagerList.get(position).getChef_vegetarian());
                recipe.putExtra("facebook", recipesPagerList.get(position).getChef_facebook());
                recipe.putExtra("twitter", recipesPagerList.get(position).getChef_twitter());
                recipe.putExtra("instagram", recipesPagerList.get(position).getChef_instagram());
                recipe.putExtra("member_since", recipesPagerList.get(position).getChef_member_since());
                startActivity(recipe);
            }
        });
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                recipesViewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        if (recipesPagerList.size()!=0) {
                        recipesViewPager.setCurrentItem((recipesViewPager.getCurrentItem()+1)%recipesPagerList.size());
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);
        // Get Popular Recipes
        RecyclerView popularRecipesRecyclerView = (RecyclerView) findViewById(R.id.most_popular_recycler);
        recipesArrayList = new ArrayList<>();
        popularRecipesAdapter = new PopularRecipesMainActivityAdapter(this, recipesArrayList);
        popularRecipesRecyclerView.setAdapter(popularRecipesAdapter);
        popularRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getPopularRecipes();
        popularRecipesAdapter.setOnItemClickListener(new PopularRecipesMainActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(MainActivity.this, SingleRecipeActivity.class);
                recipe.putExtra("id", recipesArrayList.get(position).getId());
                recipe.putExtra("title", recipesArrayList.get(position).getTitle());
                recipe.putExtra("description", recipesArrayList.get(position).getDescription());
                recipe.putExtra("time", recipesArrayList.get(position).getTime());
                recipe.putExtra("servings", recipesArrayList.get(position).getServings());
                recipe.putExtra("calories", recipesArrayList.get(position).getCalories());
                recipe.putExtra("image_url", recipesArrayList.get(position).getImage_url());
                recipe.putExtra("video_url", recipesArrayList.get(position).getVideo_url());
                recipe.putExtra("rating", recipesArrayList.get(position).getRating());
                recipe.putExtra("category_id", recipesArrayList.get(position).getCategory_id());
                recipe.putExtra("category_name", recipesArrayList.get(position).getCategory_name());
                recipe.putExtra("chef_username", recipesArrayList.get(position).getChef_username());
                recipe.putExtra("chef_id", recipesArrayList.get(position).getChef_id());
                recipe.putExtra("chef_image", recipesArrayList.get(position).getChef_image());
                recipe.putExtra("chef_trusted", recipesArrayList.get(position).getChef_trusted());
                recipe.putExtra("meal_name", recipesArrayList.get(position).getMeal_name());
                recipe.putExtra("cuisine_name", recipesArrayList.get(position).getCuisine_name());
                recipe.putExtra("views", recipesArrayList.get(position).getViews());
                recipe.putExtra("paypal", recipesArrayList.get(position).getChef_paypal());
                recipe.putExtra("email", recipesArrayList.get(position).getChef_email());
                recipe.putExtra("gender", recipesArrayList.get(position).getChef_gender());
                recipe.putExtra("vegetarian", recipesArrayList.get(position).getChef_vegetarian());
                recipe.putExtra("facebook", recipesArrayList.get(position).getChef_facebook());
                recipe.putExtra("twitter", recipesArrayList.get(position).getChef_twitter());
                recipe.putExtra("instagram", recipesArrayList.get(position).getChef_instagram());
                recipe.putExtra("member_since", recipesArrayList.get(position).getChef_member_since());
                startActivity(recipe);
            }
        });
        TextView viewAllPopularRecipes = (TextView) findViewById(R.id.view_all_recipes);
        // Add Interstitial here
        viewAllPopularRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
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
                        Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                        startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                        startActivity(allRecipes);
                    }
                }
            }
        });
        // Get Meals
        RecyclerView mealsRecyclerView = (RecyclerView) findViewById(R.id.meals_recycler);
        mealsArrayList = new ArrayList<>();
        mealsAdapter = new MealsAdapter(this, mealsArrayList);
        mealsRecyclerView.setAdapter(mealsAdapter);
        mealsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        getMeals();
        mealsAdapter.setOnItemClickListener(new MealsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent searchRecipes = new Intent(MainActivity.this, BrowseByMealActivity.class);
                searchRecipes.putExtra("meal", mealsArrayList.get(position).getId());
                startActivity(searchRecipes);
            }
        });
        // Get Chefs
        RecyclerView trustedChefs = (RecyclerView) findViewById(R.id.trusted_chefs_recycler);
        chefsArrayList = new ArrayList<>();
        trustedChefAdapter = new TrustedChefAdapter(this, chefsArrayList);
        trustedChefs.setAdapter(trustedChefAdapter);
        trustedChefs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trustedChefAdapter.setOnItemClickListener(new TrustedChefAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (chefsArrayList.get(position).getEmail().equals(userEmailSharedPrefs.getString("userEmailSharedPrefs", ""))) {
                    Intent profile = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(profile);
                } else {
                    Intent singleChef = new Intent(MainActivity.this, SingleChefActivity.class);
                    singleChef.putExtra("id", chefsArrayList.get(position).getId());
                    singleChef.putExtra("email", chefsArrayList.get(position).getEmail());
                    singleChef.putExtra("username", chefsArrayList.get(position).getUsername());
                    singleChef.putExtra("gender", chefsArrayList.get(position).getGender());
                    singleChef.putExtra("vegetarian", chefsArrayList.get(position).getVegetarian());
                    singleChef.putExtra("image", chefsArrayList.get(position).getAvatar_url());
                    singleChef.putExtra("paypal", chefsArrayList.get(position).getPaypal());
                    singleChef.putExtra("trusted", chefsArrayList.get(position).getTrusted());
                    singleChef.putExtra("facebook", chefsArrayList.get(position).getFacebook());
                    singleChef.putExtra("twitter", chefsArrayList.get(position).getTwitter());
                    singleChef.putExtra("instagram", chefsArrayList.get(position).getInstagram());
                    singleChef.putExtra("member_since", chefsArrayList.get(position).getMember_since());
                    startActivity(singleChef);
                }
            }
        });
        getChefs();
        TextView viewAllChefs = (TextView) findViewById(R.id.view_all_chefs);
        viewAllChefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                                startActivity(allRecipes);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                                startActivity(allRecipes);
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
                        Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                        startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                                startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                                startActivity(allRecipes);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent allRecipes = new Intent(MainActivity.this, AllChefsActivity.class);
                        startActivity(allRecipes);
                    }
                }
            }
        });
        // Get Trusted Chefs
        RecyclerView quickRecipesRecyclerView = (RecyclerView) findViewById(R.id.quick_recycler);
        quickArrayList = new ArrayList<>();
        quickRecipesAdapter = new PopularRecipesMainActivityAdapter(this, quickArrayList);
        quickRecipesRecyclerView.setAdapter(quickRecipesAdapter);
        quickRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getQuickRecipes();
        quickRecipesAdapter.setOnItemClickListener(new PopularRecipesMainActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(MainActivity.this, SingleRecipeActivity.class);
                recipe.putExtra("id", quickArrayList.get(position).getId());
                recipe.putExtra("title", quickArrayList.get(position).getTitle());
                recipe.putExtra("description", quickArrayList.get(position).getDescription());
                recipe.putExtra("time", quickArrayList.get(position).getTime());
                recipe.putExtra("servings", quickArrayList.get(position).getServings());
                recipe.putExtra("calories", quickArrayList.get(position).getCalories());
                recipe.putExtra("image_url", quickArrayList.get(position).getImage_url());
                recipe.putExtra("video_url", quickArrayList.get(position).getVideo_url());
                recipe.putExtra("rating", quickArrayList.get(position).getRating());
                recipe.putExtra("category_id", quickArrayList.get(position).getCategory_id());
                recipe.putExtra("category_name", quickArrayList.get(position).getCategory_name());
                recipe.putExtra("chef_username", quickArrayList.get(position).getChef_username());
                recipe.putExtra("chef_id", quickArrayList.get(position).getChef_id());
                recipe.putExtra("chef_image", quickArrayList.get(position).getChef_image());
                recipe.putExtra("paypal", quickArrayList.get(position).getChef_paypal());
                recipe.putExtra("email", quickArrayList.get(position).getChef_email());
                recipe.putExtra("chef_trusted", quickArrayList.get(position).getChef_trusted());
                recipe.putExtra("meal_name", quickArrayList.get(position).getMeal_name());
                recipe.putExtra("cuisine_name", quickArrayList.get(position).getCuisine_name());
                recipe.putExtra("views", quickArrayList.get(position).getViews());
                recipe.putExtra("gender", quickArrayList.get(position).getChef_gender());
                recipe.putExtra("vegetarian", quickArrayList.get(position).getChef_vegetarian());
                recipe.putExtra("facebook", quickArrayList.get(position).getChef_facebook());
                recipe.putExtra("twitter", quickArrayList.get(position).getChef_twitter());
                recipe.putExtra("instagram", quickArrayList.get(position).getChef_instagram());
                recipe.putExtra("member_since", quickArrayList.get(position).getChef_member_since());
                startActivity(recipe);
            }
        });
        TextView viewAllQuickRecipes = (TextView) findViewById(R.id.view_all_quick);
        viewAllQuickRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
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
                        Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                        startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                                startActivity(allRecipes);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent allRecipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                        startActivity(allRecipes);
                    }
                }
            }
        });
        // Get Popular Categories
        RecyclerView popularCategoriesRecyclerView = (RecyclerView) findViewById(R.id.popular_categories_recycler);
        categoriesArrayList = new ArrayList<>();
        popularCategoriesAdapter = new PopularCategoriesAdapter(this, categoriesArrayList);
        popularCategoriesRecyclerView.setAdapter(popularCategoriesAdapter);
        popularCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Header Drawer
        View header = navigationView.getHeaderView(0);
        Button logout = (Button) header.findViewById(R.id.logout);
        currentUserNameHeader = (TextView) header.findViewById(R.id.current_user_name);
        currentEmailHeader = (TextView) header.findViewById(R.id.current_user_email);
        currentProfileImageHeader = (CircleImageView) header.findViewById(R.id.profile_image_header);
        currentEmailHeader.setText(userEmailSharedPrefs.getString("userEmailSharedPrefs", ""));
        getConnectedChefData();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Shared Preferences
                userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", "").apply();
                userIdSharedPrefs.edit().putString("userIdSharedPrefs", "").apply();
                userUsernameSharedPrefs.edit().putString("userUsernameSharedPrefs", "").apply();
                userGenderSharedPrefs.edit().putString("userGenderSharedPrefs", "").apply();
                userVegetarianSharedPrefs.edit().putString("userVegetarianSharedPrefs", "").apply();
                userAvatarUrlSharedPrefs.edit().putString("userAvatarUrlSharedPrefs", "").apply();
                userPaypalSharedPrefs.edit().putString("userPaypalSharedPrefs", "").apply();
                userTrustedSharedPrefs.edit().putString("userTrustedSharedPrefs", "").apply();
                userFacebookSharedPrefs.edit().putString("userFacebookSharedPrefs", "").apply();
                userTwitterSharedPrefs.edit().putString("userTwitterSharedPrefs", "").apply();
                userInstagramSharedPrefs.edit().putString("userInstagramSharedPrefs", "").apply();
                // Logout Google
                if (mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();
                }
                // Logout Facebook
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                // Go To Login Page
                Intent loginPage = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(loginPage);
                finish();
            }
        });
        popularCategoriesAdapter.setOnItemClickListener(new PopularCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent searchRecipes = new Intent(MainActivity.this, BrowseByCategoryActivity.class);
                searchRecipes.putExtra("category", categoriesArrayList.get(position).getId());
                startActivity(searchRecipes);
            }
        });
        TextView viewAllQuickCat = (TextView) findViewById(R.id.view_all_categories);
        viewAllQuickCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                                startActivity(allRecipes);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                                startActivity(allRecipes);
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
                        Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                        startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                                startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                                startActivity(allRecipes);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent allRecipes = new Intent(MainActivity.this, AllCategoriesActivity.class);
                        startActivity(allRecipes);
                    }
                }
            }
        });
        // Get Cuisines
        RecyclerView cuisinesRecyclerView = (RecyclerView) findViewById(R.id.cuisines_recycler);
        cuisinesArrayList = new ArrayList<>();
        cuisinesAdapter = new CuisinesAdapter(this, cuisinesArrayList);
        cuisinesRecyclerView.setAdapter(cuisinesAdapter);
        cuisinesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        getCuisines();
        cuisinesAdapter.setOnItemClickListener(new CuisinesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent searchRecipes = new Intent(MainActivity.this, BrowseByCuisineActivity.class);
                searchRecipes.putExtra("cuisine", cuisinesArrayList.get(position).getId());
                startActivity(searchRecipes);
            }
        });
        // Get Articles
        RecyclerView articlesRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler);
        articlesArrayList = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(this, articlesArrayList);
        articlesRecyclerView.setAdapter(articlesAdapter);
        articlesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getArticles();
        articlesAdapter.setOnItemClickListener(new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent article = new Intent(MainActivity.this, SingleArticleActivity.class);
                article.putExtra("id", articlesArrayList.get(position).getId());
                article.putExtra("title", articlesArrayList.get(position).getTitle());
                article.putExtra("text", articlesArrayList.get(position).getText());
                article.putExtra("textSmall", articlesArrayList.get(position).getTextSmall());
                article.putExtra("image", articlesArrayList.get(position).getImage());
                article.putExtra("date", articlesArrayList.get(position).getDate());
                startActivity(article);
            }
        });
        TextView viewAllArticles = (TextView) findViewById(R.id.view_all_articles);
        viewAllArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialType.getString("interstitialType", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                                startActivity(allRecipes);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                                startActivity(allRecipes);
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
                        Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                        startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                                startActivity(allRecipes);
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
                                Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                                startActivity(allRecipes);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent allRecipes = new Intent(MainActivity.this, AllArticlesActivity.class);
                        startActivity(allRecipes);
                    }
                }
            }
        });
        // Profile Image Click
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(profile);
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(MainActivity.this, getString(R.string.admob_app_id));
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
            AdSettings.addTestDevice("79f1845f-e26f-421b-b301-7ff2d12b2b55");
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBanner.getString("facebookBanner", null), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            adContainer.addView(facebookAdView);
            facebookAdView.loadAd();
        }
        // Admob Native Ad
        admobNative = getSharedPreferences("admobNative", MODE_PRIVATE);
        // Native Admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        adViewNative = (UnifiedNativeAdView) this.getLayoutInflater()
                .inflate(R.layout.admob_native_ad_unified, null);
        frameLayout = findViewById(R.id.native_ad_home);
        refreshAd();
        // Facebook Ads Native
        nativeAdLayout = findViewById(R.id.fb_native_banner_ad_container);
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("79f1845f-e26f-421b-b301-7ff2d12b2b55");
        loadNativeAd();
        // Video AD
        admobVideo = getSharedPreferences("admobVideo", MODE_PRIVATE);
        facebookVideo = getSharedPreferences("facebookVideo", MODE_PRIVATE);
        videoType = getSharedPreferences("videoType", MODE_PRIVATE);
        AudienceNetworkAds.initialize(MainActivity.this);
        AdSettings.addTestDevice("79f1845f-e26f-421b-b301-7ff2d12b2b55");
        rewardedVideoAd = new RewardedVideoAd(MainActivity.this, facebookVideo.getString("facebookVideo", ""));
        final RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Intent add = new Intent(MainActivity.this, AddNewRecipeActivity.class);
                startActivity(add);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                rewardedVideoAd.show();
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
            }

            @Override
            public void onRewardedVideoClosed() {
                Intent add = new Intent(MainActivity.this, AddNewRecipeActivity.class);
                startActivity(add);
            }
        };
        rewardedAd = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        loadRewardedVideoAd();
        final String videoAdType = videoType.getString("videoType", "");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoAdType.equals("facebook")) {
                    rewardedVideoAd.loadAd(
                            rewardedVideoAd.buildLoadAdConfig()
                                    .withAdListener(rewardedVideoAdListener)
                                    .build());
                } else if(videoAdType.equals("admob")) {
                    if (rewardedAd.isLoaded()) {
                        rewardedAd.show();
                        rewardedAd.setRewardedVideoAdListener(new com.google.android.gms.ads.reward.RewardedVideoAdListener() {
                            @Override
                            public void onRewardedVideoAdLoaded() {
                            }

                            @Override
                            public void onRewardedVideoAdOpened() {
                            }

                            @Override
                            public void onRewardedVideoStarted() {
                            }

                            @Override
                            public void onRewardedVideoAdClosed() {
                                Intent add = new Intent(MainActivity.this, AddNewRecipeActivity.class);
                                startActivity(add);
                                //loadRewardedVideoAd();
                            }

                            @Override
                            public void onRewarded(RewardItem rewardItem) {
                            }

                            @Override
                            public void onRewardedVideoAdLeftApplication() {

                            }

                            @Override
                            public void onRewardedVideoAdFailedToLoad(int i) {
                                Intent add = new Intent(MainActivity.this, AddNewRecipeActivity.class);
                                startActivity(add);
                            }

                            @Override
                            public void onRewardedVideoCompleted() {
                            }
                        });
                    } else {
                        Intent add = new Intent(MainActivity.this, AddNewRecipeActivity.class);
                        startActivity(add);
                    }
                }
            }
        });
    }

    private void loadRewardedVideoAd() {
        if (!rewardedAd.isLoaded()) {
            rewardedAd.loadAd(admobVideo.getString("admobVideo", ""), new AdRequest.Builder().build());
        }
    }


    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitial.getString("admobInterstitial", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void prepareInterstitialAd() {
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("79f1845f-e26f-421b-b301-7ff2d12b2b55");
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitial.getString("facebookInterstitial", ""));
        facebookInterstitialAd.loadAd();
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

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getVideoController();
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this,admobNative.getString("admobNative", null) );
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                populateUnifiedNativeAdView(unifiedNativeAd, adViewNative);
                frameLayout.removeAllViews();
                frameLayout.addView(adViewNative);
            }

        });
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
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
        com.facebook.ads.MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
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

    private void getChefs() {
        String test = url+"/api/chefs/trusted";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, test, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                int id = recipe.getInt("id");
                                String idStr = String.valueOf(id);
                                String email = recipe.getString("email");
                                String username = recipe.getString("username");
                                String gender = recipe.getString("gender");
                                String vegetarian = recipe.getString("vegetarian");
                                String avatar_url = recipe.getString("avatar_url");
                                String paypal = recipe.getString("paypal");
                                String trusted = recipe.getString("trusted");
                                String facebook = recipe.getString("facebook");
                                String twitter = recipe.getString("twitter");
                                String instagram = recipe.getString("instagram");
                                int recipes = recipe.getInt("recipes");
                                String member_since = recipe.getString("member_since");
                                chefsArrayList.add(new Chef(idStr,email,username,gender,vegetarian,avatar_url,paypal,trusted,facebook,twitter,instagram,member_since, String.valueOf(recipes)));
                            }
                            trustedChefAdapter.notifyDataSetChanged();
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

    private void getConnectedChefData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/getdata", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    // Get Connected Chef Data
                    int id = jsonObject.getInt("id");
                    String idStr = String.valueOf(id);
                    String userName = jsonObject.getString("username");
                    String gender = jsonObject.getString("gender");
                    String vegetarian = jsonObject.getString("vegetarian");
                    String avatar_url = jsonObject.getString("avatar_url");
                    String paypal = jsonObject.getString("paypal");
                    String trusted = jsonObject.getString("trusted");
                    String facebook = jsonObject.getString("facebook");
                    String twitter = jsonObject.getString("twitter");
                    String instagram = jsonObject.getString("instagram");
                    String member_since =jsonObject.getString("member_since");
                    // Register All User Data In Shared Prefs
                    userIdSharedPrefs.edit().putString("userIdSharedPrefs", idStr).apply();
                    userUsernameSharedPrefs.edit().putString("userUsernameSharedPrefs", userName).apply();
                    userGenderSharedPrefs.edit().putString("userGenderSharedPrefs", gender).apply();
                    userVegetarianSharedPrefs.edit().putString("userVegetarianSharedPrefs", vegetarian).apply();
                    userAvatarUrlSharedPrefs.edit().putString("userAvatarUrlSharedPrefs", avatar_url).apply();
                    userPaypalSharedPrefs.edit().putString("userPaypalSharedPrefs", paypal).apply();
                    userTrustedSharedPrefs.edit().putString("userTrustedSharedPrefs", trusted).apply();
                    userFacebookSharedPrefs.edit().putString("userFacebookSharedPrefs", facebook).apply();
                    userTwitterSharedPrefs.edit().putString("userTwitterSharedPrefs", twitter).apply();
                    userInstagramSharedPrefs.edit().putString("userInstagramSharedPrefs", instagram).apply();
                    userMemberSince.edit().putString("userMemberSince", member_since).apply();
                    // Set Header User Info
                    getPopularCategories();
                    currentUserNameHeader.setText(userName);
                    Picasso.get().load(avatar_url).fit().centerInside().into(profileImage);
                    Picasso.get().load(avatar_url).fit().centerInside().into(currentProfileImageHeader);
                } catch(JSONException e){
                    Log.e("Error ", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmailSharedPrefs.getString("userEmailSharedPrefs", ""));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getMeals() {
        String test = url+"/api/meals/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, test, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                int id = recipe.getInt("id");
                                String name = recipe.getString("name");
                                String image = recipe.getString("image");
                                mealsArrayList.add(new Meal(String.valueOf(id),name,image));
                            }
                            mealsAdapter.notifyDataSetChanged();
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

    private void getArticles() {
        String test = url+"/api/articles/latest";
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

    private void getCuisines() {
        String test = url+"/api/cuisines/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, test, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                int id = recipe.getInt("id");
                                String name = recipe.getString("cuisine_name");
                                String image = recipe.getString("cuisine_image");
                                cuisinesArrayList.add(new Cuisine(String.valueOf(id),name,image));
                            }
                            cuisinesAdapter.notifyDataSetChanged();
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
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void getLatestRecipes() {
        String urlApi = url+"/api/recipes/latest/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","");
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
                                recipesPagerList.add(new Recipe(idStr,title,description,review,time,servings,calories,image_url,video_url,ratingStr,category_id_str,category_name, category_image,
                                        chef_id_str,chef_username, chef_image, chef_trusted, chef_paypal, chef_email, vegetarian_or_not, meal_id_str,meal_name, meal_image,
                                        cuisine_id_str, cuisine_name, cuisine_image, views, chef_gender, chef_vegetarian, chef_facebook, chef_twitter, chef_instagram, chef_member_since));
                            }
                            recipesMainViewPagerAdapter.notifyDataSetChanged();
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

    private void getPopularRecipes() {
        String urlApi = url+"/api/recipes/popular/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","");
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
                                int views = recipe.getInt("views");
                                String chef_paypal = recipe.getString("paypal");
                                String chef_email = recipe.getString("email");
                                String chef_gender = recipe.getString("gender");
                                String chef_vegetarian = recipe.getString("vegetarian");
                                String chef_facebook = recipe.getString("facebook");
                                String chef_twitter = recipe.getString("twitter");
                                String chef_instagram = recipe.getString("instagram");
                                String chef_member_since = recipe.getString("member_since");
                                recipesArrayList.add(new Recipe(idStr,title,description,review,time,servings,calories,image_url,video_url,ratingStr,category_id_str,category_name, category_image,
                                        chef_id_str,chef_username, chef_image, chef_trusted, chef_paypal, chef_email, vegetarian_or_not, meal_id_str,meal_name, meal_image,
                                        cuisine_id_str, cuisine_name, cuisine_image, views, chef_gender, chef_vegetarian, chef_facebook, chef_twitter, chef_instagram, chef_member_since));
                            }
                            popularRecipesAdapter.notifyDataSetChanged();
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

    private void getQuickRecipes() {
        String urlApi = url+"/api/recipes/quick/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","");
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

    private void getPopularCategories() {
        String urlApi = url+"/api/categories/popular/"+userIdSharedPrefs.getString("userIdSharedPrefs","");
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
                                int recipes = recipe.getInt("recipes");
                                String categoryName = recipe.getString("category_name");
                                String categoryImage = recipe.getString("category_image");
                                String vegetarian_or_not = recipe.getString("vegetarian_or_not");
                                String popular = recipe.getString("popular");
                                categoriesArrayList.add(new Category(idStr,categoryName, categoryImage, popular, vegetarian_or_not, recipes));
                            }
                            popularCategoriesAdapter.notifyDataSetChanged();
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

    public void smartRating() {
        rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.smart_rate_dialog);
                rankDialog.setCancelable(true);
                ratingBar = (SquareRatingView)rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(0);
                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(getResources().getString(R.string.rate_us));
                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ratingBar.getRating()>=3) {
                            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(goToMarket);
                                rankDialog.cancel();
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.this, "Couldn't launch the market", Toast.LENGTH_LONG).show();
                                rankDialog.cancel();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Thank you for your rating!", Toast.LENGTH_SHORT).show();
                            rankDialog.cancel();
                        }
                    }
                });
                rankDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile :
                Intent pro = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(pro);
                break;
            case R.id.favorites :
                Intent fav = new Intent(MainActivity.this, MyFavoritesRecipes.class);
                startActivity(fav);
                break;
            case R.id.recipes :
                Intent recipes = new Intent(MainActivity.this, AllRecipesActivity.class);
                startActivity(recipes);
                break;
            case R.id.shopping_list :
                Intent shop = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(shop);
                break;
            case R.id.categories :
                Intent allcat = new Intent(MainActivity.this, AllCategoriesActivity.class);
                startActivity(allcat);
                break;
            case R.id.articles :
                Intent article = new Intent(MainActivity.this, AllArticlesActivity.class);
                startActivity(article);
                break;
            case R.id.terms_of_use :
                Intent terms = new Intent(MainActivity.this, TermsOfUseActivity.class);
                startActivity(terms);
                break;
            case R.id.privacy :
                Intent privacy = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                startActivity(privacy);
                break;
            case R.id.report :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.menu_report_a_bug));
                builder.setMessage(getResources().getString(R.string.menu_report_a_bug_message));
                builder.setPositiveButton("Send Email",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                                emailSelectorIntent.setData(Uri.parse("mailto:"));
                                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.menu_report_a_bug));
                                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                emailIntent.setSelector( emailSelectorIntent );
                                if( emailIntent.resolveActivity(getPackageManager()) != null )
                                    startActivity(emailIntent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.share :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Download this APP From : http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Share Now"));
                break;
            case R.id.contact_us :
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );
                if( emailIntent.resolveActivity(getPackageManager()) != null )
                    startActivity(emailIntent);
                break;
            case R.id.rate :
                smartRating();
                break;
            case R.id.exit :
                finishAndRemoveTask();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
                new AlertDialog.Builder(this)
                        .setIcon(getResources().getDrawable(R.drawable.ic_exit))
                        .setTitle(getResources().getString(R.string.exit))
                        .setMessage(getResources().getString(R.string.really_exit))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
                                finishAndRemoveTask();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.options_search).getActionView();

        assert manager != null;
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // Toast.makeText(MainActivity.this, ""+query, Toast.LENGTH_SHORT).show();
                Intent searchIntent = new Intent(MainActivity.this, SearchRecipesActivity.class);
                searchIntent.putExtra("term", query);
                startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_rate:
                smartRating();
                return true;
            case R.id.options_search:
                // Search
                return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}