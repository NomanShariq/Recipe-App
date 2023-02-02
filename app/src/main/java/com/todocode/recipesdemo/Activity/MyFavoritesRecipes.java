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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.todocode.recipesdemo.Adapter.FavoritesAdapter;
import com.todocode.recipesdemo.Adapter.PopularRecipesMainActivityAdapter;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MyFavoritesRecipes extends AppCompatActivity {
    private String url;
    private RequestQueue queue;
    public FavoritesAdapter quickRecipesAdapter;
    public ArrayList<Recipe> quickArrayList;
    private SharedPreferences userIdSharedPrefs;
    private TextView nada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites_recipes);
        Toolbar toolbar = findViewById(R.id.favorites_recipes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_favorites_recipes));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nada = (TextView) findViewById(R.id.nada);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        RecyclerView quickRecipesRecyclerView = (RecyclerView) findViewById(R.id.favorites_recipes_recycler);
        quickArrayList = new ArrayList<>();
        quickRecipesAdapter = new FavoritesAdapter(this, quickArrayList);
        quickRecipesRecyclerView.setAdapter(quickRecipesAdapter);
        quickRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getFavoritesRecipes();
        quickRecipesAdapter.setOnItemClickListener(new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent recipe = new Intent(MyFavoritesRecipes.this, SingleRecipeActivity.class);
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

    public void getFavoritesRecipes() {
        String urlApi = url+"/api/recipes/favorites/"+userIdSharedPrefs.getString("userIdSharedPrefs","");
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

}