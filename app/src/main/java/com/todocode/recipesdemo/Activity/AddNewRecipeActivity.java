package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputLayout;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Manager.PermissionManager;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddNewRecipeActivity extends AppCompatActivity {
    private ImageView changePRofileImage, btnAllowPermissions, recipeImage;
    private TextInputLayout title, description, time, servings, calories, video_url;
    private String url;
    private RequestQueue queue;
    private Spinner categoriesSpinner, mealsSpinner, cuisinesSpinner, vegetarianSpinner;
    private Button next;
    private SharedPreferences userEmailSharedPrefs, userIdSharedPrefs;
    private static final int PICK_IMAGE = 1;
    Uri imageurl;
    private Bitmap bitmap;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private AwesomeValidation validator;
    private SharedPreferences recipeImageShared;
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
        setContentView(R.layout.activity_add_new_recipe);
        btnAllowPermissions = (ImageView) findViewById(R.id.allow_permission);
        if (new PermissionManager(AddNewRecipeActivity.this).checkPreference()) {
            btnAllowPermissions.setVisibility(View.GONE);
        }
        Toolbar toolbar = findViewById(R.id.add_recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_recipe_new));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        validator = new AwesomeValidation(ValidationStyle.BASIC);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        categoriesSpinner = (Spinner) findViewById(R.id.spinner_categories);
        mealsSpinner = (Spinner) findViewById(R.id.spinner_meals);
        cuisinesSpinner = (Spinner) findViewById(R.id.spinner_cuisines);
        vegetarianSpinner = (Spinner) findViewById(R.id.spinner_vegetarian);
        changePRofileImage = (ImageView) findViewById(R.id.edit_image);
        url = getResources().getString(R.string.domain_name);
        recipeImageShared = getSharedPreferences("recipeImageShared", MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);
        next = (Button) findViewById(R.id.next);
        title = (TextInputLayout) findViewById(R.id.recipe_title);
        description = (TextInputLayout) findViewById(R.id.recipe_description);
        video_url = (TextInputLayout) findViewById(R.id.video_url);
        time = (TextInputLayout) findViewById(R.id.recipe_time);
        servings = (TextInputLayout) findViewById(R.id.recipe_servings);
        calories = (TextInputLayout) findViewById(R.id.recipe_calories);
        getMeals();
        getCuisines();
        getCategories();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRules();
                if (validator.validate()) {
                    next.setVisibility(View.GONE);
                    saveRecipe();
                    validator.clear();
                }
            }
        });
        btnAllowPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        changePRofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(AddNewRecipeActivity.this, getString(R.string.admob_app_id));
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

    private void saveRecipe() {
        final String titleStr = title.getEditText().getText().toString();
        final String descriptionStr = description.getEditText().getText().toString();
        final String timeStr = time.getEditText().getText().toString();
        final String servingsStr = servings.getEditText().getText().toString();
        final String caloriesStr = calories.getEditText().getText().toString();
        final String categoryStr = categoriesSpinner.getSelectedItem().toString();
        final String cuisineStr = cuisinesSpinner.getSelectedItem().toString();
        final String mealStr = mealsSpinner.getSelectedItem().toString();
        final String chefStr = userEmailSharedPrefs.getString("userEmailSharedPrefs", "");
        final String vegStr = vegetarianSpinner.getSelectedItem().toString();
        final String videoUrl = video_url.getEditText().getText().toString();
        final String imageUrlStr = recipeImageShared.getString("recipeImageShared", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/recipes/recipe/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String recipeId = jsonObject.getString("id");
                    if (success.equals("1")) {
                        next.setVisibility(View.GONE);
                        Toast.makeText(AddNewRecipeActivity.this, "Add ingredients to this recipe", Toast.LENGTH_SHORT).show();
                        Intent ingredients = new Intent(AddNewRecipeActivity.this, AddIngredientsActivity.class);
                        ingredients.putExtra("id", recipeId);
                        startActivity(ingredients);
                        finish();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image_url", imageUrlStr);
                params.put("title", titleStr);
                params.put("description", descriptionStr);
                params.put("time", timeStr);
                params.put("servings", servingsStr);
                params.put("calories", caloriesStr);
                params.put("category", categoryStr);
                params.put("meal", mealStr);
                params.put("cuisine", cuisineStr);
                params.put("email", chefStr);
                params.put("vegetarian_or_not", vegStr);
                params.put("video_url", videoUrl);
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewRecipeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void setupRules() {
        validator.addValidation(this, R.id.recipe_title, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.recipe_description, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.recipe_time, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.recipe_servings, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.recipe_calories, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.spinner_categories, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.spinner_meals, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.spinner_cuisines, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.spinner_vegetarian, RegexTemplate.NOT_EMPTY, R.string.not_empty);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED   ) {
                    new PermissionManager(AddNewRecipeActivity.this).writePreference();
                    // Hide Image
                    btnAllowPermissions.setVisibility(View.GONE);
                    changePRofileImage.performClick();
                }
                return;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(AddNewRecipeActivity.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_WRITE_PERMISSION);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            imageurl = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageurl);
                bitmap = BitmapFactory.decodeStream(inputStream);
                recipeImage.setImageBitmap(bitmap);
                //uploadImage();
                final String imageData = imageToStr(bitmap);
                //Toast.makeText(this, imageData, Toast.LENGTH_LONG).show();
                recipeImageShared.edit().putString("recipeImageShared", imageData).apply();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToStr(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String encodedImg = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImg;
    }

    private void getCuisines() {
        String cuisinesApiUrl = url+"/api/cuisines/all";
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cuisinesApiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisine = jsonArray.getJSONObject(i);
                                String mealName = cuisine.getString("cuisine_name");
                                arrayList.add(mealName);
                            }
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cuisinesSpinner.setAdapter(arrayAdapter);
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

    private void getMeals() {
        String cuisinesApiUrl = url+"/api/meals/all";
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cuisinesApiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisine = jsonArray.getJSONObject(i);
                                String mealName = cuisine.getString("name");
                                arrayList.add(mealName);
                            }
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mealsSpinner.setAdapter(arrayAdapter);
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

    private void getCategories() {
        String cuisinesApiUrl = url+"/api/categories/all/"+userIdSharedPrefs.getString("userIdSharedPrefs", "");
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cuisinesApiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisine = jsonArray.getJSONObject(i);
                                String mealName = cuisine.getString("category_name");
                                arrayList.add(mealName);
                            }
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categoriesSpinner.setAdapter(arrayAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}