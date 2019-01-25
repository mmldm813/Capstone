package com.example.android.capstone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;

import java.sql.Date;
import java.util.Calendar;

public class UserInfoActivity extends AppCompatActivity {

    private AppDatabase db;

    private TextInputEditText nameTextBox;
    private TextInputEditText weightTextBox;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private long resultInFeet;
    private long resultInInches;
    private Button saveButton;
    private ImageView calendarImage;
    private TextView dateText;
    private RadioButton maleButton;
    private RadioButton femaleButton;

    private UserInfo.Gender gender;

    private DatePickerDialog pickerDialog;

    public static void startWith(Activity activity) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(getApplicationContext());
        findViews();
        setupSpinners();
        setListenersOnSpinners();
        setupListenerOnSaveButton();
        setListenerOnCalendarButton();

    }

    public void findViews() {
        nameTextBox = findViewById(R.id.name_edit_text);
        feetSpinner = findViewById(R.id.feet);
        inchesSpinner = findViewById(R.id.inches);
        weightTextBox = findViewById(R.id.weight_edit_text);
        calendarImage = findViewById(R.id.image_calendar);
        dateText = findViewById(R.id.date_text);
        maleButton = findViewById(R.id.male_button);
        femaleButton = findViewById(R.id.female_button);
        saveButton = findViewById(R.id.save_button);
    }

    public void setupSpinners() {
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource
                (this, R.array.feet_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(feetAdapter);

        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource
                (this, R.array.inches_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesSpinner.setAdapter(inchesAdapter);

    }

    public void setListenersOnSpinners() {
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

    public void setListenerOnCalendarButton() {
        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickerDialog = new DatePickerDialog(UserInfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                pickerDialog.show();
            }
        });
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.male_button:
                if (checked) {
                    gender = UserInfo.Gender.MALE;
                }
                break;
            case R.id.female_button:
                if (checked) {
                    gender = UserInfo.Gender.FEMALE;
                }
                break;
        }

    }

    private void setupListenerOnSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase(v);
                Intent loadJournalActivity = new Intent(UserInfoActivity.this, JournalActivity.class);
                startActivity(loadJournalActivity);
            }
        });
    }

    public void saveToDatabase(View view) {
        String name = nameTextBox.getText().toString();
        String weight = weightTextBox.getText().toString();
        int adjWeight = Integer.parseInt(weight);

        String date = dateText.getText().toString();
        String[] datePieces = date.split("/");
        Date newDate = Date.valueOf(datePieces[2] + "-" + datePieces[0] + "-" + datePieces[1]);

        final UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setFeet(resultInFeet);
        userInfo.setInches(resultInInches);
        userInfo.setWeightInLbs(adjWeight);
        userInfo.setDateOfBirth(newDate);
        userInfo.setGender(gender);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.userDao().insertUserInfo(userInfo);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
