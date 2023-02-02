package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Activity.MyFavoritesRecipes;
import com.todocode.recipesdemo.Activity.ShoppingListActivity;
import com.todocode.recipesdemo.Activity.SingleRecipeActivity;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesAdapter extends  RecyclerView.Adapter<FavoritesAdapter.RecipesHolder> {

    private Context context;
    private List<Recipe> recipes;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class RecipesHolder extends RecyclerView.ViewHolder {
        private TextView title, chef, totalTime, recipeViews, recipeCategoryName;
        private ImageView recipeimage;
        private CircleImageView chefImage;
        private LinearLayout remove;

        public RecipesHolder(@NonNull View itemView, final FavoritesAdapter.OnItemClickListener recipesListener) {
            super(itemView);
            remove = (LinearLayout) itemView.findViewById(R.id.delete_linear);
            title = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeCategoryName = (TextView) itemView.findViewById(R.id.recipeCategoryName);
            chefImage = (CircleImageView) itemView.findViewById(R.id.chefImage);
            recipeimage = (ImageView) itemView.findViewById(R.id.recipe_image);
            totalTime = (TextView) itemView.findViewById(R.id.recipeTime);
            chef = (TextView) itemView.findViewById(R.id.chefName);
            recipeViews = (TextView) itemView.findViewById(R.id.recipe_views);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipesListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recipesListener.onItemClick(position);
                        }
                    }
                }
            });
        }
        public void setDetails(final Recipe recipe) {
            title.setText(recipe.getTitle());
            chef.setText(recipe.getChef_username());
            totalTime.setText(recipe.getTime());
            recipeViews.setText(String.valueOf(recipe.getViews()));
            recipeCategoryName.setText(recipe.getCategory_name());
            //final String fullUrl = context.getString(R.string.domain_name)+"/uploads/recipes/"+recipe.getImage_url();
            Picasso.get().load(recipe.getImage_url()).fit().centerInside().into(recipeimage);
            Picasso.get().load(recipe.getChef_image()).fit().centerInside().into(chefImage);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.domain_name) + "/api/favorites/remove", new Response.Listener<String>() {
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
                                Toast.makeText(context, "Recipe Removed From Favorites!", Toast.LENGTH_SHORT).show();
                                ((MyFavoritesRecipes)context).quickArrayList.clear();
                                ((MyFavoritesRecipes)context).quickRecipesAdapter.notifyDataSetChanged();
                                ((MyFavoritesRecipes)context).getFavoritesRecipes();
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
                            params.put("recipe_id", recipe.getId());
                            Log.e("recipe_id", recipe.getId());
                            SharedPreferences userIdSharedPrefs;
                            userIdSharedPrefs = context.getSharedPreferences("userIdSharedPrefs", Context.MODE_PRIVATE);
                            params.put("chef_id", userIdSharedPrefs.getString("userIdSharedPrefs", ""));
                            Log.e("chef_id", userIdSharedPrefs.getString("userIdSharedPrefs", ""));
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

    public FavoritesAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_favorite_recipe_layout, parent, false);
        return new RecipesHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.setDetails(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}


