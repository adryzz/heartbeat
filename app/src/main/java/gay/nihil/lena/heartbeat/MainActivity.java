package gay.nihil.lena.heartbeat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import gay.nihil.lena.heartbeat.databinding.ActivityMainBinding;
import gay.nihil.lena.heartbeat.ui.SettingsActivity;
import gay.nihil.lena.heartbeat.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public AortaConnectionService service;
    public boolean isServiceConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager manager = getSystemService(NotificationManager.class);
        createNotificationChannels(manager);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_events)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        requestPermissions(new String[] { "android.permission.POST_NOTIFICATIONS" },0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(getApplicationContext(), AortaConnectionService.class);
        startForegroundService(intent);

        serviceConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "about", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void serviceConnection() {
        Intent bindIntent = new Intent(getApplicationContext(), AortaConnectionService.class);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder svc) {
                service = ((AortaConnectionService.AortaServiceBinder)svc).getService();
                isServiceConnected = true;
                //HomeFragment fragment = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_events).getChildFragmentManager().getFragments().get(0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
                isServiceConnected = false;
            }
        };
        bindService(bindIntent, connection, 0);
    }

    private void createNotificationChannels(NotificationManager manager) {
        List<NotificationChannel> channels = new ArrayList<>();

        channels.add(createChannel(
                R.string.fgs_notification_channel_id,
                R.string.fgs_notification_channel_name,
                R.string.fgs_notification_channel_description,
                NotificationManager.IMPORTANCE_HIGH
        ));

        channels.add(createChannel(
                R.string.alert_notification_channel_id,
                R.string.alert_notification_channel_name,
                R.string.alert_notification_channel_description,
                NotificationManager.IMPORTANCE_MAX
        ));


        manager.createNotificationChannels(channels);
    }

    private NotificationChannel createChannel(int idRes, int nameRes, int descriptionRes, int importance) {
        String id = getString(idRes);
        CharSequence name = getString(nameRes);
        String description = getString(descriptionRes);

        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        return channel;
    }
}