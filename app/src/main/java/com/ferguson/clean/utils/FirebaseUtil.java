package com.ferguson.clean.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ferguson.clean.objects.TrashObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FirebaseUtil {

    public static FirebaseFirestore mDB;
    public static DocumentReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mStorageReference;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    public static ArrayList<TrashObj> mTrashObj;
    private static Activity caller;
    private static final int RC_SIGN_IN = 123;
    public static boolean isAdmin;

    private FirebaseUtil() {
    }

    public static void openFbReference(String reference, final Activity callingActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mDB = FirebaseFirestore.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callingActivity;
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null){
//                        Dashboard.signIn();
                    }else {
                        String userId = firebaseAuth.getUid();
//                        checkAdmin(userId);
                    }
                    Toast.makeText(callingActivity.getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();
                }
            };
            connectStorage();
        }
        mTrashObj = new ArrayList<TrashObj>();
        mDatabaseReference = mDB.collection(reference).document();
    }


    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static void dettachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
/*    private static void checkAdmin(String uid){
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(uid);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                isAdmin = true;
                Log.d("Admin", "This user is an admin");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(childEventListener);
    }*/

    public static void connectStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("deals_pictures");

    }
}
