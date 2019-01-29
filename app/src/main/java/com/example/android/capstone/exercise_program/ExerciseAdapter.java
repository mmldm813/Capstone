package com.example.android.capstone.exercise_program;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>{

    private List<Exercise> exercises;
    private Context context;

    public ExerciseAdapter(Context context) {
        this.exercises = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.program_views, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setData(List<Exercise> exercises) {
        if (exercises != null) {
            this.exercises.clear();
            this.exercises.addAll(exercises);
        } else {
            this.exercises = exercises;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order;
        TextView exerciseName;
        TextView exerciseTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.order_number);
            exerciseName = itemView.findViewById(R.id.exercise_name);
            exerciseTime = itemView.findViewById(R.id.time);
        }

        void bind(final Exercise exercise){
            if (exercise != null) {
                order.setText(Integer.toString(exercise.getOrderNumber()));
                exerciseName.setText(exercise.getName());
                exerciseTime.setText(exercise.getFriendlyTimeLimit());
            }
        }
    }

}
