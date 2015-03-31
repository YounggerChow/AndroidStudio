package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.app.AlertDialog;
import android.text.*;
import android.content.DialogInterface;

public class StrengthWorkoutActivity extends ActionBarActivity {
    int sets;
    int reps;
    double weight;
    int counter;
    int ID;
    TextView setsCompleted;
    WorkoutItem w;
    Button completeWorkout;
    int exertionLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_workout);

        TextView title = (TextView)findViewById(R.id.strength_title);
        TextView strengthSets = (TextView)findViewById(R.id.strength_sets);
        TextView strengthReps = (TextView)findViewById(R.id.strength_reps);
        TextView weightUsed = (TextView)findViewById(R.id.weight_used);
        setsCompleted = (TextView)findViewById(R.id.completed_sets);
        completeWorkout = (Button)findViewById(R.id.complete_strength);
        Bundle b = getIntent().getExtras();
        ID = b.getInt("ID");

        DBHelper dbh = new DBHelper(this);

        w = dbh.getWorkoutById(ID);
        title.setText(w.getName().toString());
        sets = ((StrengthWorkoutItem)w).getSetsScheduled();
        reps = ((StrengthWorkoutItem)w).getRepsScheduled();
        weight = ((StrengthWorkoutItem)w).getWeightUsed();
        counter = ((StrengthWorkoutItem)w).getSetsCompleted();

        setsCompleted.setText("Completed Sets: " + Integer.toString(counter));

        dbh.close();
        displaySets();
        completeWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //check to see if all sets have been completed
                //make sure time is documented
                //make sure a radio button is selected
                if (((StrengthWorkoutItem)w).getSetsCompleted() != ((StrengthWorkoutItem) w).getSetsScheduled()){
                    //prompt user input
                    AlertDialog.Builder builder = new AlertDialog.Builder(StrengthWorkoutActivity.this);
                    builder.setTitle("Attention");
                    builder.setMessage("You have not completed all scheduled sets. Are you sure you would like to " +
                            "complete this entry?");

                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (exertionLvl == 0){
                                final AlertDialog.Builder exertBuild = new AlertDialog.Builder(StrengthWorkoutActivity.this);
                                exertBuild.setTitle("Error");
                                exertBuild.setMessage("Please select an Exertion Level.");

                                exertBuild.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                exertBuild.show();
                            }

                                updateCompletedWorkout();

                        }
                    });

                    builder.setNegativeButton("No!", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }

                    if (exertionLvl == 0){
                        final AlertDialog.Builder exertBuild = new AlertDialog.Builder(StrengthWorkoutActivity.this);
                        exertBuild.setTitle("Error");
                        exertBuild.setMessage("Please select an Exertion Level.");

                        exertBuild.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        exertBuild.show();
                }
                else{
                        updateCompletedWorkout();
                        final AlertDialog.Builder finish = new AlertDialog.Builder(StrengthWorkoutActivity.this);
                        finish.setTitle("COMPLETE");
                        finish.setMessage("WORKOUT LOGGED!");

                        finish.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent = new Intent(StrengthWorkoutActivity.this, DailyWorkoutActivity.class);
                                startActivity(intent);
                            }

                        });
                        finish.show();

                    }


            }

        });

        // complete workout onclicklistener checks to see if 1 of radio buttons is selected.
        // complete workout onclicklistener updates database with

        strengthSets.setText("Reps: "+ Integer.toString(reps));
        strengthReps.setText("Sets: " + Integer.toString(sets));
        weightUsed.setText("Weight: " + Double.toString(weight));

        //radio buttons that user can select that describes difficulty of exercise.  this will be "easy" "moderate" "hard"
        //EditText that user may input amount of time taken to complete workout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strength_workout, menu);
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

    private void displaySets(){
        LinearLayout linearContainer = (LinearLayout) findViewById(R.id.strength_populate);
        linearContainer.removeAllViewsInLayout();


        TableLayout a = new TableLayout(StrengthWorkoutActivity.this);
        a.removeAllViews();

        linearContainer.addView(a);

        //populate screen with number of sets
        for (int i = 0; i < sets; i++) {
            ((StrengthWorkoutItem)w).getSetsCompleted();

            final TableRow row = new TableRow(StrengthWorkoutActivity.this);
            LinearLayout main = new LinearLayout(StrengthWorkoutActivity.this);
            LinearLayout stack = new LinearLayout(StrengthWorkoutActivity.this);
            TextView viewWeight = new TextView(StrengthWorkoutActivity.this);
            final TextView viewSet = new TextView(StrengthWorkoutActivity.this);
            row.setId(1000 + i);
            main.setId(2000 + i);
            stack.setId(3000 + i);
            viewSet.setId(5000 + i);
            row.removeAllViews();
            row.setBackgroundColor(getResources().getColor(R.color.primary200));
            row.setPadding(5, 10, 5, 10);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(0, 5, 0, 5);
            row.setLayoutParams(trParams);

            main.setOrientation(LinearLayout.HORIZONTAL);
            stack.setOrientation(LinearLayout.VERTICAL);

            viewSet.setText("Set: " + (i + 1));

            ViewGroup.LayoutParams stackParams = new LinearLayout.LayoutParams(600,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            stack.setLayoutParams(stackParams);
            stack.addView(viewSet);
            main.addView(stack);


            row.addView(main);
            a.addView(row);
            // make sets unclickable if ALL have been completed

            if (counter >= row.getId() - 999) {
                row.setClickable(false);
                viewSet.append(" Complete!");

            } else {

                row.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //check to see if all sets have been completed

                        AlertDialog.Builder builder = new AlertDialog.Builder(StrengthWorkoutActivity.this);
                        builder.setTitle("MINUTES TAKEN TO COMPLETE SET:");

                        // Set up the input
                        final EditText input = new EditText(StrengthWorkoutActivity.this);

                        // Specify the type of input expected; this, for example, sets the input as a password,
                        // and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("FINISHED!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                counter += 1;
                                //disable row from being clicked again
                                row.setClickable(false);
                                viewSet.append(" Complete!");

                                // Error Check EditText Input *************************
                                // add time spent to existing value
                                double timeInHours = Double.parseDouble(input.getText().toString()) / 60;
                                w.setTimeSpent(w.getTimeSpent() + timeInHours);

                                ((StrengthWorkoutItem) w).setSetsCompleted(counter);
                                ((StrengthWorkoutItem)w).setRepsCompleted(counter * reps);
                                //setRepsCompleted

                                setsCompleted.setText("Sets Completed: " + Integer.toString(((StrengthWorkoutItem) w).getSetsCompleted()));

                                DBHelper dbh = new DBHelper(StrengthWorkoutActivity.this);
                                dbh.completeWorkout(w);
                                dbh.close();

                            }
                        });
                        builder.setNegativeButton("I'M NOT DONE!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }

                });

            }
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.easy_strength:
                if (checked)
                    exertionLvl = 1;
                    break;
            case R.id.moderate_strength:
                if (checked)
                    exertionLvl = 2;
                    break;
            case R.id.hard_strength:
                if (checked)
                    exertionLvl = 3;
                    break;
        }
    }

    private void updateCompletedWorkout(){
        DBHelper dbh = new DBHelper(StrengthWorkoutActivity.this);
        //calculate calories (exertion lvl, time)
        //set
        WorkoutItem w = dbh.getWorkoutById(ID);
        Profile p = new Profile(StrengthWorkoutActivity.this);

        w.setExertionLevel(exertionLvl);
        double mets = w.calculateMETs();
        double time = w.getTimeSpent();

        double caloriesBurned = mets * weight * time;
        w.setCaloriesBurned(caloriesBurned);
        dbh.completeWorkout(w);
    }

}
