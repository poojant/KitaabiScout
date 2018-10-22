package com.example.poojan.kitaabiscout;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFeedback extends AppCompatActivity {

    DatabaseReference ref;
    FirebaseAuth auth;
    DatabaseReference dbRating;
    Button aBtn;
    TextView bookTitle, bookAuthor, bookGenre, bookRating;
    ImageView bookImage, share, upload;
    Float rating, prevRating;
    LinearLayout linearLayout4;
    EditText commentbox;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<String, CommentAdapterViewHolder> adapter;
    String key, title, author, genre, genreBasedBookKey, amazonUri, imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            key = bundle.getString("key");
            title = bundle.getString("title");
            author = bundle.getString("author");
            genre = bundle.getString("genre");
            imgUri = bundle.getString("imgUri");
            amazonUri = bundle.getString("amazonUri");
            rating = bundle.getFloat("rating");
        }
        toolbar = findViewById(R.id.broadcast_toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.rv_bookList);

        auth = FirebaseAuth.getInstance();

        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookGenre = findViewById(R.id.bookGenre);
        bookRating = findViewById(R.id.bookRating);
        bookImage = findViewById(R.id.bookImage);
        share = findViewById(R.id.share);
        aBtn = findViewById(R.id.aBtn);
        linearLayout4 = findViewById(R.id.linearLayout4);
        upload = findViewById(R.id.upload);
        commentbox = findViewById(R.id.commentbox);

        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookGenre.setText(genre);
        bookRating.setText(rating.toString());
        Glide.with(this).load(imgUri).into(bookImage);

        final RatingBar rb = (RatingBar)findViewById(R.id.userRating);

        prevRating = rb.getRating();
        dbRating = FirebaseDatabase.getInstance().getReference().child("ratings").child(auth.getUid()).child(key);
        dbRating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rb.setRating(dataSnapshot.getValue(Float.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                //startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        aBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(amazonUri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                        .child("books")
                        .child(key);
                ref = FirebaseDatabase.getInstance().getReference().child("ratings").child(auth.getUid()).child(key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Book book = dataSnapshot.getValue(Book.class);
                                    double count = book.getRatings_count();
                                    double rate = book.getAverage_rating();
                                    rate = ((rate*count)-(prevRating)+(rating))/(count);
                                    ref.setValue(rating);
                                    rb.setRating(rating);
                                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("books");
                                    dbref.child(key).child("average_rating").setValue(rate);
                                    Toast.makeText(getApplicationContext(),"Rating is: "+String.valueOf(rating), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                            ref.setValue(rating);
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Book book = dataSnapshot.getValue(Book.class);
                                    double count = book.getRatings_count();
                                    double rate = book.getAverage_rating();
                                    rate = ((rate*count)+(rating))/(count+1);
                                    count = count + 1;
                                    rb.setRating(rating);
                                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("books");
                                    dbref.child(key).child("average_rating").setValue(rate);
                                    dbref.child(key).child("ratings_count").setValue(count);

                                    Toast.makeText(getApplicationContext(),"Rating is: "+String.valueOf(rating), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                        .child("comments")
                        .child(key).child(auth.getUid());
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(UserFeedback.this,"Sorry only one comment is allowed",Toast.LENGTH_LONG).show();
                        }else{
                            db.setValue(commentbox.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        loaddata();
    }
    public void loaddata(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("comments").child(key);
        dbref.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<String, CommentAdapterViewHolder>
                (String.class, R.layout.comment_list_item,
                        CommentAdapterViewHolder.class,
                        dbref) {

            public void populateViewHolder(final CommentAdapterViewHolder commentAdapterViewHolder,
                                           final String str, final int position) {
                String key = this.getRef(position).getKey().toString();
                Log.d("key",key);
                Log.d("Position", String.valueOf(position));

                commentAdapterViewHolder.setKey(key);
                commentAdapterViewHolder.setComment(str);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();
        return true;
    }

}
