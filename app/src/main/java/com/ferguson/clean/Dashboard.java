package com.ferguson.clean;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ferguson.clean.locationPing.MapsActivity;
import com.ferguson.clean.quiz.MainGameActivity;
import com.ferguson.clean.utils.Tools;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Arrays;
import java.util.List;

import hotchemi.android.rate.AppRate;


public class Dashboard extends AppCompatActivity {

    private View lyt_parent;
    private LinearLayout lytPlasticWaste;
    private LinearLayout lytGame;
    private LinearLayout lytLocator;
    private static final int RC_SIGN_IN = 123;
    public static FirebaseFirestore mDB;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mStorageReference;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView txtHighScore;


    public Dashboard() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initToolbar();
        initComponent();

        mDB = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    signIn();
                }else {
                    String userId = firebaseAuth.getUid();
//                        checkAdmin(userId);
                }
                Toast.makeText(Dashboard.this, "Welcome Back", Toast.LENGTH_LONG).show();
            }
        };
        connectStorage();

        //Get user to rate app
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(Dashboard.this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_dashboard_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    private void initComponent() {
        lyt_parent = findViewById(R.id.lyt_parent);
        lytPlasticWaste = findViewById(R.id.lyt_plastic_waste);
        lytGame = findViewById(R.id.lyt_game);
        lytLocator = findViewById(R.id.lyt_locator);
        txtHighScore = findViewById(R.id.txt_highscore);

        lytPlasticWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Home.class);
                intent.putExtra("FIREBASE_UID", mFirebaseAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        lytGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MainGameActivity.class);
                startActivity(intent);
//                loadHighScore();
            }
        });

        lytLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        loadHighScore();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        Tools.changeMenuIconColor(menu, Color.BLACK);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void connectStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("deals_pictures");
    }

    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void loadHighScore(){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+".game",Context.MODE_PRIVATE);

        int highScore = sharedPreferences.getInt("high_score",0);
        txtHighScore.setText(Integer.toString(highScore));
    }

    @Override
    protected void onRestart() {
        loadHighScore();
        super.onRestart();
    }

    @Override
    protected void onStart() {
        loadHighScore();
        super.onStart();
    }


}
