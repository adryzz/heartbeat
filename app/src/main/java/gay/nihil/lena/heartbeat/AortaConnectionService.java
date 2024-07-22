package gay.nihil.lena.heartbeat;

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
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import gay.nihil.lena.heartbeat.ui.SettingsActivity;

public class AortaConnectionService extends Service {
    public AortaConnectionService() {
    }

    private final IBinder binder = new AortaServiceBinder();
    AppDatabase database;

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


        String notifText = getString(R.string.fgs_notification_text_offline);
        String notifTitle = getString(R.string.fgs_notification_title, getString(R.string.fgs_notification_offline));
        setupPreferences();

        startForeground(9, Utils.createPersistentNotification(notifTitle, notifText, getApplicationContext()));
        return super.onStartCommand(intent, flags, startId);
    }

    private void setupPreferences() {
        // setup listener for preference changes
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            // apply preference
        });
    }

    private BroadcastReceiver deviceActiveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                // User has unlocked the phone
                Log.i("tag", "unlocked");
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                // Screen is turned on
                Log.i("tag", "turned on");
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