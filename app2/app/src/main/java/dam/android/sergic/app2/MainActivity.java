package dam.android.sergic.app2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import dam.android.sergic.app2.asyncTasks.JobServiceLocation;
import dam.android.sergic.app2.db.BatoiLogicDBManager;
import dam.android.sergic.app2.models.Deliveryman;
import dam.android.sergic.app2.tools.Tools;
import dam.android.sergic.app2.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private static boolean mode;
    private NavController controller;
    private FloatingActionButton fab;

    public static final int JOB_LOCATION_ID = 443;

    // Permissions required to contacts provider, only needed to READ
    @SuppressLint("InlinedApi")
    public static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    public static final int REQUEST_LOCATION_PERMISSION = 1;                                            // Id to identify a contacts permission request

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        scheduleJob();
    }

    private void setupUI()
    {
        fab = findViewById(R.id.fab);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigatorDrawer();

        mode = true;
        fab.setOnClickListener(view ->
        {
            if(mode)
            {
                controller.navigate(R.id.nav_map);
                mode = false;
                fab.setImageDrawable(getDrawable(R.drawable.ic_assistant));
            }
            else
            {
                controller.navigate(R.id.nav_orders);
                mode = true;
                fab.setImageDrawable(getDrawable(R.drawable.ic_my_location_black_24dp));
            }
        });
    }

    private void setupNavigatorDrawer()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Getting the Header
        View hView =  navigationView.getHeaderView(0);
        TextView tvName = hView.findViewById(R.id.tvName);
        TextView tvNickname = hView.findViewById(R.id.tvNickname);

        // Changing header text
        Deliveryman user = Tools.getUser(this);
        tvName.setText(user.getName());
        tvNickname.setText(getString(R.string.placeholder_nickname, user.getNickname()));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_orders, R.id.nav_map)
                .setDrawerLayout(drawer)
                .build();
        controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, controller, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, controller);

        controller.addOnDestinationChangedListener((controller, destination, arguments) ->
        {
            if(destination.getId() == R.id.nav_map)
            {
                fab.setImageDrawable(getDrawable(R.drawable.ic_assistant));
                mode = false;
            }
            else
            {
                if(destination.getId() == R.id.nav_orders)
                {
                    fab.setImageDrawable(getDrawable(R.drawable.ic_my_location_black_24dp));
                    mode = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // Logout option.
        if (item.getItemId() == R.id.action_logout)
        {
            Tools.eraseUser(this);
            new BatoiLogicDBManager(getApplicationContext()).drop();
            cancelJob();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else
        {
            if(item.getItemId() == R.id.action_refresh)
            {
                // The other option is to update the recyclerview
                Bundle bundle = new Bundle();
                bundle.putBoolean("refresh", true);
                controller.navigate(R.id.nav_orders, bundle);
                mode = true;                                        // True means - To Map.
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void scheduleJob()
    {
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        if(!preferences.getBoolean("firstRunComplete", false) && checkPermissions())
        {
            //schedule the job only once.
            ComponentName location = new ComponentName(this, JobServiceLocation.class);
            JobInfo info = new JobInfo.Builder(JOB_LOCATION_ID, location)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)                                   // Require an internet connection.
                    .setPersisted(true)                                                                 // Job working even after reboot.
                    .setPeriodic(AlarmManager.INTERVAL_FIFTEEN_MINUTES)                                 // Job Executed Every 15 min.
                    .build();

            int resultCode = JobScheduler.RESULT_FAILURE;
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            if (scheduler != null)
                resultCode = scheduler.schedule(info);

//            Toast.makeText(this,"JobScheduled",Toast.LENGTH_SHORT).show();

            if(resultCode == JobScheduler.RESULT_SUCCESS)
            {
                //update shared preference
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstRunComplete", true);
                editor.apply();
            }
        }
    }

    private void cancelJob()
    {
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        if(preferences.getBoolean("firstRunComplete", false))
        {
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            if (scheduler != null)
                scheduler.cancel(JOB_LOCATION_ID);

            //update shared preference
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRunComplete", false);
            editor.apply();
        }
    }

    private boolean checkPermissions()
    {
        // check for permissions granted
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // opens Dialog: requests user to grant permission.
            ActivityCompat.requestPermissions(this, MainActivity.PERMISSIONS,
                    MainActivity.REQUEST_LOCATION_PERMISSION);
            return false;
        }
        else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_LOCATION_PERMISSION)
        {
            // We have requested one READ permission for contacts, so only need [0] to be checked.
            if(grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.location_service_is_needed),Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mode",mode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mode = savedInstanceState.getBoolean("mode");
    }
}