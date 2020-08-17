package com.example.resposloyalty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    EditText firstNameText;
    EditText lastNameText;
    EditText middleNameText;
    EditText phoneNumberText;
    EditText birthdayText;
    EditText emailText;
    EditText addressText;
    DatePickerDialog birthdayDatePicker;
    private ClientProfile clientProfile;
    private CheckBox maleCheckBox;
    private CheckBox femaleCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientProfile = new ClientProfile((Authenticator) getIntent().getSerializableExtra("authConfirmedClient"));
        Thread thread = new Thread(clientProfile);
        thread.start();
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firstNameText = findViewById(R.id.firstNameEditText);
        lastNameText = findViewById(R.id.lastNameEditText);
        middleNameText = findViewById(R.id.middleNameEditText);
        phoneNumberText = findViewById(R.id.phoneNumberEditText);
        emailText = findViewById(R.id.email_edit_text);
        addressText = findViewById(R.id.address_edit_text);
        birthdayText = findViewById(R.id.birthday_edit_text);
        birthdayText.setInputType(InputType.TYPE_NULL);
        birthdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                birthdayDatePicker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthdayText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                birthdayDatePicker.show();
            }
        });

        maleCheckBox = findViewById(R.id.maleCheckBox);
        femaleCheckBox = findViewById(R.id.femaleCheckBox);
        maleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    femaleCheckBox.setChecked(false);
                }
            }
        });
        femaleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    maleCheckBox.setChecked(false);
                }
            }
        });
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        displayProfileInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show();
                return (true);
            case R.id.logout:
                Intent intentMain = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intentMain);
                return (true);
            case R.id.exit:
                finishAffinity();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void saveProfile(View view) {
        clientProfile.setFirstName(firstNameText.getText().toString());
        clientProfile.setLastName(lastNameText.getText().toString());
        clientProfile.setMiddleName(middleNameText.getText().toString());
        clientProfile.setPhoneNumber(phoneNumberText.getText().toString());
        try {
            clientProfile.setBirthday(new SimpleDateFormat("dd/MM/yyyy").
                    parse(birthdayText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        clientProfile.setEmail(emailText.getText().toString());
        clientProfile.setAddress(addressText.getText().toString());
        if (maleCheckBox.isChecked())
            clientProfile.setMale(true);
        if (femaleCheckBox.isChecked())
            clientProfile.setMale(false);

        Runnable runnable = new Runnable() {
            public void run() {
                clientProfile.updateProfile();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
            if (!clientProfile.getErrorMessage().equals("")) {
                Toast.makeText(this, clientProfile.getErrorMessage(), Toast.LENGTH_LONG).show();
                clientProfile.setErrorMessage("");
            }
            else {
                Toast.makeText(this, "Профіль збережено", Toast.LENGTH_LONG).show();
                finish();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected void displayProfileInfo() {
        firstNameText.setText(clientProfile.getFirstName());
        lastNameText.setText(clientProfile.getLastName());
        middleNameText.setText(clientProfile.getMiddleName());
        phoneNumberText.setText(clientProfile.getPhoneNumber());
        birthdayText.setText(new SimpleDateFormat("dd/MM/yyyy").format(clientProfile.getBirthday()));
        emailText.setText(clientProfile.getEmail());
        addressText.setText(clientProfile.getAddress());
        if (clientProfile.isMale() == true) {
            maleCheckBox.setChecked(true);
        } else {
            femaleCheckBox.setChecked(true);
        }

    }
}