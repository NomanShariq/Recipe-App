package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularRecipesMainActivityAdapter extends  RecyclerView.Adapter<PopularRecipesMainActivityAdapter.RecipesHolder> {

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
        private TextView recipeCategory, recipeViews, recipeTime, recipeTitle, recipeChefName;
        private CircleImageView recipeChefImage;
        private ImageView recipeImage;

        public RecipesHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recipeCategory = (TextView) itemView.findViewById(R.id.recipeCategoryName);
            recipeViews = (TextView) itemView.findViewById(R.id.recipeViews);
            recipeTime = (TextView) itemView.findViewById(R.id.recipeTime);
            recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeChefName = (TextView) itemView.findViewById(R.id.chefName);
            recipeChefImage = (CircleImageView) itemView.findViewById(R.id.chefImage);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
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
        public void setDetails(Recipe recipe) {
            recipeCategory.setText(recipe.getCategory_name());
            recipeViews.setText(String.valueOf(recipe.getViews()));
            recipeTime.setText(recipe.getTime()+" MIN");
            recipeTitle.setText(recipe.getTitle());
            recipeChefName.setText(recipe.getChef_username());
            Picasso.get().load(recipe.getImage_url()).fit().centerInside().into(recipeImage);
            Picasso.get().load(recipe.getChef_image()).fit().centerInside().into(recipeChefImage);
        }
    }

    public PopularRecipesMainActivityAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_most_popular_recipe_layout, parent, false);
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

