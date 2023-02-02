package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Activity.EditMyRecipeActivity;
import com.todocode.recipesdemo.Activity.MyRecipesActivity;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRecipesAdapter extends  RecyclerView.Adapter<MyRecipesAdapter.RecipesHolder> {

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
        private TextView recipeViews, recipeTime, recipeTitle;
        private ImageView recipeImage;
        private LinearLayout editLinear, deleteLinear;

        public RecipesHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //recipeViews = (TextView) itemView.findViewById(R.id.recipeViews);
            //recipeTime = (TextView) itemView.findViewById(R.id.recipeTime);
            recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
            editLinear = (LinearLayout) itemView.findViewById(R.id.edit_linear);
            deleteLinear = (LinearLayout) itemView.findViewById(R.id.delete_linear);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        public void setDetails(final Recipe recipe) {
            recipeTitle.setText(recipe.getTitle());
            Picasso.get().load(recipe.getImage_url()).fit().centerInside().into(recipeImage);
            deleteLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "delete "+recipe.getId(), Toast.LENGTH_SHORT).show();
                    ((MyRecipesActivity)context).deleteMyRecipe(String.valueOf(recipe.getId()));
                    ((MyRecipesActivity)context).recreate();

                }
            });
            editLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "edit "+recipe.getId(), Toast.LENGTH_SHORT).show();
                    Intent edit = new Intent(context, EditMyRecipeActivity.class);
                    edit.putExtra("title", recipe.getTitle());
                    edit.putExtra("image", recipe.getImage_url());
                    edit.putExtra("description", recipe.getDescription());
                    edit.putExtra("time", recipe.getTime());
                    edit.putExtra("servings", recipe.getServings());
                    edit.putExtra("calories", recipe.getCalories());
                    edit.putExtra("video_url", recipe.getVideo_url());
                    edit.putExtra("recipe_id", recipe.getId());
                    edit.putExtra("recipe_chef_id", recipe.getChef_id());
                    edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(edit);
                }
            });
        }
    }

    public MyRecipesAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_recipes_single_layout, parent, false);
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


