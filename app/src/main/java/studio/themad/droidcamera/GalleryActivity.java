package studio.themad.droidcamera;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import studio.themad.droidcamera.Model.Photo;
import studio.themad.droidcamera.ViewHolder.PhotoViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class GalleryActivity extends Activity {

    Activity context;
    DatabaseReference mRef;
    RecyclerView imageRecycler;
    Boolean alter = true;
    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        context = this;

        imageRecycler = (RecyclerView) findViewById(R.id.imageRecycler);
        imageRecycler.setNestedScrollingEnabled(false);
        imageRecycler.setHasFixedSize(true);

        mRef = FirebaseDatabase.getInstance().getReference();

        mAdapter = new FirebaseRecyclerAdapter<Photo, PhotoViewHolder>(Photo.class, R.layout.item_gallery, PhotoViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(PhotoViewHolder viewHolder, Photo photo, int position) {
                viewHolder.setImage(photo.getLocation(), alter);
                alter = !alter;
            }
        };
        imageRecycler.setAdapter(mAdapter);

        LinearLayoutManager mManager;
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        imageRecycler.setLayoutManager(mManager);

    }
}
