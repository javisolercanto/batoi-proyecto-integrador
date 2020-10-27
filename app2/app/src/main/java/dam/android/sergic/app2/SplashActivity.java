package dam.android.sergic.app2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import dam.android.sergic.app2.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Making the activity full screen without notification bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        // Hiding the action bar
        ActionBar ab = getSupportActionBar();
        if(ab!=null)
            ab.hide();

        // Hiding also the navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Thread timer = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                try { Thread.sleep(SPLASH_TIME); }
                catch (InterruptedException ex)
                {
                    Log.i("error",ex.toString());
                }
                finally
                {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };

        timer.start();
    }
}
