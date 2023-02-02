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
import com.todocode.recipesdemo.Model.Meal;
import com.todocode.recipesdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrustedChefAdapter extends  RecyclerView.Adapter<TrustedChefAdapter.ChefsHolder> {

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
        private TextView chefName;
        private CircleImageView chefImage;

        public ChefsHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            chefName = (TextView) itemView.findViewById(R.id.trusted_chef_name);
            chefImage = (CircleImageView) itemView.findViewById(R.id.trusted_chef_image);
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
            Picasso.get().load(chef.getAvatar_url()).fit().centerInside().into(chefImage);
        }
    }

    public TrustedChefAdapter(Context context, List<Chef> chefs) {
        this.context = context;
        this.chefs = chefs;
    }

    @NonNull
    @Override
    public ChefsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_trusted_chef_layout, parent, false);
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



