package com.todocode.recipesdemo.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesMainViewPagerAdapter extends RecyclerView.Adapter<RecipesMainViewPagerAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private RecipesMainViewPagerAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(RecipesMainViewPagerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public RecipesMainViewPagerAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recipe_slider, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.setDetails(recipes.get(position));

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private KenBurnsView recipeImage;
        private TextView recipeTitle, recipeCategory, recipeTime, chefName, cuisineName;
        private CircleImageView chefImage;

        public RecipeViewHolder(@NonNull View itemView,final RecipesMainViewPagerAdapter.OnItemClickListener listener) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCategory = itemView.findViewById(R.id.recipeCategoryName);
            recipeTime = itemView.findViewById(R.id.recipeTime);
            chefName = itemView.findViewById(R.id.chefName);
            chefImage = itemView.findViewById(R.id.chefImage);
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
        @SuppressLint("SetTextI18n")
        void setDetails(Recipe recipe) {
            Picasso.get().load(recipe.getImage_url()).into(recipeImage);
            recipeTitle.setText(recipe.getTitle());
            recipeCategory.setText(recipe.getCategory_name());
            recipeTime.setText(recipe.getTime()+" MIN");
            chefName.setText(recipe.getChef_username());
            Picasso.get().load(recipe.getChef_image()).into(chefImage);
        }
    }
}

