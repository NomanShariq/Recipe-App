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
import com.todocode.recipesdemo.Model.Ingredient;
import com.todocode.recipesdemo.Model.Meal;
import com.todocode.recipesdemo.R;

import java.util.List;

public class IngredientsAdapter extends  RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {

    private Context context;
    private List<Ingredient> ingredients;


    public class IngredientsHolder extends RecyclerView.ViewHolder {
        private TextView quantity, name;

        public IngredientsHolder(@NonNull View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
            name = (TextView) itemView.findViewById(R.id.ingredient_name);
        }
        public void setDetails(Ingredient ingredient) {
            quantity.setText(ingredient.getQuantity());
            name.setText(ingredient.getIngredient());
        }
    }

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_ingredient_layout, parent, false);
        return new IngredientsAdapter.IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientsHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.setDetails(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}


