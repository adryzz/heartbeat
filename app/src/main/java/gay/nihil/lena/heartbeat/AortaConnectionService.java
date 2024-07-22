package gay.nihil.lena.heartbeat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

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


        String notifText = getString(R.string.fgs_notification_offline);
        setupPreferences();

        startForeground(9, createPersistentNotification(notifText, false));
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createPersistentNotification(String text, boolean addStopButton) {
        // make an intent to start the main UI when pressing on the persistent notification
        Intent notificationIntent = new Intent(this, SettingsActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        TaskStackBuilder builder = TaskStackBuilder.create(getApplicationContext());
        builder.addNextIntentWithParentStack(new Intent(getApplicationContext(), SettingsActivity.class));

        PendingIntent settingsIntent = builder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action settingsAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_home_black_24dp,
                        getString(R.string.title_activity_settings), settingsIntent)
                        .build();

        // make the persistent notification
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, getString(R.string.fgs_notification_channel_id))
                        .setContentTitle(getText(R.string.fgs_notification_title))
                        .setContentText(text)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentIntent(pendingIntent)
                        .addAction(settingsAction)
                        .setOnlyAlertOnce(true)
                        .setVibrate(new long[] { 0L })
                        .setSound(null)
                        .setOngoing(true)
                        .setSilent(true);

        return notification.build();
    }

    private void setupPreferences() {
        // setup listener for preference changes
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            // apply preference
        });
    }

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