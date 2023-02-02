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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Manager.PermissionManager;
import com.todocode.recipesdemo.Model.Cuisine;
import com.todocode.recipesdemo.Model.Meal;
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

public class EditMyRecipeActivity extends AppCompatActivity {
    private ImageView changePRofileImage, btnAllowPermissions, recipeImage;
    private TextInputLayout title, description, time, servings, calories, video_url;
    private String url;
    private RequestQueue queue;
    private Spinner categoriesSpinner, mealsSpinner, cuisinesSpinner, vegetarianSpinner;
    private Button save;
    private SharedPreferences userEmailSharedPrefs,userIdSharedPrefs;
    private static final int PICK_IMAGE = 1;
    Uri imageurl;
    private Bitmap bitmap;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_recipe);
        btnAllowPermissions = (ImageView) findViewById(R.id.allow_permission);
        if (new PermissionManager(EditMyRecipeActivity.this).checkPreference()) {
            btnAllowPermissions.setVisibility(View.GONE);
        }
        Toolbar toolbar = findViewById(R.id.edit_recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_recipe));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        categoriesSpinner = (Spinner) findViewById(R.id.spinner_categories);
        mealsSpinner = (Spinner) findViewById(R.id.spinner_meals);
        cuisinesSpinner = (Spinner) findViewById(R.id.spinner_cuisines);
        vegetarianSpinner = (Spinner) findViewById(R.id.spinner_vegetarian);
        changePRofileImage = (ImageView) findViewById(R.id.edit_image);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        save = (Button) findViewById(R.id.save);
        title = (TextInputLayout) findViewById(R.id.recipe_title);
        description = (TextInputLayout) findViewById(R.id.recipe_description);
        video_url = (TextInputLayout) findViewById(R.id.video_url);
        time = (TextInputLayout) findViewById(R.id.recipe_time);
        servings = (TextInputLayout) findViewById(R.id.recipe_servings);
        calories = (TextInputLayout) findViewById(R.id.recipe_calories);
        title.getEditText().setText(getIntent().getStringExtra("title"));
        description.getEditText().setText(getIntent().getStringExtra("description"));
        time.getEditText().setText(getIntent().getStringExtra("time"));
        servings.getEditText().setText(getIntent().getStringExtra("servings"));
        calories.getEditText().setText(getIntent().getStringExtra("calories"));
        if (!getIntent().getStringExtra("video_url").equals("null")) {
            video_url.getEditText().setText(getIntent().getStringExtra("video_url"));
        }
        Picasso.get().load(getIntent().getStringExtra("image")).fit().centerInside().into(recipeImage);
        getMeals();
        getCuisines();
        getCategories();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdition();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED   ) {
                    new PermissionManager(EditMyRecipeActivity.this).writePreference();
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
            ActivityCompat.requestPermissions(EditMyRecipeActivity.this, new String[] {
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
                uploadImage();
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

    private void uploadImage() {
        final String imageData = imageToStr(bitmap);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/recipes/mine/image/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditMyRecipeActivity.this, getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
                Intent myRecipes = new Intent(EditMyRecipeActivity.this, MyRecipesActivity.class);
                myRecipes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myRecipes);
                finish();
                //Toast.makeText(EditMyRecipeActivity.this, getIntent().getStringExtra("recipe_id"), Toast.LENGTH_SHORT).show();
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
                // Check that image is added
                params.put("image", imageData);
                params.put("id", getIntent().getStringExtra("recipe_id"));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditMyRecipeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void saveEdition() {
        final String titleStr = title.getEditText().getText().toString();
        final String descriptionStr = description.getEditText().getText().toString();
        final String timeStr = time.getEditText().getText().toString();
        final String servingsStr = servings.getEditText().getText().toString();
        final String caloriesStr = calories.getEditText().getText().toString();
        final String categoryStr = categoriesSpinner.getSelectedItem().toString();
        final String mealStr = mealsSpinner.getSelectedItem().toString();
        final String cuisineStr = cuisinesSpinner.getSelectedItem().toString();
        final String vegStr = vegetarianSpinner.getSelectedItem().toString();
        final String youtubeStr = video_url.getEditText().getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/recipes/mine/edit", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        // Good
                        Toast.makeText(EditMyRecipeActivity.this, "Your recipe is edited and submitted for review!", Toast.LENGTH_SHORT).show();
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
                params.put("email", userEmailSharedPrefs.getString("userEmailSharedPrefs", ""));
                params.put("title", titleStr);
                params.put("description", descriptionStr);
                params.put("time", timeStr);
                params.put("servings", servingsStr);
                params.put("calories", caloriesStr);
                params.put("category", categoryStr);
                params.put("meal", mealStr);
                params.put("cuisine", cuisineStr);
                params.put("veg", vegStr);
                params.put("video", youtubeStr);
                params.put("recipe_id", getIntent().getStringExtra("recipe_id"));
                params.put("recipe_chef_id", getIntent().getStringExtra("recipe_chef_id"));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditMyRecipeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
        String cuisinesApiUrl = url+"/api/categories/all/"+userIdSharedPrefs.getString("userIdSharedPrefs","");
        //Log.e("api", cuisinesApiUrl);
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