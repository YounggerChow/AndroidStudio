package com.gymrattrax.gymrattrax;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.text.ParseException;
import java.util.Date;

public class DBHelperTest extends AndroidTestCase {

    DBHelper dbh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        dbh = new DBHelper(context);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private WorkoutItem getWorkout() {
        WorkoutItem workout = new StrengthWorkoutItem();
        workout.setName(ExerciseName.CRUNCH);
        try {
            workout.setDateScheduled(dbh.convertDate("2015-03-20 15:00:00.000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        workout.setTimeScheduled(30);
        workout.setType(ExerciseType.STRENGTH);
        ((StrengthWorkoutItem) workout).setWeightUsed(15);
        ((StrengthWorkoutItem) workout).setRepsScheduled(12);
        ((StrengthWorkoutItem) workout).setSetsScheduled(4);
        return workout;
    }

    public void testCreateWorkout() {
        WorkoutItem workout = getWorkout();

        long id = dbh.addWorkout(workout);

        WorkoutItem returnWorkout = dbh.getWorkoutById(id);

        assert returnWorkout != null;
        assertEquals(workout.getName(), returnWorkout.getName());
        assertEquals(workout.getType(), returnWorkout.getType());
        assertEquals(workout.getDateScheduled(), returnWorkout.getDateScheduled());
        assertEquals(workout.getDateCompleted(), returnWorkout.getDateCompleted());
        assertEquals(workout.getTimeScheduled(), returnWorkout.getTimeScheduled());
        assertEquals(workout.getTimeSpent(), returnWorkout.getTimeSpent());
        assertEquals(workout.getExertionLevel(), returnWorkout.getExertionLevel());
        assertEquals(((StrengthWorkoutItem)workout).getWeightUsed(), ((StrengthWorkoutItem)returnWorkout).getWeightUsed());
        assertEquals(((StrengthWorkoutItem)workout).getRepsScheduled(), ((StrengthWorkoutItem)returnWorkout).getRepsScheduled());
        assertEquals(((StrengthWorkoutItem)workout).getRepsCompleted(), ((StrengthWorkoutItem)returnWorkout).getRepsCompleted());
        assertEquals(((StrengthWorkoutItem)workout).getSetsScheduled(), ((StrengthWorkoutItem)returnWorkout).getSetsScheduled());
        assertEquals(((StrengthWorkoutItem)workout).getSetsCompleted(), ((StrengthWorkoutItem)returnWorkout).getSetsCompleted());
    }

    public void testUpdateWorkout() {
        WorkoutItem workout = getWorkout();
        long id = dbh.addWorkout(workout);
        assertTrue(id > 0);
        workout.setID((int) id);
        workout.setTimeSpent(17);
        ((StrengthWorkoutItem)workout).setRepsCompleted(10);
        ((StrengthWorkoutItem)workout).setSetsCompleted(3);
        workout.setCaloriesBurned(120);
        workout.setExertionLevel(2);

        int result = dbh.completeWorkout(workout);
        assertTrue(result > 0);

        WorkoutItem returnWorkout = dbh.getWorkoutById(id);

        assertNotNull(returnWorkout);
        assertEquals(workout.getName(), returnWorkout.getName());
        assertEquals(workout.getType(), returnWorkout.getType());
        assertEquals(workout.getDateScheduled(), returnWorkout.getDateScheduled());
        assertEquals(workout.getDateCompleted(), returnWorkout.getDateCompleted());
        assertEquals(workout.getTimeScheduled(), returnWorkout.getTimeScheduled());
        assertEquals(workout.getTimeSpent(), returnWorkout.getTimeSpent());
        assertEquals(workout.getExertionLevel(), returnWorkout.getExertionLevel());
        assertEquals(((StrengthWorkoutItem)workout).getWeightUsed(), ((StrengthWorkoutItem)returnWorkout).getWeightUsed());
        assertEquals(((StrengthWorkoutItem)workout).getRepsScheduled(), ((StrengthWorkoutItem)returnWorkout).getRepsScheduled());
        assertEquals(((StrengthWorkoutItem)workout).getRepsCompleted(), ((StrengthWorkoutItem)returnWorkout).getRepsCompleted());
        assertEquals(((StrengthWorkoutItem)workout).getSetsScheduled(), ((StrengthWorkoutItem)returnWorkout).getSetsScheduled());
        assertEquals(((StrengthWorkoutItem)workout).getSetsCompleted(), ((StrengthWorkoutItem)returnWorkout).getSetsCompleted());
    }

    public void testDeleteWorkout() {
        WorkoutItem workout = getWorkout();

        long id = dbh.addWorkout(workout);

        int rows = dbh.deleteWorkout(workout);

        assertFalse(rows == 0);

//        Date date = null;
//        try {
//            date = dbh.convertDate("2015-03-20 00:00:00.000");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        WorkoutItem[] returnedWorkouts = dbh.getWorkoutsInRange(date, date);
//        WorkoutItem returnWorkout = null;
//        for (WorkoutItem w : returnedWorkouts) {
//            if (w.getID() == id) {
//                returnWorkout = w;
//                break;
//            }
//        }
        WorkoutItem returnWorkout = dbh.getWorkoutById(id);

        assertNull(returnWorkout);
    }
}