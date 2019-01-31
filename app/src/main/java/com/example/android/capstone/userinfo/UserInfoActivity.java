package com.example.android.capstone.userinfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.capstone.R;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class UserInfoActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserInfo userInfo;

    private TextInputEditText nameTextBox;
    private TextInputEditText weightTextBox;
    private TextInputLayout nameLayout;
    private TextInputLayout weightLayout;
    private TextView heightErrorText;
    private TextView dobErrorText;
    private TextView genderErrorText;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private long resultInFeet;
    private long resultInInches;
    private Button saveButton;
    private TextView dateText;
    private RadioButton maleButton;
    private RadioButton femaleButton;

    private DatePickerDialog pickerDialog;

    public static void startWith(Activity activity) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        dateText = findViewById(R.id.date_text);
        maleButton = findViewById(R.id.male_button);
        femaleButton = findViewById(R.id.female_button);
        saveButton = findViewById(R.id.save_button);
        nameLayout = findViewById(R.id.nameTextInputLayout);
        heightErrorText = findViewById(R.id.height_error_text);
        weightLayout = findViewById(R.id.weightTextInputLayout);
        dobErrorText = findViewById(R.id.dob_error_text);
        genderErrorText = findViewById(R.id.gender_error_text);
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
        dateText.setOnClickListener(new View.OnClickListener() {
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
                                dateText.setText(String.format("%s-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
                            }
                        }, year, month, day);
                pickerDialog.show();
            }
        });
    }

    private void setupListenerOnSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    saveToDatabase(v);
                    finish();
                }
            }
        });
    }

    private boolean validateFields() {
        boolean isValid = true;

        String name = nameTextBox.getText().toString();
        if (name.isEmpty()) {
            nameLayout.setError(getString(R.string.enter_valid_name));
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (resultInFeet < 4) {
            heightErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            heightErrorText.setVisibility(View.INVISIBLE);
        }

        String weight = weightTextBox.getText().toString();
        if (weight.isEmpty() || Integer.parseInt(weight) < 80 || Integer.parseInt(weight) > 300) {
            weightLayout.setError(getString(R.string.enter_correct_weight));
            isValid = false;
        } else {
            weightLayout.setError(null);
        }

        String dateOfBirthText = dateText.getText().toString();
        if (!dateOfBirthText.contains("-")) {
            dobErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            dobErrorText.setVisibility(View.INVISIBLE);
        }

        if (!maleButton.isChecked() && !femaleButton.isChecked()) {
            genderErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            genderErrorText.setVisibility(View.INVISIBLE);
        }

        return isValid;
    }

    public void saveToDatabase(View view) {
        final boolean isUpdate;
        if (userInfo == null) {
            isUpdate = false;
            userInfo = new UserInfo();
        } else {
            isUpdate = true;
        }

        String name = nameTextBox.getText().toString();
        userInfo.setName(name);

        userInfo.setFeet(resultInFeet);
        userInfo.setInches(resultInInches);

        String weight = weightTextBox.getText().toString();
        userInfo.setWeightInLbs(Integer.parseInt(weight));

        String dateOfBirthText = dateText.getText().toString();
        Date dateOfBirth = Date.valueOf(dateOfBirthText);
        userInfo.setDateOfBirth(dateOfBirth);

        UserInfo.Gender gender;
        if (maleButton.isChecked()) {
            gender = UserInfo.Gender.MALE;
        } else {
            gender = UserInfo.Gender.FEMALE;
        }
        userInfo.setGender(gender);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isUpdate) {
                    db.userDao().updateUserInfo(userInfo);
                } else {
                    db.userDao().insertUserInfo(userInfo);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {
        UserInfoViewModel viewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(@Nullable List<UserInfo> userInfos) {
                Timber.d("Updating info from LiveData in ViewModel");
                if (userInfos.size() > 0) {
                    userInfo = userInfos.get(0);
                    refreshUi();
                }
            }
        });
    }

    private void refreshUi() {
        nameTextBox.setText(userInfo.getName());
        feetSpinner.setSelection((int) userInfo.getFeet());
        inchesSpinner.setSelection((int) userInfo.getInches());
        weightTextBox.setText(String.valueOf(userInfo.getWeightInLbs()));
        dateText.setText(userInfo.getDateOfBirth().toString());
        if (userInfo.getGender() == UserInfo.Gender.MALE) {
            maleButton.setChecked(true);
        } else {
            femaleButton.setChecked(true);
        }
        saveButton.setText(R.string.update);
    }
}
