package com.example.poojan.kitaabiscout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Broadcast extends AppCompatActivity {

    Toolbar toolbar;
    EditText broadcast_text;
    ImageView imgMic;
    Button submit;
    FirebaseAuth auth;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        toolbar = findViewById(R.id.broadcast_toolbar);
        toolbar.setTitle("broadcast");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        broadcast_text = findViewById(R.id.broadcast_text);
        submit = findViewById(R.id.submit_broadcast);
        imgMic = findViewById(R.id.mic);

        auth = FirebaseAuth.getInstance();
        /*final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tp = FirebaseDatabase.getInstance().getReference().child("books");
        tp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    Book book = snapshot.getValue(Book.class);
                    db.child(book.getGenre()).child(key).setValue(book);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("books");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals("24")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Harry-Potter-Deathly-Hallows/dp/1408855712/ref=sr_1_1?s=books&ie=UTF8&qid=1539610627&sr=1-1&keywords=harry+potter+and+the+deathly+hallows");
                    }else if(snapshot.getKey().equals("191")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Name-Wind-Kingkiller-Chronicle/dp/0575081406/ref=sr_1_1?s=books&ie=UTF8&qid=1539610718&sr=1-1&keywords=name+of+the+wind");
                    }else if(snapshot.getKey().equals("134")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Storm-Swords-Song-Fire-Three/dp/055357342X/ref=sr_1_2?s=books&ie=UTF8&qid=1539610802&sr=1-2&keywords=a+storm+of+swords");
                    }else if(snapshot.getKey().equals("26")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Harry-Potter-Half-Blood-Prince/dp/1408855704/ref=sr_1_2?s=books&ie=UTF8&qid=1539611072&sr=1-2&keywords=harry+potter+and+the+half+blood+prince");
                    }else if(snapshot.getKey().equals("23")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Harry-Potter-Goblet-Fire/dp/1408855682/ref=sr_1_1?s=books&ie=UTF8&qid=1539611183&sr=1-1&keywords=harry+potter+and+the+goblet+of+fire");
                    }else if(snapshot.getKey().equals("17")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Harry-Potter-Prisoner-Azkaban/dp/1408855674/ref=sr_1_1?s=books&ie=UTF8&qid=1539611279&sr=1-1&keywords=harry+potter+and+the+prisoner+of+azkaban");
                    }else if(snapshot.getKey().equals("160")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Lord-Rings-Return-King/dp/0007488343/ref=sr_1_1?s=books&ie=UTF8&qid=1539611354&sr=1-1&keywords=the+return+of+the+king");
                    }else if(snapshot.getKey().equals("174")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Percy-Jackson-Last-Olympian-Book/dp/0141346884/ref=sr_1_1?s=books&ie=UTF8&qid=1539611448&sr=1-1&keywords=the+last+olympian");
                    }else if(snapshot.getKey().equals("188")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Lord-Rings-Two-Towers/dp/0007488327/ref=sr_1_6?s=books&ie=UTF8&qid=1539611532&sr=1-6&keywords=the+lord+of+the+rings");
                    }else if(snapshot.getKey().equals("38")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Game-Thrones-HBO-Tie-Song/dp/0553593714/ref=sr_1_2_sspa?s=books&ie=UTF8&qid=1539611632&sr=1-2-spons&keywords=a+game+of+thrones&psc=1");
                    }else if(snapshot.getKey().equals("30")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Help-Kathryn-Stockett/dp/0241956536/ref=sr_1_1?s=books&ie=UTF8&qid=1539611718&sr=1-1&keywords=the+help");
                    }else if(snapshot.getKey().equals("143")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Unbroken-Laura-Hillenbrand/dp/0007580576/ref=sr_1_1?s=books&ie=UTF8&qid=1539611793&sr=1-1&keywords=unbroken");
                    }else if(snapshot.getKey().equals("109")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Clash-Kings-Song-Ice-Fire/dp/0007465823/ref=sr_1_1?s=books&ie=UTF8&qid=1539611890&sr=1-1&keywords=a+clash+of+kings");
                    }else if(snapshot.getKey().equals("84")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Giving-Tree-Shel-Silverstein/dp/1846143837/ref=sr_1_1?s=books&ie=UTF8&qid=1539611957&sr=1-1&keywords=the+giving+tree");
                    }else if(snapshot.getKey().equals("111")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Me-Before-You-Jojo-Moyes/dp/0718157834/ref=sr_1_2?s=books&ie=UTF8&qid=1539612042&sr=1-2&keywords=me+before+you");
                    }else if(snapshot.getKey().equals("66")){
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/Thousand-Splendid-Suns-Khaled-Hosseini/dp/1408844443/ref=sr_1_1?s=books&ie=UTF8&qid=1539612205&sr=1-1&keywords=a+thousand+splendid+suns");
                    }else{
                        db.child(snapshot.getKey()).child("amazon_uri").setValue("https://www.amazon.in/");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        imgMic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("broadcast").child(auth.getUid());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = broadcast_text.getText().toString().trim();
                if(temp.isEmpty()){
                    Toast.makeText(Broadcast.this,"Please type something",Toast.LENGTH_LONG).show();
                }else{
                    database.push().child("message").setValue(broadcast_text.getText().toString());
                    Toast.makeText(Broadcast.this,"Message sent successfully",Toast.LENGTH_LONG).show();
                    Intent home = new Intent(Broadcast.this,MainActivity.class);
                    startActivity(home);
                }
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    broadcast_text.setText(result.get(0));
                }
                break;
            }

        }
    }
    public boolean onSupportNavigateUp(){

        onBackPressed();
        return true;
    }
}
