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
import com.todocode.recipesdemo.Model.Meal;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MealsAdapter extends  RecyclerView.Adapter<MealsAdapter.MealsHolder> {

    private Context context;
    private List<Meal> meals;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class MealsHolder extends RecyclerView.ViewHolder {
        private TextView mealTitle;
        private ImageView mealImage;

        public MealsHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mealTitle = (TextView) itemView.findViewById(R.id.mealTitle);
            mealImage = (ImageView) itemView.findViewById(R.id.mealImage);
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
        public void setDetails(Meal meal) {
            mealTitle.setText(meal.getName());
            String imageUrl = context.getResources().getString(R.string.domain_name)+"/uploads/meals/"+meal.getImageUrl();
            Picasso.get().load(imageUrl).fit().centerInside().into(mealImage);
        }
    }

    public MealsAdapter(Context context, List<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_meal_layout_main, parent, false);
        return new MealsHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.setDetails(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

}


