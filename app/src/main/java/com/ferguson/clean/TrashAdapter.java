package com.ferguson.clean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ferguson.clean.utils.FirebaseUtil;
import com.ferguson.clean.utils.TimeAgo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder>{

    ArrayList<TrashObj> trash;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ImageView imageTrash;

    public TrashAdapter(Home activity){
        FirebaseUtil.openFbReference("traveldeals", activity);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        trash = FirebaseUtil.mTrashObj;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                TrashObj td = dataSnapshot.getValue(TrashObj.class);
//
////                Log.d("Deal :: ", td.getTitle());
//                td.setTrashId(dataSnapshot.getKey());
//                trash.add(td);
//                notifyItemInserted(trash.size()-1);
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
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.trash_item, parent, false);

        return new TrashViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, int position) {
        TrashObj trashObj = trash.get(position);
        holder.bind(trashObj);
    }

    @Override
    public int getItemCount() {
        return trash.size();
    }

    public class TrashViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTimeAgo;
        TextView txtPlaceName;
        TextView txtComment;
        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeAgo = itemView.findViewById(R.id.timeAgo);
            txtPlaceName = itemView.findViewById(R.id.placeName);
            txtComment = itemView.findViewById(R.id.comment);
            imageTrash = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        public void bind(TrashObj tObj){

            txtTimeAgo.setText(TimeAgo.timeAgo(tObj.getTimeStamp()));
            txtPlaceName.setText("Airport City, Stanbic Heights");
            txtComment.setText(tObj.getComment());
            showImage(tObj.getImgUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TrashObj selectedTrash = trash.get(position);
            Intent intent = new Intent(view.getContext(), TrashActivity.class);
            intent.putExtra("Trash", selectedTrash);
            view.getContext().startActivity(intent);
        }
    }

    private void showImage(String url){
        if (url != null && !url.isEmpty()){
            Picasso.with(imageTrash.getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageTrash);
        }
    }
}
