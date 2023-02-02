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
import com.todocode.recipesdemo.Model.Chef;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllChefsAdapter extends  RecyclerView.Adapter<AllChefsAdapter.ChefsHolder> {

    private Context context;
    private List<Chef> chefs;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class ChefsHolder extends RecyclerView.ViewHolder {
        private TextView chefName, numberOfRecipes, memberSince;
        private ImageView chefImage, trusted;

        public ChefsHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            chefName = (TextView) itemView.findViewById(R.id.chefName);
            chefImage = (ImageView) itemView.findViewById(R.id.chefImage);
            trusted = (ImageView) itemView.findViewById(R.id.trusted);
            numberOfRecipes = (TextView) itemView.findViewById(R.id.number_of_recipes);
            memberSince = (TextView) itemView.findViewById(R.id.member_since);
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
        public void setDetails(Chef chef) {
            chefName.setText(chef.getUsername());
            memberSince.setText("Member since : "+chef.getMember_since());
            numberOfRecipes.setText(chef.getNumberOfRecipes()+" Recipes");
            if (chef.getTrusted().equals("yes")) {
                trusted.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(chef.getAvatar_url()).fit().centerInside().into(chefImage);
        }
    }

    public AllChefsAdapter(Context context, List<Chef> chefs) {
        this.context = context;
        this.chefs = chefs;
    }

    @NonNull
    @Override
    public ChefsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_chef_layout_for_all, parent, false);
        return new ChefsHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefsHolder holder, int position) {
        Chef chef = chefs.get(position);
        holder.setDetails(chef);
    }

    @Override
    public int getItemCount() {
        return chefs.size();
    }

}



