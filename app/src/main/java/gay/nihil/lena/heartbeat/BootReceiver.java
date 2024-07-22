package gay.nihil.lena.heartbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("BootReceiver", "Device booted. Starting service...");
            Intent serviceIntent = new Intent(context, AortaConnectionService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}