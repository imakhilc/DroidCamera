package studio.themad.droidcamera.ViewHolder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import studio.themad.droidcamera.R;

/**
 * Created by AKHIL on 14-Jun-17.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setImage(String location, Boolean alter) {
        ImageView photo = (ImageView) mView.findViewById(R.id.photo);
        Uri imgUri = Uri.parse(location);

        if (alter) {
            Picasso.with(mView.getContext())
                    .load(imgUri)
                    .resize(200, 200)
                    .into(photo);
        } else {
            Picasso.with(mView.getContext())
                    .load(imgUri)
                    .resize(200, 200)
                    .into(photo);
        }
    }
}