package studio.themad.droidcamera.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;

import studio.themad.droidcamera.ImagePreview;
import studio.themad.droidcamera.R;

/**
 * Created by AKHIL on 14-Jun-17.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    View mView;
    private final Context context;
    String id;
    DatabaseReference mRef;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mView = itemView;
    }

    public void setId(String id) {
        this.id = id;
        mRef = FirebaseDatabase.getInstance().getReference().child("Akhil").child("Photos").child(id);
    }

    public void setImage(String location) {
        ImageView photo = (ImageView) mView.findViewById(R.id.photo);
        ImageButton delete = (ImageButton) mView.findViewById(R.id.delete);

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
                Intent intent = new Intent(context, ImagePreview.class);
                intent.putExtra("image_url", imgUri.toString());
                context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                File file = new File(imgUri.getPath());
                                boolean deleted = file.delete();
                                mRef.removeValue();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked - do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this photo?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}