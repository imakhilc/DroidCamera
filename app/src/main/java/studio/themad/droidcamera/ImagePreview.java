package studio.themad.droidcamera;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImagePreview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepreview);

        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //url passed through intent
        String url = getIntent().getStringExtra("image_url");

        ImageView preview = (ImageView) findViewById(R.id.preview);

        Picasso.with(this)
                .load(url)
                .rotate(90)
                .resize(1024, 576)
                .into(preview);
    }
}
