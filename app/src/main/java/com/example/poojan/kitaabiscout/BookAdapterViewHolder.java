package com.example.poojan.kitaabiscout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookAdapterViewHolder extends RecyclerView.ViewHolder {

    Context context;
    TextView bookTitle, bookAuthor, bookGenre, bookRating;
    ImageView bookImage, bookmark, share;
    FirebaseAuth auth;
    DatabaseReference dbref;
    String key, title, author, genre, genreBasedBookKey;
    Float rating;
    Long id;
    public BookAdapterViewHolder(View itemView) {
        super(itemView);
        bookTitle = itemView.findViewById(R.id.bookTitle);
        bookAuthor = itemView.findViewById(R.id.bookAuthor);
        bookGenre = itemView.findViewById(R.id.bookGenre);
        bookRating = itemView.findViewById(R.id.bookRating);
        bookImage = itemView.findViewById(R.id.bookImage);
        bookmark = itemView.findViewById(R.id.bookmark);
        share = itemView.findViewById(R.id.share);

        auth = FirebaseAuth.getInstance();
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref = FirebaseDatabase.getInstance().getReference().child("bookmarks").child(auth.getUid()).child(key);
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            dataSnapshot.getRef().removeValue();
                            bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                        }else{
                            dbref.setValue(Boolean.TRUE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I recommend *" + title + "* a " +
                        genre + " book, written by " +
                        author + ".\nBook Rating by KitaabiScout : " + rating.toString());
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                //startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    public void setContext(Context context){
        this.context = context;
    }
    public void setTitle(String title){
        this.title = title;
        bookTitle.setText(title);
    }
    public void setAuthor(String author){
        this.author = author;
        bookAuthor.setText(author);
    }
    public void setGenre(String genre){
        this.genre = genre;
        bookGenre.setText(genre);
    }
    public void setAvgRating(Float avgRating){
        rating = avgRating;
        bookRating.setText(String.valueOf(avgRating));
    }
    public void setImage(String imgUri){
        Glide.with(context).load(imgUri).into(bookImage);
    }

    public void setKey(String key){
        this.key = key;
        dbref = FirebaseDatabase.getInstance().getReference().child("bookmarks").child(auth.getUid()).child(key);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    bookmark.setImageResource(R.drawable.ic_bookmark_yellow_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
