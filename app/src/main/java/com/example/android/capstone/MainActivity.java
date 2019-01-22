package com.example.android.capstone;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.sql.Date;

public class MainActivity extends AppCompatActivity {

    private static AppDatabase db;
    TextInputEditText nameTextBox;
    Spinner feetSpinner;
    Spinner inchesSpinner;
    long resultInFeet;
    long resultInInches;
    TextInputEditText weightTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(getApplicationContext());
        findViews();
        setupSpinners();
        setListenersOnSpinners();

    }

    public void findViews(){
        nameTextBox = findViewById(R.id.name_edit_text);
        feetSpinner = findViewById(R.id.feet);
        inchesSpinner = findViewById(R.id.inches);
        weightTextBox = findViewById(R.id.weight_edit_text);
    }

    public void setupSpinners(){
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource
                (this, R.array.feet_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(feetAdapter);

        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource
                (this, R.array.inches_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesSpinner.setAdapter(inchesAdapter);
    }

    public void setListenersOnSpinners(){
        feetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               resultInFeet = parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing yet
            }
        });

        inchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultInInches = parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing yet
            }
        });
    }

    public void saveToDatabase(View view) {
        String name = nameTextBox.getText().toString();
        String weight = weightTextBox.getText().toString();
        int adjWeight = Integer.parseInt(weight);
        Date dateOfBirth = (new Date(1990, 9 ,11));

        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setFeet(resultInFeet);
        userInfo.setInches(resultInInches);
        userInfo.setWeightInLbs(adjWeight);
        userInfo.setDateOfBirth(dateOfBirth);
        db.userDao().insertUserInfo(userInfo);
    }
}
