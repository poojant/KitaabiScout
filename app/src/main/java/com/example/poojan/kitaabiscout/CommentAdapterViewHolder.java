package com.example.poojan.kitaabiscout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView userComment, userName;
    FirebaseAuth auth;
    public CommentAdapterViewHolder(View itemView) {
        super(itemView);
        auth = FirebaseAuth.getInstance();
        userComment = itemView.findViewById(R.id.comment);
        userName = itemView.findViewById(R.id.userName);
    }

    public void setComment(String comment){
        userComment.setText(comment);
    }
    public void setKey(String key){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("user_details")
                .child(key).child("userName");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
