package studio.themad.droidcamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import android.app.Activity;


public class CameraActivity extends Activity {
    private Camera mCamera;
    private CamPreview mPreview;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;

        //Camera permission request for Marshmallow  devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            //start camera preview if permitted
            startPreview();
        }
    }

    public void startPreview() {
        mCamera = getCameraInstance();

        mPreview = new CamPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.preview);
        preview.addView(mPreview);
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            startPreview();
        }
    }
}