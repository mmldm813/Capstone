package com.example.android.capstone.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;

public class ExerciseFragment extends Fragment {
    private static final String EXTRA_EXCERCISE = "excercise";

    private Exercise excercise;

    public static ExerciseFragment newInstance(Exercise exercise) {
        ExerciseFragment fragment = new ExerciseFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_EXCERCISE, exercise);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        excercise = (Exercise) getArguments().getSerializable(EXTRA_EXCERCISE);

        View view = inflater.inflate(R.layout.exercise_fragment_layout, container, false);
        setupViews(view);
        return  view;
    }

    private void setupViews(View view) {
        TextView excerciseName = view.findViewById(R.id.exercise_name);
        excerciseName.setText(excercise.getName());
        TextView timeLimit = view.findViewById(R.id.time_limit);
        timeLimit.setText(Long.toString(excercise.getTimeLimitInSeconds()));

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).startNextFragment();
            }
        });
    }
}
