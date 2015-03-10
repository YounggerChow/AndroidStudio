package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends ActionBarActivity {

    private Button backProfileButton;
    private Button editProfileButton;
    private EditText nameEditText;
    private EditText birthDateEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText fatPercentageEditText;
    private RadioButton littleExercise;
    private RadioButton lightExercise;
    private RadioButton modExercise;
    private RadioButton heavyExercise;
    private Spinner profileSpinner;
    private boolean editing;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profile = new Profile(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_profile);

        //database query and then set editTexts to display the appropriate data

        backProfileButton = (Button) findViewById(R.id.BackProfileButton);
        editProfileButton = (Button) findViewById(R.id.EditProfileButton);
        nameEditText = (EditText)findViewById(R.id.profile_name);
        birthDateEditText = (EditText)findViewById(R.id.birth_date);
        weightEditText = (EditText)findViewById(R.id.profile_weight);
        heightEditText = (EditText)findViewById(R.id.profile_height);
        fatPercentageEditText = (EditText)findViewById(R.id.fat_percentage);
        littleExercise = (RadioButton)findViewById(R.id.little_exercise);
        lightExercise = (RadioButton)findViewById(R.id.light_exercise);
        modExercise = (RadioButton)findViewById(R.id.mod_exercise);
        heavyExercise = (RadioButton)findViewById(R.id.heavy_exercise);
        profileSpinner = (Spinner)findViewById(R.id.profile_spinner);
        editing = false;

        lockInput();
        setTextFromProfile();


        backProfileButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (editing) {
                    editing = false;
                    lockInput();
                    editProfileButton.setText("EDIT");
                    setTextFromProfile();
                }
                else {
                    onBackPressed();
                }

            }
        });

        editProfileButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (editing) {
                    String errors = validateInput();
                    if (errors.isEmpty()) {
                        lockInput();
                        editProfileButton.setText("EDIT");
                        saveChanges(view);
                        editing = false;
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), errors,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    editing = true;
                    nameEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                    nameEditText.setEnabled(true);
                    nameEditText.setClickable(true);

                    birthDateEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                    birthDateEditText.setEnabled(true);
                    birthDateEditText.setClickable(true);

                    weightEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                    weightEditText.setEnabled(true);
                    weightEditText.setClickable(true);

                    heightEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                    heightEditText.setEnabled(true);
                    heightEditText.setClickable(true);

                    fatPercentageEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                    fatPercentageEditText.setEnabled(true);
                    fatPercentageEditText.setClickable(true);

                    littleExercise.setEnabled(true);
                    littleExercise.setClickable(true);
                    lightExercise.setEnabled(true);
                    lightExercise.setClickable(true);
                    modExercise.setEnabled(true);
                    modExercise.setClickable(true);
                    heavyExercise.setEnabled(true);
                    heavyExercise.setClickable(true);
                    profileSpinner.setEnabled(true);
                    profileSpinner.setClickable(true);

                    editProfileButton.setText("SAVE");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fitness_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void lockInput() {

        // make edit text unclickable until edit button is clicked
        nameEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        nameEditText.setEnabled(false);
        nameEditText.setClickable(false);

        birthDateEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        birthDateEditText.setEnabled(false);
        birthDateEditText.setClickable(false);

        weightEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        weightEditText.setEnabled(false);
        weightEditText.setClickable(false);

        heightEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        heightEditText.setEnabled(false);
        heightEditText.setClickable(false);

        fatPercentageEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        fatPercentageEditText.setEnabled(false);
        fatPercentageEditText.setClickable(false);

        littleExercise.setEnabled(false);
        littleExercise.setClickable(false);
        lightExercise.setEnabled(false);
        lightExercise.setClickable(false);
        modExercise.setEnabled(false);
        modExercise.setClickable(false);
        heavyExercise.setEnabled(false);
        heavyExercise.setClickable(false);
        profileSpinner.setEnabled(false);
        profileSpinner.setClickable(false);
    }

    public void saveChanges(View view){
        // update database profile
        DBHelper dbh = new DBHelper(this);
        dbh.setProfileInfo(DBContract.ProfileTable.KEY_NAME, nameEditText.getText().toString());
        dbh.setProfileInfo(DBContract.ProfileTable.KEY_HEIGHT, heightEditText.getText().toString());

        String date = birthDateEditText.getText().toString();
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date d = null;
        try {
            d = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null) {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            date = dbFormat.format(d) + " 00:00:00.000";
            dbh.setProfileInfo(DBContract.ProfileTable.KEY_BIRTH_DATE, date);
        }

        double bodyFat = -1;
        if (!fatPercentageEditText.getText().toString().trim().isEmpty())
            bodyFat = Double.valueOf(fatPercentageEditText.getText().toString());

        double activityLevel = -1;

        if (littleExercise.isChecked())
            activityLevel = DBContract.WeightTable.ACT_LVL_LITTLE;
        else if (lightExercise.isChecked())
            activityLevel = DBContract.WeightTable.ACT_LVL_LIGHT;
        else if (modExercise.isChecked())
            activityLevel = DBContract.WeightTable.ACT_LVL_MOD;
        else if (heavyExercise.isChecked())
            activityLevel = DBContract.WeightTable.ACT_LVL_HEAVY;
        else
            System.out.println("No activity level checked");

        dbh.setWeight(Double.valueOf(weightEditText.getText().toString()), bodyFat, activityLevel);


        switch (profileSpinner.getItemAtPosition(
                profileSpinner.getSelectedItemPosition()).toString().toUpperCase().substring(0,1)) {
            case "M":
                dbh.setProfileInfo(DBContract.ProfileTable.KEY_SEX,
                        DBContract.ProfileTable.VAL_SEX_MALE);
                break;
            case "F":
                dbh.setProfileInfo(DBContract.ProfileTable.KEY_SEX,
                        DBContract.ProfileTable.VAL_SEX_FEMALE);
                break;
        }

        profile = new Profile(this);
    }

    public void editProfile(View view){
        // unlock EditText fields and spinner
    }

    /**
     * Handle radio button clicks
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.little_exercise:
                if (checked)
                    // ActivityLevel = 1.2
                    break;
            case R.id.light_exercise:
                if (checked)
                    // ActivityLevel = 1.375
                    break;
            case R.id.mod_exercise:
                if (checked)
                    // ActivityLevel = 1.55
                    break;
            case R.id.heavy_exercise:
                if (checked)
                    // ActivityLevel = 1.725
                    break;
        }
    }

    private void setTextFromProfile() {
        nameEditText.setText(profile.getName());
        if (profile.getHeight() > 0)
            heightEditText.setText(String.valueOf(profile.getHeight()));
        else
            heightEditText.setText("");

        if (profile.getAge() > 0) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            birthDateEditText.setText(inputFormat.format(profile.getDOB()));
        }
        else {
            birthDateEditText.setText("");
        }

        if (profile.getWeight() > 0)
            weightEditText.setText(String.valueOf(profile.getWeight()));
        else
            weightEditText.setText("");
        if (profile.getFatPercentage() > 0)
            fatPercentageEditText.setText(String.valueOf(profile.getFatPercentage()));
        else
            fatPercentageEditText.setText("");
        if (profile.getActivityLevel() <= DBContract.WeightTable.ACT_LVL_LITTLE)
            littleExercise.toggle();
        else if (profile.getActivityLevel() <= DBContract.WeightTable.ACT_LVL_LIGHT)
            lightExercise.toggle();
        else if (profile.getActivityLevel() <= DBContract.WeightTable.ACT_LVL_MOD)
            modExercise.toggle();
        else
            heavyExercise.toggle();

        switch (profile.getGender()) {
            case 'M':
                profileSpinner.setSelection(0);
                break;
            case 'F':
                profileSpinner.setSelection(1);
                break;
        }
    }
    private String validateInput() {
        String testVar;
        double testDbl;
        //Name and body fat are optional, and sex forces input.
        //Test birth date
        testVar = birthDateEditText.getText().toString();
        //even though MM/DD/YYYY is stated, M/D/YYYY is allowed
        if (testVar.trim().isEmpty())
            return "Date is required.";
        else if (testVar.trim().length() < 8)
            return "Date is in incorrect format";
        if (!testVar.equals(testVar.trim())) {
            testVar = testVar.trim();
            birthDateEditText.setText(testVar);
        }
        if (!testVar.equals(testVar.replace('-','/'))) {
            testVar = testVar.replace('-','/');
            birthDateEditText.setText(testVar);
        }
        if (testVar.charAt(1) == '/') {
            testVar = "0" + testVar;
            birthDateEditText.setText(testVar);
        }
        if (testVar.charAt(4) == '/') {
            testVar = testVar.substring(0, 3) + "0" + testVar.substring(3);
            birthDateEditText.setText(testVar);
        }
        if (!(testVar.length() == 10))
            return "Date is in incorrect format.";
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date testDate;
        try {
            testDate = inputFormat.parse(testVar);
        } catch (ParseException e) {
            return "Date is in incorrect format";
        }
        if (testDate != null) {
            Calendar now = Calendar.getInstance();
            Calendar testCal = Calendar.getInstance();
            testCal.setTime(testDate);
            if (testCal.after(now)) {
                return "Okay, Marty McFly, you weren't born in the future.";
            }
        }
        else {
            return "Date is in incorrect format";
        }

        //Test weight
        testVar = weightEditText.getText().toString();
        if (testVar.trim().isEmpty())
            return "Weight is required.";
        try {
            testDbl = Double.parseDouble(testVar);
        } catch (NumberFormatException e) {
            return "Weight is in incorrect format.";
        }
        if (testDbl < 0)
            return "Weight cannot be negative.";
        else if (testDbl < 20)
            return "Weight is too low.";
        else if (testDbl > 1400)
            return "Weight is too high.";

        //Test height
        testVar = heightEditText.getText().toString();
        if (testVar.trim().isEmpty())
            return "Height is required.";
        try {
            testDbl = Double.parseDouble(testVar);
        } catch (NumberFormatException e) {
            return "Height is in incorrect format.";
        }
        if (testDbl < 0)
            return "Height cannot be negative.";
        else if (testDbl < 20)
            return "Height is too low.";
        else if (testDbl > 120)
            return "Height is too high.";

        //Test body fat percentage
        testVar = fatPercentageEditText.getText().toString();
        if (!testVar.trim().isEmpty()) {
            try {
                testDbl = Double.parseDouble(testVar);
            } catch (NumberFormatException e) {
                return "Body fat percentage is in incorrect format.";
            }
            if (testDbl < 0)
                return "Body fat percentage cannot be negative.";
            else if (testDbl > 100)
                return "Body fat percentage cannot be over 100%.";
        }

        return ""; //No errors found.
    }
}