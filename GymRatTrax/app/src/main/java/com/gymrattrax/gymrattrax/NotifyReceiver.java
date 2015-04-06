package com.gymrattrax.gymrattrax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Set all alarms again when the device restarts.
 */
public class NotifyReceiver extends BroadcastReceiver {
    public static final String TAG = "NotifyReceiver";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME = "time";
    public static final String TONE = "tone";
    public static final String INTENT_NOTIFY = "intentNotify";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast received.");
        setNotifications(context);
    }

    public static void setNotifications(Context context) {
        Log.d(TAG, "Inside setNotifications (context=" + context.toString() + ").");
        cancelNotifications(context);

        DBHelper dbh = new DBHelper(context);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean defaultEnabled = sharedPref.getBoolean(SettingsActivity.PREF_NOTIFY_ENABLED, true);
        boolean defaultVibrate = sharedPref.getBoolean(SettingsActivity.PREF_NOTIFY_VIBRATE, true);
        int defaultMinutes = Integer.parseInt(
                sharedPref.getString(SettingsActivity.PREF_NOTIFY_ADVANCE, "0"));
        Uri defaultTone = Uri.parse(sharedPref.getString(SettingsActivity.PREF_NOTIFY_TONE, ""));

        Calendar today = Calendar.getInstance();
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.DAY_OF_MONTH, 7);
        WorkoutItem[] workouts = dbh.getWorkoutsInRange(today.getTime(), nextWeek.getTime());

//        NotifyScheduler notifyScheduler;
//        notifyScheduler = new NotifyScheduler(context);
//        notifyScheduler.doBindService();
        for (WorkoutItem workoutItem : workouts) {
            if (workoutItem.isNotificationDefault()) {
                if (defaultEnabled) {
                    workoutItem.setNotificationEnabled(true);
                    workoutItem.setNotificationVibrate(defaultVibrate);
                    workoutItem.setNotificationMinutesInAdvance(defaultMinutes);
                    workoutItem.setNotificationTone(defaultTone);
                } else {
                    workoutItem.setNotificationEnabled(false);
                }
            }
            if (workoutItem.isNotificationEnabled()) {
                PendingIntent pIntent = createPendingIntent(context, workoutItem);
//                notifyScheduler.setAlarmForNotification(workoutItem);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(workoutItem.getDateScheduled());
                calendar.add(Calendar.MINUTE, -workoutItem.getNotificationMinutesInAdvance());
                //TODO: Remove this 'quick fix' later.
                if (calendar.after(today)) {
                    Log.d(TAG, "About to set notification (ID: " + workoutItem.getID() + ").");
                    setNotification(context, calendar, pIntent);
                } else {
                    Log.d(TAG, "Notification (ID: " + workoutItem.getID() + ") not set.");
                }
            }
        }
//        notifyScheduler.doUnbindService();
        dbh.close();
    }
    private static void setNotification(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d(TAG, "Notification set.");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
    }

    public static void cancelNotifications(Context context) {
        DBHelper dbHelper = new DBHelper(context);

        WorkoutItem[] workoutItems = dbHelper.getWorkoutsForToday();

        for (WorkoutItem workoutItem : workoutItems) {
            if (workoutItem.isNotificationEnabled()) {
                PendingIntent pIntent = createPendingIntent(context, workoutItem);

                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Log.d(TAG, "Notification (ID: " + workoutItem.getID() + ") canceled.");
                alarmManager.cancel(pIntent);
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, WorkoutItem workoutItem) {
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(ID, workoutItem.getID());
        intent.putExtra(NAME, workoutItem.getName());
        Calendar cal = Calendar.getInstance();
        cal.setTime(workoutItem.getDateScheduled());
        cal.add(Calendar.MINUTE, -workoutItem.getNotificationMinutesInAdvance());
        intent.putExtra(TIME, cal.getTimeInMillis());
        if (workoutItem.getNotificationTone() != null) {
            intent.putExtra(TONE, workoutItem.getNotificationTone().toString());
        } else {
            intent.putExtra(TONE, (String)null);
        }

        return PendingIntent.getService(context, workoutItem.getID(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}