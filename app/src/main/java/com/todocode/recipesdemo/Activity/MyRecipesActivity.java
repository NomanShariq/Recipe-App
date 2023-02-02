package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Adapter.MyRecipesAdapter;
import com.todocode.recipesdemo.Adapter.PopularRecipesMainActivityAdapter;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyRecipesActivity extends AppCompatActivity {
    public ArrayList<Recipe> myRecipesArrayList;
    public MyRecipesAdapter MyRecipesAdapter;
    public String url;
    public TextView nada;
    public SharedPreferences userEmailSharedPrefs;
    public RequestQueue queue;
    // Bottom Banner Ads Start
    private SharedPreferences admobBanner, facebookBanner, bannerType;
    private String bannerBottomTypeStr;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    // Bottom Banner Ads End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        Toolbar toolbar = findViewById(R.id.my_recipes_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_recipes));
        nada = (TextView) findViewById(R.id.nada);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        // Get Quick Recipes
        RecyclerView myRecipesRecyclerView = (RecyclerView) findViewById(R.id.my_recipes_recycler);
        myRecipesArrayList = new ArrayList<>();
        MyRecipesAdapter = new MyRecipesAdapter(this, myRecipesArrayList);
        myRecipesRecyclerView.setAdapter(MyRecipesAdapter);
        myRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getMyRecipes();
        MyRecipesAdapter.setOnItemClickListener(new MyRecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(MyRecipesActivity.this, SingleRecipeActivity.class);
                recipe.putExtra("id", myRecipesArrayList.get(position).getId());
                recipe.putExtra("chef_paypal", myRecipesArrayList.get(position).getChef_paypal());
                recipe.putExtra("chef_email", myRecipesArrayList.get(position).getChef_email());
                recipe.putExtra("title", myRecipesArrayList.get(position).getTitle());
                recipe.putExtra("description", myRecipesArrayList.get(position).getDescription());
                recipe.putExtra("time", myRecipesArrayList.get(position).getTime());
                recipe.putExtra("servings", myRecipesArrayList.get(position).getServings());
                recipe.putExtra("calories", myRecipesArrayList.get(position).getCalories());
                recipe.putExtra("image_url", myRecipesArrayList.get(position).getImage_url());
                recipe.putExtra("video_url", myRecipesArrayList.get(position).getVideo_url());
                recipe.putExtra("rating", myRecipesArrayList.get(position).getRating());
                recipe.putExtra("category_id", myRecipesArrayList.get(position).getCategory_id());
                recipe.putExtra("category_name", myRecipesArrayList.get(position).getCategory_name());
                recipe.putExtra("chef_username", myRecipesArrayList.get(position).getChef_username());
                recipe.putExtra("chef_id", myRecipesArrayList.get(position).getChef_id());
                recipe.putExtra("chef_image", myRecipesArrayList.get(position).getChef_image());
                recipe.putExtra("chef_trusted", myRecipesArrayList.get(position).getChef_trusted());
                recipe.putExtra("meal_name", myRecipesArrayList.get(position).getMeal_name());
                recipe.putExtra("paypal", myRecipesArrayList.get(position).getChef_paypal());
                recipe.putExtra("email", myRecipesArrayList.get(position).getChef_email());
                recipe.putExtra("cuisine_name", myRecipesArrayList.get(position).getCuisine_name());
                recipe.putExtra("views", myRecipesArrayList.get(position).getViews());
                recipe.putExtra("gender", myRecipesArrayList.get(position).getChef_gender());
                recipe.putExtra("vegetarian", myRecipesArrayList.get(position).getChef_vegetarian());
                recipe.putExtra("facebook", myRecipesArrayList.get(position).getChef_facebook());
                recipe.putExtra("twitter", myRecipesArrayList.get(position).getChef_twitter());
                recipe.putExtra("instagram", myRecipesArrayList.get(position).getChef_instagram());
                recipe.putExtra("member_since", myRecipesArrayList.get(position).getChef_member_since());
                startActivity(recipe);
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(MyRecipesActivity.this, getString(R.string.admob_app_id));
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

    public void getMyRecipes() {
        String urlApi = url+"/api/recipes/mine/"+userEmailSharedPrefs.getString("userEmailSharedPrefs","");
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
                                myRecipesArrayList.add(new Recipe(idStr,title,description,review,time,servings,calories,image_url,video_url,ratingStr,category_id_str,category_name, category_image,
                                        chef_id_str,chef_username, chef_image, chef_trusted, chef_paypal, chef_email, vegetarian_or_not, meal_id_str,meal_name, meal_image,
                                        cuisine_id_str, cuisine_name, cuisine_image, views, chef_gender, chef_vegetarian, chef_facebook, chef_twitter, chef_instagram, chef_member_since));
                            }
                            MyRecipesAdapter.notifyDataSetChanged();
                            if (myRecipesArrayList.size()==0) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteMyRecipe(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/recipes/mine/delete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("1")) {
                        Toast.makeText(MyRecipesActivity.this, "Recipe Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    }
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
                params.put("id", id);
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
}