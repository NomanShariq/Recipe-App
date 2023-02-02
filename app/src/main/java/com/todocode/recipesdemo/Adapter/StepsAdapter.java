package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todocode.recipesdemo.Model.Ingredient;
import com.todocode.recipesdemo.Model.Step;
import com.todocode.recipesdemo.R;

import java.util.List;

public class StepsAdapter extends  RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    private Context context;
    private List<Step> steps;


    public class StepsHolder extends RecyclerView.ViewHolder {
        private TextView number, name;

        public StepsHolder(@NonNull View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.step_number);
            name = (TextView) itemView.findViewById(R.id.step_name);
        }
        public void setDetails(Step step) {
            number.setText(step.getStep_number());
            name.setText(step.getDescription());
        }
    }

    public StepsAdapter(Context context, List<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsAdapter.StepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_step_layout, parent, false);
        return new StepsAdapter.StepsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepsHolder holder, int position) {
        Step step = steps.get(position);
        holder.setDetails(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

}



