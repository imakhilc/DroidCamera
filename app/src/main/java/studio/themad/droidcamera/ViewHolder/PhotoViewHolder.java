package studio.themad.droidcamera.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.cert.Certificate;

import studio.themad.droidcamera.ImagePreview;
import studio.themad.droidcamera.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AKHIL on 14-Jun-17.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    View mView;
    private final Context context;
    String id;
    DatabaseReference mRef;
    private StorageReference mStorageRef;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mView = itemView;
    }

    public void setId(String id) {
        this.id = id;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Akhil").child("Photos");
        mRef = FirebaseDatabase.getInstance().getReference().child("Akhil").child("Photos").child(id);
    }

    public void setImage(String location, String cloud) {

        ImageView photo = (ImageView) mView.findViewById(R.id.photo);
        final ImageButton delete = (ImageButton) mView.findViewById(R.id.delete);
        final ImageButton upload = (ImageButton) mView.findViewById(R.id.upload);

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

        //on individual delete button press
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
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
                                //No button clicked
                                delete.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this photo?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        //cloud upload service
        if (cloud.equals("true"))
            upload.setVisibility(View.GONE);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload.setColorFilter(0xff0084ff, PorterDuff.Mode.MULTIPLY);

                StorageReference imageRef = mStorageRef.child(id + ".jpg");

                Bitmap bitmap1 = BitmapFactory.decodeFile(imgUri.getPath());
                Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 720, 405, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        upload.setVisibility(View.GONE);
                        mRef.child(id).child("cloud").setValue("true");
                    }
                });

                /*imageRef.putFile(imgUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                                upload.setVisibility(View.GONE);
                                mRef.child(id).child("cloud").setValue("true");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });*/
            }
        });
    }
}