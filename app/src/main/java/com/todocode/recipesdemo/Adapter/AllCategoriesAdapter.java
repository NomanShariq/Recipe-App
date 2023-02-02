package com.todocode.recipesdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Model.Category;
import com.todocode.recipesdemo.R;

import java.util.List;

public class AllCategoriesAdapter extends  RecyclerView.Adapter<AllCategoriesAdapter.CategoriesHolder> {

    private Context context;
    private List<Category> categories;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class CategoriesHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle, numberOfRecipes;
        private ImageView categoryImage;

        public CategoriesHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            categoryTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            numberOfRecipes = (TextView) itemView.findViewById(R.id.number_of_recipes);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
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
        public void setDetails(Category category) {
            categoryTitle.setText(category.getName());
            numberOfRecipes.setText(String.valueOf(category.getRecipes_num())+ " " + context.getResources().getString(R.string.recipes));
            String imageUrl = context.getResources().getString(R.string.domain_name)+"/uploads/categories/"+category.getImage();
            Picasso.get().load(imageUrl).fit().centerInside().into(categoryImage);
        }
    }

    public AllCategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_layout_for_all, parent, false);
        return new CategoriesHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesHolder holder, int position) {
        Category category = categories.get(position);
        holder.setDetails(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}




