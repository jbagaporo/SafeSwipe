package com.itboys.lockscreen;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.itboys.safeswipe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up our Lockscreen
        makeFullScreen();
        startService(new Intent(this, YourService.class));

        setContentView(R.layout.activity_main);

        //get the wallpaper from homescreen
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        //set the wallpaper u got from homescreen on ur layout
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.myRelativeLayout);
        ll.setBackground(wallpaperDrawable);
    }

    public void startSafeswipe(View view){
        startActivity(new Intent(this, SafeSwipe.class));
    }


    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

}
