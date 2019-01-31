package com.example.android.capstone.exercise;

import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.android.capstone.MyApplication;
import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.tts.TtsManager;

import javax.inject.Inject;

public class ExerciseFragment extends Fragment {
    private static final String EXTRA_EXERCISE = "exercise";
    private static final int SPEAK_INTERVAL = 15;

    @Inject
    TtsManager ttsManager;

    private Exercise exercise;
    private boolean isStart;
    private long timeRemaining;

    private Chronometer chronometer;
    private Button chronometerButton;

    public static ExerciseFragment newInstance(Exercise exercise) {
        ExerciseFragment fragment = new ExerciseFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_EXERCISE, exercise);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        exercise = (Exercise) getArguments().getSerializable(EXTRA_EXERCISE);
        timeRemaining = exercise.getTimeLimitInSeconds();

        inject();

        View view = inflater.inflate(R.layout.exercise_fragment_layout, container, false);
        setupViews(view);
        return view;
    }

    private void inject() {
        ((MyApplication)getContext().getApplicationContext()).getAppComponent().inject(this);
    }

    private void setupViews(View view) {
        TextView exerciseName = view.findViewById(R.id.exercise_name);
        exerciseName.setText(exercise.getName());

        TextView timeLimit = view.findViewById(R.id.time_limit);
        timeLimit.setText(exercise.getFriendlyTimeLimit());

        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setCountDown(true);
        chronometer.setBase(SystemClock.elapsedRealtime() + timeRemaining * 1000);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                onTick();
            }
        });

        chronometerButton = view.findViewById(R.id.chronometer_button);
        chronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChronometer();
            }
        });

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                ((ExerciseActivity)getActivity()).startNextFragment(timeRemaining);
            }
        });
    }

    private void onTick() {
        timeRemaining--;
        if ((timeRemaining % SPEAK_INTERVAL) == 0) {
            if (timeRemaining == 0) {
                ttsManager.speak(getString(R.string.time_limit_reached));
            }  else {
                String ending = getString(R.string.remaining);
                if (timeRemaining < 0) {
                    ending = getString(R.string.over);
                }
                ttsManager.speak(getTimeSpeech(timeRemaining) + " " + ending);
            }
        }
    }

    private void updateChronometer(){
        if (isStart) {
            chronometer.stop();
            isStart = false;
            chronometerButton.setText(R.string.start);
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeRemaining * 1000);
            chronometer.start();
            isStart = true;
            chronometerButton.setText(R.string.stop);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        speakExcerciseName();
        speakTimeLimit();
    }

    private void speakExcerciseName() {
        ttsManager.speak(exercise.getName(), TextToSpeech.QUEUE_FLUSH);
    }

    private void speakTimeLimit() {
        ttsManager.speak(getString(R.string.time_limit) + getTimeSpeech(exercise.getTimeLimitInSeconds()));
    }

    private String getTimeSpeech(long time) {
        String timeSpeech;

        if (time < 0) {
            time *= -1;
        }
        long min = time / 60;
        long sec = time % 60;
        if (min > 0) {
            timeSpeech = "" + min + getString(R.string.minutes) + sec + getString(R.string.seconds);
        } else {
            timeSpeech = "" + sec + getString(R.string.seconds);
        }
        return timeSpeech;
    }
}
