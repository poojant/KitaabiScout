package com.example.poojan.kitaabiscout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bookmark extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private FirebaseAuth auth;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Boolean, BookAdapterViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        toolbar = findViewById(R.id.bookmark_toolbar);
        toolbar.setTitle("Bookmarks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_bookmarkbookList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("bookmarks").child(auth.getUid());
        dbref.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<Boolean, BookAdapterViewHolder>
                (Boolean.class , R.layout.book_list_item,
                        BookAdapterViewHolder.class, dbref) {

            public void populateViewHolder(final BookAdapterViewHolder bookAdapterViewHolder,
                                           final Boolean model, final int position) {
                String key = this.getRef(position).getKey().toString();
                Log.d("key",key);
                Log.d("Position", String.valueOf(position));

                bookAdapterViewHolder.setKey(key);

                DatabaseReference dbBook = FirebaseDatabase.getInstance().getReference().child("books").child(key);
                dbBook.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        bookAdapterViewHolder.setContext(getApplicationContext());
                        bookAdapterViewHolder.setTitle(book.getTitle());
                        bookAdapterViewHolder.setAuthor(book.getAuthors());
                        bookAdapterViewHolder.setGenre(book.getGenre());
                        bookAdapterViewHolder.setAvgRating(book.getAverage_rating());
                        bookAdapterViewHolder.setImage(book.getImage_url());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();
        return true;
    }
}
