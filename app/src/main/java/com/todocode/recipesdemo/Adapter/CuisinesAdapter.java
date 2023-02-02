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
import com.todocode.recipesdemo.Model.Cuisine;
import com.todocode.recipesdemo.Model.Meal;
import com.todocode.recipesdemo.R;

import java.util.List;

public class CuisinesAdapter extends  RecyclerView.Adapter<CuisinesAdapter.CuisineHolder> {

    private Context context;
    private List<Cuisine> cuisines;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class CuisineHolder extends RecyclerView.ViewHolder {
        private ImageView cuisineImage;

        public CuisineHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cuisineImage = (ImageView) itemView.findViewById(R.id.cuisineImage);
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
        public void setDetails(Cuisine cuisine) {
            String imageUrl = context.getResources().getString(R.string.domain_name)+"/uploads/cuisines/"+cuisine.getImageUrl();
            Picasso.get().load(imageUrl).fit().centerInside().into(cuisineImage);
        }
    }

    public CuisinesAdapter(Context context, List<Cuisine> cuisines) {
        this.context = context;
        this.cuisines = cuisines;
    }

    @NonNull
    @Override
    public CuisineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cuisine_layout, parent, false);
        return new CuisineHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineHolder holder, int position) {
        Cuisine cuisine = cuisines.get(position);
        holder.setDetails(cuisine);
    }

    @Override
    public int getItemCount() {
        return cuisines.size();
    }

}



