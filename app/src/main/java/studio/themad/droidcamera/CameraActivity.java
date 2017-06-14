package studio.themad.droidcamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import studio.themad.droidcamera.Model.Photo;

import static android.content.ContentValues.TAG;


public class CameraActivity extends Activity implements View.OnClickListener {

    private Camera mCamera;
    private CamPreview mPreview;
    ImageButton capture, gallery, close;
    Activity context;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;

        //hide notification and navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Request permission and start Preview
        checkPermission();

        //declare views
        capture = (ImageButton) findViewById(R.id.capture);
        gallery = (ImageButton) findViewById(R.id.gallery);
        close = (ImageButton) findViewById(R.id.close);

        try {
            capture.setOnClickListener(this);
            gallery.setOnClickListener(this);
            close.setOnClickListener(this);
        } catch (Exception e) {
        }
    }

    //button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture:
                mCamera.takePicture(null, null, mPicture);
                break;
            case R.id.gallery:
                startActivity(new Intent(CameraActivity.this, GalleryActivity.class));
                break;
            case R.id.close:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //hide notification and navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //checking permission starts here
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                startPreview();
            }
        } else {
            startPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }
    //checking permission ends here

    //start camera preview
    public void startPreview() {
        mCamera = getCameraInstance();

        mPreview = new CamPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.preview);
        preview.addView(mPreview);
    }

    //get camera instance
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            //attempt to get a Camera instance
            c = Camera.open();
        } catch (Exception e) {
            //camera not available
        }
        return c;
    }

    //after pressing capture button
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mPreview.mCamera.startPreview();

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    //saving the captured image
    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DroidCamera");

        //create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        //create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        //Toast.makeText(context, mediaFile.toURI().toString(), Toast.LENGTH_SHORT).show();

        //add database entry
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Akhil").child("Photos");
        Photo photo = new Photo(mediaFile.toURI().toString());
        mRef.push().setValue(photo);

        return mediaFile;
    }
}