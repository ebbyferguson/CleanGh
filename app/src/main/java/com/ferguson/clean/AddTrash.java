package com.ferguson.clean;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ferguson.clean.utils.ConnectivityHelper;
import com.ferguson.clean.utils.FirebaseUtil;
import com.ferguson.clean.utils.TimeAgo;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddTrash extends AppCompatActivity {

    public static final int REQUEST_CODE_FOR_MAP = 555;
    private AppCompatTextView txtLocation;
    private AppCompatButton btnAddImage;
    private AppCompatButton btnDone;
    private LinearLayout lytBack;
    private AppCompatImageButton btnBack;
    private AppCompatImageView imgView;
    private ConstraintLayout lytLocation;
    private AppCompatButton btnAddLocation;
    private AppCompatTextView txtEditLocation;
    private TextInputEditText txtComments;
    private static final String IMAGE_DIRECTORY = "/cleangh";
    private int GALLERY = 1, CAMERA = 2;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    TrashObj trashObj;
    GeoFire geoFire;
    private int revealX;
    private int revealY;
    private String bundleExtraLat;
    private String bundleExtraLong;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trash);
        initComponents();

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;


        final Intent intent = getIntent();



        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }



        txtLocation.setPaintFlags(txtLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Get permissions to access gallery, storage and camera
        requestMultiplePermissions();

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        lytBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unRevealActivity();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unRevealActivity();
            }
        });
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectivityHelper.isConnectedToNetwork(AddTrash.this)) {
                    //Show the connected screen
                    Intent intent  = MapsActivity.makeIntent(AddTrash.this);
                    startActivityForResult(intent, REQUEST_CODE_FOR_MAP);
                } else {
                    //Show disconnected screen
                    showNoInternetDialog();
                }

            }
        });
        txtEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = MapsActivity.makeIntent(AddTrash.this);
                startActivityForResult(intent, REQUEST_CODE_FOR_MAP);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Long tsLong = System.currentTimeMillis();
//                Log.i("Current time :: ", tsLong.toString());
//                String oldT = "1566316258891";
//                long oldTime = Long.parseLong(oldT);
//                String timeAgo = TimeAgo.timeAgo(oldTime);
//                Log.i("Time Ago is :: ", timeAgo);

//                saveTrashLocation(String.valueOf(oldTime),"userId", bundleExtraLat, bundleExtraLong);

                FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                DatabaseReference dbref = fdb.getReference();


//                dbref.child("trash").setValue(TrashObj.class);
                geoFire = new GeoFire(dbref.child("geofire"));
                geoFire.setLocation("firebase-hq", new GeoLocation(Double.parseDouble(bundleExtraLat), Double.parseDouble(bundleExtraLong)), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }

//                    @Override
//                    public void onComplete(String key, FirebaseError error) {
//                        if (error != null) {
//                            System.err.println("There was an error saving the location to GeoFire: " + error);
//                        } else {
//                            System.out.println("Location saved on server successfully!");
//                        }
//                    }
                });


            }
        });

    }

    private void initComponents(){
        txtLocation = (AppCompatTextView) findViewById(R.id.txt_location);
        btnAddImage = (AppCompatButton) findViewById(R.id.btn_add_image);
        btnBack = (AppCompatImageButton) findViewById(R.id.btn_back);
        lytBack = (LinearLayout) findViewById(R.id.lyt_back);
        imgView = (AppCompatImageView) findViewById(R.id.img_trash);
        lytLocation = (ConstraintLayout) findViewById(R.id.lyt_location);
        btnAddLocation = (AppCompatButton) findViewById(R.id.btn_add_location);
        txtEditLocation = (AppCompatTextView) findViewById(R.id.btn_edit_location);
        txtComments = (TextInputEditText) findViewById(R.id.txt_comments);
        btnDone = (AppCompatButton) findViewById(R.id.btn_done);
        rootLayout = findViewById(R.id.root);
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    //image, user_id, trash_id, comment, latitude, longitude, timestamp

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }


        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(AddTrash.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgView.setImageBitmap(bitmap);
                    btnAddImage.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddTrash.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(AddTrash.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_FOR_MAP){
            String placeName = data.getStringExtra("USER_PLACE_NAME");
            bundleExtraLat = data.getStringExtra("USER_LOCATION_LAT");
            bundleExtraLong = data.getStringExtra("USER_LOCATION_LONG");
            txtLocation.setText(placeName);
            lytLocation.setVisibility(View.VISIBLE);
            btnAddLocation.setVisibility(View.GONE);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
//                            openSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void saveTrash(String timeStamp, String userId, String lat, String lng){
//        trashObj.setUserId(userId);
//        trashObj.setTimeStamp(timeStamp);
//        trashObj.setImgUrl("something");
//        trashObj.setLongLocation(lng);
//        trashObj.setLatLocation(lat);
//        trashObj.setTrashId("1TID");
//        trashObj.setComment(txtComments.getText().toString());
    }


    public void showNoInternetDialog() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check your internet connection and try again");

        // add a button
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
