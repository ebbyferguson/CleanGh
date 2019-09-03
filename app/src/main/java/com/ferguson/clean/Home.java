package com.ferguson.clean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.ferguson.clean.objects.TrashObj;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Home extends AppCompatActivity {

    private static final String TAG = "All Trash locations";
    private FloatingActionButton fab;
    private RecyclerView rv;
    private LinearLayout lytNoItem;
    private TrashAdapter mAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collRef = db.collection("trashes");
    String firebaseUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();

        final Intent intent = getIntent();
        firebaseUid = intent.getStringExtra("FIREBASE_UID");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 500ms
                enterReveal();
            }
        }, 500);

        /*
        * Check if there's data(trashes) in the FireStore
        * then display the list of trashes in a recycler
        * else display no item UI
        * */

        setUpRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Home.this, AddTrash.class);
//                Bundle b = null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    b = ActivityOptions.makeScaleUpAnimation(fab, 0, 0, fab.getWidth(),
//                            fab.getHeight()).toBundle();
//                }
//                startActivity(intent, b);
                presentActivity(view);
            }
        });
    }

    private void initComponents(){
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        lytNoItem = (LinearLayout) findViewById(R.id.lyt_no_item);
    }

    @SuppressLint("RestrictedApi")
    private void enterReveal() {
        // previously invisible view
        FloatingActionButton myView = findViewById(R.id.fab_add);

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            myView.setVisibility(View.INVISIBLE);
        }
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, AddTrash.class);
        intent.putExtra(AddTrash.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AddTrash.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("FIREBASE_UID",firebaseUid);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    public void setUpRecyclerView(){
        Query query = collRef.orderBy("time_stamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TrashObj> options = new FirestoreRecyclerOptions.Builder<TrashObj>()
                .setQuery(query, TrashObj.class)
                .build();

        mAdapter = new TrashAdapter(options);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);

        rv.setVisibility(View.VISIBLE);
        lytNoItem.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}
