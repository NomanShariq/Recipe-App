package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Adapter.AddIngredientsAdapter;
import com.todocode.recipesdemo.Adapter.IngredientsAdapter;
import com.todocode.recipesdemo.Adapter.PopularRecipesMainActivityAdapter;
import com.todocode.recipesdemo.Adapter.StepsAdapter;
import com.todocode.recipesdemo.Adapter.StepsListAdapter;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Model.Ingredient;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.Model.Step;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleRecipeActivity extends AppCompatActivity {
    private KenBurnsView recipeImage;
    private ImageView single_recipe_video;
    private TextView category, meal, cuisine, recipe_title, chef_name,
            rate_num, number_of_ratings, minutes, servings, calories, description;
    private RecyclerView ingredients_recycler, steps_recycler;
    private RatingBar ratingBar;
    private ir.icegroup.curvedratingbar.RatingBar rating;
    private String url;
    private CircleImageView chefImage;
    public AddIngredientsAdapter ingredientsAdapter;
    public RecyclerView ingredientsRecyclerView;
    public List<Ingredient> ingredientsArrayList;
    private RequestQueue queue;
    private String step, number;
    public StepsListAdapter stepsAdapter;
    public RecyclerView stepsRecyclerView;
    public List<Step> stepsArrayList;
    public MenuItem liked, like, rateItem;
    public Dialog rankDialog;
    private ArrayList<Recipe> quickArrayList;
    private PopularRecipesMainActivityAdapter quickRecipesAdapter;
    public SharedPreferences userEmailSharedPrefs, userPaypalSharedPrefs,userIdSharedPrefs;
    private LinearLayout goToChef;
    private Button donate;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        Toolbar toolbar = findViewById(R.id.single_recipe_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        userPaypalSharedPrefs = getSharedPreferences("userPaypalSharedPrefs", MODE_PRIVATE);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        if (userIdSharedPrefs.getString("userIdSharedPrefs", "").equals(getIntent().getStringExtra("chef_id"))) {
        } else {
            addViewToRecipe();
        }
        facebookNative = getSharedPreferences("facebookNative",MODE_PRIVATE);
        // Facebook Ads Native
        nativeAdLayout = findViewById(R.id.fb_native_banner_ad_container);
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("1aa71c93-1bcf-4079-b2a2-8d0cee9ebf77");
        loadNativeAd();
        chefImage = (CircleImageView) findViewById(R.id.chef_image);
        Picasso.get().load(getIntent().getStringExtra("chef_image")).fit().centerInside().into(chefImage);
        recipeImage = (KenBurnsView) findViewById(R.id.recipeImage);
        String imageUrl = getIntent().getStringExtra("image_url");
        Picasso.get().load(imageUrl).fit().centerInside().into(recipeImage);
        single_recipe_video = (ImageView) findViewById(R.id.single_recipe_video);
        //String video = getIntent().getStringExtra("video_url");
        if (getIntent().getStringExtra("video_url")!=null){
            if (getIntent().getStringExtra("video_url").contains("youtube.com")) {
                single_recipe_video.setVisibility(View.VISIBLE);
                single_recipe_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( getIntent().getStringExtra("video_url") ) );
                        startActivity( browse );
                    }
                });
            }
        }
        category = (TextView) findViewById(R.id.category);
        category.setText(getIntent().getStringExtra("category_name"));
        meal = (TextView) findViewById(R.id.meal);
        meal.setText(getIntent().getStringExtra("meal_name"));
        cuisine = (TextView) findViewById(R.id.cuisine);
        cuisine.setText(getIntent().getStringExtra("cuisine_name"));
        recipe_title = (TextView) findViewById(R.id.recipe_title);
        recipe_title.setText(getIntent().getStringExtra("title"));
        chef_name = (TextView) findViewById(R.id.chef_name);
        chef_name.setText(getIntent().getStringExtra("chef_username"));
        number_of_ratings = (TextView) findViewById(R.id.number_of_ratings);
        minutes = (TextView) findViewById(R.id.minutes);
        minutes.setText(getIntent().getStringExtra("time")+" MIN");
        servings = (TextView) findViewById(R.id.servings);
        servings.setText(getIntent().getStringExtra("servings")+ " PERS");
        calories = (TextView) findViewById(R.id.calories);
        calories.setText(getIntent().getStringExtra("calories") + " CAL");
        description = (TextView) findViewById(R.id.description);
        description.setText(getIntent().getStringExtra("description"));
        ingredients_recycler = (RecyclerView) findViewById(R.id.ingredients_recycler);
        steps_recycler = (RecyclerView) findViewById(R.id.steps_recycler);
        rating = (ir.icegroup.curvedratingbar.RatingBar) findViewById(R.id.recipe_rating);
        rate_num = (TextView) findViewById(R.id.recipe_rate_num);
        Double rate = Double.parseDouble(getIntent().getStringExtra("rating"));
        int IntValue = (int) rate.doubleValue();
        rating.setStar(IntValue);
        rate_num.setText(getIntent().getStringExtra("rating"));
        // List of Ingredients
        ingredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_single);
        ingredientsArrayList = new ArrayList<>();
        ingredientsAdapter = new AddIngredientsAdapter(this, ingredientsArrayList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ingredientsRecyclerView.setHasFixedSize(true);
        getIngredients();
        // List of Steps
        stepsRecyclerView = (RecyclerView) findViewById(R.id.steps_recycler_single);
        stepsArrayList = new ArrayList<>();
        stepsAdapter = new StepsListAdapter(this, stepsArrayList);
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        stepsRecyclerView.setHasFixedSize(true);
        getSteps();
        // Get Quick Recipes
        RecyclerView quickRecipesRecyclerView = (RecyclerView) findViewById(R.id.related_recipes_recycler);
        quickArrayList = new ArrayList<>();
        quickRecipesAdapter = new PopularRecipesMainActivityAdapter(this, quickArrayList);
        quickRecipesRecyclerView.setAdapter(quickRecipesAdapter);
        quickRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getRelatedRecipes();
        quickRecipesAdapter.setOnItemClickListener(new PopularRecipesMainActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(SingleRecipeActivity.this, SingleRecipeActivity.class);
                recipe.putExtra("id", quickArrayList.get(position).getId());
                recipe.putExtra("title", quickArrayList.get(position).getTitle());
                recipe.putExtra("description", quickArrayList.get(position).getDescription());
                recipe.putExtra("time", quickArrayList.get(position).getTime());
                recipe.putExtra("servings", quickArrayList.get(position).getServings());
                recipe.putExtra("calories", quickArrayList.get(position).getCalories());
                recipe.putExtra("image_url", quickArrayList.get(position).getImage_url());
                recipe.putExtra("video_url", quickArrayList.get(position).getVideo_url());
                recipe.putExtra("rating", quickArrayList.get(position).getRating());
                recipe.putExtra("category_name", quickArrayList.get(position).getCategory_name());
                recipe.putExtra("chef_username", quickArrayList.get(position).getChef_username());
                recipe.putExtra("chef_id", quickArrayList.get(position).getChef_id());
                recipe.putExtra("chef_image", quickArrayList.get(position).getChef_image());
                recipe.putExtra("chef_trusted", quickArrayList.get(position).getChef_trusted());
                recipe.putExtra("paypal", quickArrayList.get(position).getChef_paypal());
                recipe.putExtra("email", quickArrayList.get(position).getChef_email());
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
        getRatingsNumber();
        goToChef = (LinearLayout) findViewById(R.id.go_to_chef_profile);
        goToChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chef = new Intent(SingleRecipeActivity.this, SingleChefActivity.class);
                chef.putExtra("paypal", getIntent().getStringExtra("paypal"));
                chef.putExtra("email", getIntent().getStringExtra("email"));
                chef.putExtra("id", getIntent().getStringExtra("chef_id"));
                chef.putExtra("username", getIntent().getStringExtra("chef_username"));
                chef.putExtra("gender", getIntent().getStringExtra("gender"));
                chef.putExtra("vegetarian", getIntent().getStringExtra("vegetarian"));
                chef.putExtra("image", getIntent().getStringExtra("chef_image"));
                chef.putExtra("trusted", getIntent().getStringExtra("chef_trusted"));
                chef.putExtra("facebook", getIntent().getStringExtra("facebook"));
                chef.putExtra("twitter", getIntent().getStringExtra("twitter"));
                chef.putExtra("instagram", getIntent().getStringExtra("instagram"));
                chef.putExtra("member_since", getIntent().getStringExtra("member_since"));
                startActivity(chef);
                finish();
            }
        });
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
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(SingleRecipeActivity.this, getString(R.string.admob_app_id));
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

    private void addViewToRecipe() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.domain_name) + "/api/recipes/view/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String success = null;
                try {
                    success = jsonObject.getString("success");
                } catch (JSONException e) {
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
                params.put("id", getIntent().getStringExtra("id"));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SingleRecipeActivity.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest2);
    }

    private void getRatingsNumber() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.domain_name) + "/api/ratings/number", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String success = null;
                try {
                    success = jsonObject.getString("success");
                    number_of_ratings.setText("("+success + " Ratings)");
                } catch (JSONException e) {
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
                params.put("recipe_id", getIntent().getStringExtra("id"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SingleRecipeActivity.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest2);
    }

    private void getRelatedRecipes() {
        String urlApi = url+"/api/recipes/related/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","")+"/"+getIntent().getStringExtra("category_id")+"/"+getIntent().getStringExtra("id");
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

    private void getIngredients() {
        final String recipeId = getIntent().getStringExtra("id");
        String cuisinesApiUrl = url+"/api/ingredients/"+recipeId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cuisinesApiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisine = jsonArray.getJSONObject(i);
                                int Ingid = cuisine.getInt("id");
                                String idStr = String.valueOf(Ingid);
                                String ingredient = cuisine.getString("ingredient");
                                String quantity = cuisine.getString("quantity");
                                int recipeId = cuisine.getInt("recipe_id");
                                String id = String.valueOf(recipeId);
                                ingredientsArrayList.add(new Ingredient(idStr, ingredient, quantity, id));
                            }
                            ingredientsAdapter.notifyDataSetChanged();
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

    private void getSteps() {
        final String recipeId = getIntent().getStringExtra("id");
        String cuisinesApiUrl = url+"/api/steps/"+recipeId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cuisinesApiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisine = jsonArray.getJSONObject(i);
                                int Ingid = cuisine.getInt("id");
                                String idStr = String.valueOf(Ingid);
                                String description = cuisine.getString("description");
                                String step_number = cuisine.getString("step_number");
                                int recipeId = cuisine.getInt("recipe_id");
                                String id = String.valueOf(recipeId);
                                stepsArrayList.add(new Step(idStr, description, step_number, id));
                            }
                            stepsAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.single_recipe_menu, menu);
        liked = menu.findItem(R.id.liked);
        like = menu.findItem(R.id.like);
        rateItem = menu.findItem(R.id.rate);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Prepare a " + getIntent().getStringExtra("title") + " : http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_recipe)));
                return true;
            case R.id.rate:
                rankDialog = new Dialog(SingleRecipeActivity.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(0);
                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(getResources().getString(R.string.rate_recipe));
                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(SingleRecipeActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                        AddRatingToRecipe();
                        rankDialog.cancel();
                        rateItem.setVisible(false);
                    }
                });
                rankDialog.show();
                return true;
            case R.id.like:
                addToFav();
                item.setVisible(false);
                liked.setVisible(true);
                return true;
            case R.id.liked:
                //removeFromFav();
                item.setVisible(false);
                like.setVisible(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddRatingToRecipe() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.domain_name) + "/api/ratings/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String success = null;
                try {
                    success = jsonObject.getString("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // No Errors
                if (success.equals("1")) {
                    Toast.makeText(SingleRecipeActivity.this, "Rating added to recipe!", Toast.LENGTH_SHORT).show();
                    getRatingsNumber();
                }
                else if(success.equals("2")) {
                    Toast.makeText(SingleRecipeActivity.this, "Recipe already rated!", Toast.LENGTH_SHORT).show();
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
                params.put("recipe_id", getIntent().getStringExtra("id"));
                params.put("chef_id", userIdSharedPrefs.getString("userIdSharedPrefs", ""));
                params.put("rating", String.valueOf(ratingBar.getRating()));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SingleRecipeActivity.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest2);
    }

    private void addToFav() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.domain_name) + "/api/favorites/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String success = null;
                try {
                    success = jsonObject.getString("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // No Errors
                if (success.equals("1")) {
                    Toast.makeText(SingleRecipeActivity.this, "Recipe Added To Favorites!", Toast.LENGTH_SHORT).show();
                }
                else if(success.equals("2")) {
                    Toast.makeText(SingleRecipeActivity.this, "Recipe Already in Favorites!", Toast.LENGTH_SHORT).show();
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
                params.put("recipe_id", getIntent().getStringExtra("id"));
                params.put("chef_id", userIdSharedPrefs.getString("userIdSharedPrefs", ""));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SingleRecipeActivity.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest2);
    }

}