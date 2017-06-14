package studio.themad.droidcamera.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import studio.themad.droidcamera.ImagePreview;
import studio.themad.droidcamera.R;

/**
 * Created by AKHIL on 14-Jun-17.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    View mView;
    private final Context context;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mView = itemView;
    }

    public void setImage(String location) {
        ImageView photo = (ImageView) mView.findViewById(R.id.photo);
        final Uri imgUri = Uri.parse(location);

        //set image
        Picasso.with(mView.getContext())
                .load(imgUri)
                .resize(320, 180)
                .rotate(90)
                .into(photo);

        //view full screen
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, ImagePreview.class);
                intent.putExtra("image_url", imgUri.toString());
                context.startActivity(intent);
            }
        });
    }
}