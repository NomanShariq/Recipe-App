package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.todocode.recipesdemo.Adapter.AddIngredientsAdapter;
import com.todocode.recipesdemo.Adapter.RemoveIngredientsAdapter;
import com.todocode.recipesdemo.Model.Ingredient;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShoppingListActivity extends AppCompatActivity {
    private TextView ingredientsNumber;
    private String url;
    private RequestQueue queue;
    private SharedPreferences userIdSharedPrefs;
    public RemoveIngredientsAdapter ingredientsAdapter;
    public RecyclerView ingredientsRecyclerView;
    public List<Ingredient> ingredientsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = findViewById(R.id.shopping_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.drawer_menu_shopping_list));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        ingredientsNumber = (TextView) findViewById(R.id.titles);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        // List of Ingredients
        ingredientsRecyclerView = (RecyclerView) findViewById(R.id.shopping_recycler);
        ingredientsArrayList = new ArrayList<>();
        ingredientsAdapter = new RemoveIngredientsAdapter(this, ingredientsArrayList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ingredientsRecyclerView.setHasFixedSize(true);
        getShoppings();
    }

    public void getShoppings() {
        final String chefId = userIdSharedPrefs.getString("userIdSharedPrefs", "");
        String apiUrl = url+"/api/shoppings/"+chefId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
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
                            ingredientsNumber.setText(String.valueOf(ingredientsArrayList.size())+ " Ingredients");
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