package gay.nihil.lena.heartbeat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.time.Instant;

import gay.nihil.lena.heartbeat.ui.SettingsActivity;

public class Utils {

    public static User getPlaceholderUser() {
        User u = new User();
        u.id = -1;
        u.name = "Unknown User";

        return u;
    }

    public static String flagsToText(int flags) {
        if (flags == 0) {
            return "Device offline";
        } else {
            return "Device online";
        }
    }

    public static String timestampToText(long timestamp) {

        long now = Instant.now().getEpochSecond();

        long difference = now - timestamp;

        if (difference < 60) {
            return "Now";
        }

        if (difference < 3600) {
            return (difference / 60) + "m";
        }

        if (difference < 86400) {
            return (difference / 3600) + "h";
        }

        if (difference < 604800) {
            return (difference / 86400) + "d";
        }

        return Instant.ofEpochSecond(timestamp).toString();
    }

    public static Notification createPersistentNotification(String title, String text, Context context) {
        // make an intent to start the main UI when pressing on the persistent notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        TaskStackBuilder builder = TaskStackBuilder.create(context);
        builder.addNextIntentWithParentStack(new Intent(context, SettingsActivity.class));

        PendingIntent settingsIntent = builder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action settingsAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_home_black_24dp,
                        context.getString(R.string.title_activity_settings), settingsIntent)
                        .build();

        // make the persistent notification
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, context.getString(R.string.fgs_notification_channel_id))
                        .setContentTitle(title)
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
}
