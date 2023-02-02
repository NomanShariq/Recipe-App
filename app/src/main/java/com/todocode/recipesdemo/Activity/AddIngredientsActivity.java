package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.todocode.recipesdemo.Adapter.IngredientsAdapter;
import com.todocode.recipesdemo.Model.Ingredient;
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

public class AddIngredientsActivity extends AppCompatActivity {
    private Button addIngredient, next;
    private String ingredient, quantity;
    public IngredientsAdapter ingredientsAdapter;
    public RecyclerView ingredientsRecyclerView;
    public List<Ingredient> ingredientsArrayList;
    private RequestQueue queue;
    private  String url;
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
        setContentView(R.layout.activity_add_ingredients);
        Toolbar toolbar = findViewById(R.id.add_ingredient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_ingredients));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addIngredient = (Button) findViewById(R.id.add_ingredient_btn);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        next = (Button) findViewById(R.id.next);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddIngredientsActivity.this);
                alertDialog.setTitle("Add Ingredient");
                LinearLayout layout = new LinearLayout(AddIngredientsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText quantityEd = new EditText(AddIngredientsActivity.this);
                final EditText ingredientEd = new EditText(AddIngredientsActivity.this);
                quantityEd.setBackgroundResource(R.drawable.edit_text_style);
                ingredientEd.setBackgroundResource(R.drawable.edit_text_style);
                quantityEd.setHint("Enter quantity ..");
                ingredientEd.setHint("Enter ingredient name ..");
                quantityEd.setTextColor(getResources().getColor(R.color.black));
                ingredientEd.setTextColor(getResources().getColor(R.color.black));
                quantityEd.setTextSize(14);
                ingredientEd.setTextSize(14);
                quantityEd.setPadding(20, 20, 20, 20);
                ingredientEd.setPadding(20, 20, 20, 20);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(20, 20, 20, 20);
                quantityEd.setLayoutParams(lp);
                ingredientEd.setLayoutParams(lp);
                layout.addView(quantityEd);
                layout.addView(ingredientEd);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ingredient = ingredientEd.getText().toString();
                        quantity = quantityEd.getText().toString();
                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.domain_name) + "/api/ingredients/add", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ingredientsArrayList.clear();
                                ingredientsAdapter.notifyDataSetChanged();
                                getIngredients();
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
                                params.put("quantity", quantity);
                                params.put("ingredient", ingredient);
                                params.put("recipe_id", getIntent().getStringExtra("id"));
                                //Log.e("quantity", quantity);
                                //Log.e("ingredient", ingredient);
                                //Log.e("recipe_id", getIntent().getStringExtra("recipe_id"));
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(AddIngredientsActivity.this);
                        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                                10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                        requestQueue.add(stringRequest2);
                    }
                });
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        // List of Ingredients
        ingredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler);
        ingredientsArrayList = new ArrayList<>();
        ingredientsAdapter = new IngredientsAdapter(this, ingredientsArrayList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ingredientsRecyclerView.setHasFixedSize(true);
        // Next Button Add Steps
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent steps = new Intent(AddIngredientsActivity.this, AddStepsActivity.class);
                steps.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(steps);
                finish();
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(AddIngredientsActivity.this, getString(R.string.admob_app_id));
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
                            next.setVisibility(View.VISIBLE);
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
}