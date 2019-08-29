package com.ferguson.clean;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferguson.clean.Objects.TrashObj;
import com.ferguson.clean.utils.TimeAgo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class TrashAdapter extends FirestoreRecyclerAdapter<TrashObj, TrashAdapter.TrashHolder>{


    public TrashAdapter(@NonNull FirestoreRecyclerOptions<TrashObj> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrashHolder holder, int position, @NonNull TrashObj model) {
        holder.comment.setText(model.getComment());
        holder.placeName.setText(model.getPlace_name());
        try {
            holder.timeAgo.setText(TimeAgo.timeAgo(model.getTime_stamp().getTime()));
        }catch (NullPointerException e){

        }
        try {
            if (model.getImg_url().isEmpty() || model.getImg_url() == "" || model.getImg_url() == null) {
                holder.trashImg.setImageResource(R.drawable.ic_trash);
            } else{
                Picasso.get().load(model.getImg_url()).into(holder.trashImg);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public TrashHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_item,
                parent, false);
        return new TrashHolder(v);
    }

    class TrashHolder extends RecyclerView.ViewHolder{

        TextView comment;
        TextView placeName;
        TextView timeAgo;
        ImageView trashImg;

        public TrashHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            placeName = itemView.findViewById(R.id.placeName);
            timeAgo = itemView.findViewById(R.id.timeAgo);
            trashImg = itemView.findViewById(R.id.image);
        }
    }
}
