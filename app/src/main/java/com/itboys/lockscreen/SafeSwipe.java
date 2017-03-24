package com.itboys.lockscreen;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.itboys.lockscreen.LocationFragment;
import com.itboys.safeswipe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SafeSwipe extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String CAMERA_DIR = "/dcim/";

    //a variable to store a reference to the Surface View at the main.admin file
    private SurfaceView sv;

    //a bitmap to display the captured image
    private Bitmap bmp;

    /**Camera variables**/

    //a surface holder
    private SurfaceHolder sHolder;

    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    private LocationFragment mLocationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_swipe);

        //get the Surface View at the main.admin file
        sv = (SurfaceView) this.findViewById(R.id.surfaceView_safeSwipe);

        //Get a surface
        sHolder = sv.getHolder();

        //add the callback interface methods defined below as the Surface View callbacks
        sHolder.addCallback(this);

        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mLocationFragment = new LocationFragment();
        getSupportFragmentManager().beginTransaction().add(
                R.id.fragment_safeSwipe, mLocationFragment).commit();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {


        //get camera parameters
        parameters = mCamera.getParameters();

        //set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();

        //sets what code should be executed after the picture is taken
        Camera.PictureCallback mCall = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                //decode the data obtained by the camera into a Bitmap
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //set the iv_image
//                iv_image.setImageBitmap(bmp);
                saveImage(bmp);

            }

        };

        mCamera.takePicture(null, null, mCall);

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = Camera.open(1);
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //stop the preview
        mCamera.stopPreview();
        //release the camera
        mCamera.release();
        //unbind the camera from this object
        mCamera = null;
    }

    public void saveImage(Bitmap finalbitmap)
    {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File (root + CAMERA_DIR + "SafeSwipe");
        myDir.mkdirs();

        String fname = "SafeSwipe Image.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            startActivityForResult(new Intent(this, MainActivity.class),1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
