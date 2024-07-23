package gay.nihil.lena.heartbeat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import java.util.List;
import java.util.Objects;

import gay.nihil.lena.heartbeat.ui.SettingsActivity;

public class AortaConnectionService extends Service {
    public AortaConnectionService() {
    }

    private final IBinder binder = new AortaServiceBinder();
    AppDatabase database;
    PendingIntent timerPendingIntent;
    boolean exact;
    long time;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // stop if it has the bool stop
        // yes it can be stopped by a bad actor but whatever
        if (intent.hasExtra("stop"))
        {
            if (intent.getBooleanExtra("stop", false))
            {
                stopSelf();
            }
        }

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "heartbeat-database").allowMainThreadQueries().build();

        registerReceiver(deviceActiveReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        registerReceiver(deviceActiveReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(deviceActiveReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));


        String notifText = getString(R.string.fgs_notification_text_offline);
        String notifTitle = getString(R.string.fgs_notification_title, getString(R.string.fgs_notification_offline));
        setupPreferences();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        exact = prefs.getBoolean("use_exact_alarm", false);
        time = Long.parseLong(prefs.getString("auto_heartbeat_timer", "300")) * 1000;
        setupTimer();
        startForeground(9, Utils.createPersistentNotification(notifTitle, notifText, getApplicationContext()));
        return super.onStartCommand(intent, flags, startId);
    }


    private void setupTimer() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (timerPendingIntent != null) {
            manager.cancel(timerPendingIntent);
        }

        Intent nextIntent = new Intent(this, AlarmReceiver.class);
        timerPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (manager.canScheduleExactAlarms() && exact) {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, time, timerPendingIntent);
        } else {
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, time, timerPendingIntent);
        }
    }

    private void setupPreferences() {
        // setup listener for preference changes
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i("tag", "prefs");
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            // apply preference
            if (Objects.equals(key, "use_exact_alarm")) {
                exact = sharedPreferences.getBoolean("use_exact_alarm", false);
            } else if (Objects.equals(key, "auto_heartbeat_timer")) {
                time = Long.parseLong(sharedPreferences.getString("auto_heartbeat_timer", "300")) * 1000;
            }
            setupTimer();
        });
    }

    private final BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private final BroadcastReceiver deviceActiveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                // User has unlocked the phone
                Log.i("tag", "unlocked");
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                // Screen is turned on
                Log.i("tag", "turned on");
            } else if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
                // TODO: send low battery message
                Log.i("tag", "low battery");
            }
        }
    };

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class AortaServiceBinder extends Binder {
        public AortaConnectionService getService() {
            return AortaConnectionService.this;
        }
    }

    /* SERVICE INTERFACE AAAAAA */

    public boolean getOnlineStatus() {
        return true;
    }

    public LiveData<List<Event>> getEvents() {
        EventDao dao = database.eventDao();
        return dao.getRecentEventsLive(64);
    }

    public User getConversation(long id) {
        UserDao dao = database.userDao();
        return dao.getUser(id);
    }

    public LiveData<List<User>> getUsers() {
        UserDao dao = database.userDao();
        return dao.getUsersLive();
    }
}