package dam.android.sergic.app2.asyncTasks;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Objects;
import dam.android.sergic.app2.tools.Tools;

public class JobServiceLocation extends JobService
{
    private static final int SIZE = 20;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        doInBackground(params);
        return true;
    }

    private void doInBackground(JobParameters params)
    {
        new Thread(new Runnable()
        {
            FusedLocationProviderClient fusedLocationClient;
            final int id = Tools.getUser(getApplicationContext()).getId();

            @Override
            public void run()
            {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if(checkPermissions() && isLocationEnabled())
                {
                    fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location ->
                        {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null)
                            {
                                // Logic to handle location object
                                try
                                {
                                    ByteBuffer buffer = ByteBuffer.allocate(SIZE);
                                    buffer.putInt(id);
                                    buffer.putDouble(location.getLatitude());
                                    buffer.putDouble(location.getLongitude());

//                                    Toast.makeText(getApplicationContext(),"LOCATION SEND",Toast.LENGTH_SHORT).show();
                                    send(buffer.array());
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                }
                else
                    // Re-scheduling the job.
                    jobFinished(params, true);
            }
        }).start();
    }

    private static final int PORT = 8888;
    private static final String SERVER = "grup2.cipfpbatoi.es";
    private void send(byte[] array) throws IOException
    {
        // Because android things i am doing this in the main thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(array,SIZE, InetAddress.getByName(SERVER),PORT);

        socket.send(packet);
        socket.close();
    }

    private boolean checkPermissions()
    {
        // check for permissions granted
        if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getApplicationContext()),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Opening the application settings.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);

            // UI Thread, otherwise Impossible to notice user that I need the location enable.
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() ->
            {
                // update the ui from here
                Toast.makeText(getApplicationContext(),"Location service not allowed at ALL TIME!",Toast.LENGTH_SHORT).show();
            },0);

            return false;
        }
        else
            return true;
    }

    private boolean isLocationEnabled()
    {
        boolean location = true;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null && !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER))) {

            // UI Thread, to let user know what I need
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                // update the ui from here
                Toast.makeText(getApplicationContext(),"Location MUST be enable forever!",Toast.LENGTH_SHORT).show();
            },0);
            getApplication().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            location = false;
        }

        return location;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        return false;
    }
}