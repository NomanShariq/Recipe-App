package com.todocode.recipesdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todocode.recipesdemo.Model.Step;
import com.todocode.recipesdemo.R;

import java.util.List;

public class StepsListAdapter extends  RecyclerView.Adapter<StepsListAdapter.StepsHolder> {

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
            number.setText(step.getStep_number()+ " : ");
            name.setText(step.getDescription());
        }
    }

    public StepsListAdapter(Context context, List<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsListAdapter.StepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_step_list_layout, parent, false);
        return new StepsListAdapter.StepsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListAdapter.StepsHolder holder, int position) {
        Step step = steps.get(position);
        holder.setDetails(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

}




