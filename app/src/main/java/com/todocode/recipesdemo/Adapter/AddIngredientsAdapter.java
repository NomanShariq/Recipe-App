package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todocode.recipesdemo.Activity.AddStepsActivity;
import com.todocode.recipesdemo.Activity.SingleRecipeActivity;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Model.Ingredient;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddIngredientsAdapter extends  RecyclerView.Adapter<AddIngredientsAdapter.IngredientsHolder> {

    private Context context;
    private List<Ingredient> ingredients;


    public class IngredientsHolder extends RecyclerView.ViewHolder {
        private TextView quantity, name;
        private ImageView addToShoppingList;

        public IngredientsHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.ingredient_name);
            addToShoppingList = (ImageView) itemView.findViewById(R.id.add_ingredient_btn);
        }
        public void setDetails(final Ingredient ingredient) {
            name.setText(ingredient.getQuantity()+ " | "+ ingredient.getIngredient());
            addToShoppingList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.domain_name) + "/api/shoppings/add", new Response.Listener<String>() {
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
                                Toast.makeText(context, "Ingredient Added To Shopping List!", Toast.LENGTH_SHORT).show();
                                addToShoppingList.setVisibility(View.GONE);
                            }
                            else if (success.equals("2")) {
                                Toast.makeText(context, "Ingredient Already In Shopping List!", Toast.LENGTH_SHORT).show();
                                addToShoppingList.setVisibility(View.GONE);
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
                            params.put("quantity", ingredient.getQuantity());
                            params.put("ingredient", ingredient.getIngredient());
                            params.put("recipe_id", ((SingleRecipeActivity) context).getIntent().getStringExtra("id"));
                            params.put("chef_id", ((SingleRecipeActivity) context).userIdSharedPrefs.getString("userIdSharedPrefs", ""));
                            params.put("key", KeysManager.getMd5(context.getResources().getString(R.string.my_preference_permission_key)));
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    requestQueue.add(stringRequest2);
                }
            });
        }
    }

    public AddIngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public AddIngredientsAdapter.IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_add_ingredient_layout, parent, false);
        return new AddIngredientsAdapter.IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddIngredientsAdapter.IngredientsHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.setDetails(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}



