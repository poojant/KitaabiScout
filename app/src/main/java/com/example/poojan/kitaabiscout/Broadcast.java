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
