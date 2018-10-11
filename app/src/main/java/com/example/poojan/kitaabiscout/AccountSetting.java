package com.example.poojan.kitaabiscout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSetting extends AppCompatActivity {

    TextView accName, favGen1, favGen2, accEmail;
    DatabaseReference dbref;
    FirebaseAuth auth;
    public ProgressDialog dialog;
    AlertDialog.Builder AlertName;
    EditText edittext;
    RelativeLayout genList1, genList2;
    String oldGenreSave;
    Button gen1Save, gen1Cancel, gen2Save, gen2Cancel;
    LinearLayout gen1SC, gen2SC;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        toolbar = findViewById(R.id.accout_setting);
        toolbar.setTitle("Account Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = new ProgressDialog(AccountSetting.this);
        dialog.setMessage("Updating please wait...");

        auth = FirebaseAuth.getInstance();
        accName = findViewById(R.id.accName);
        favGen1 = findViewById(R.id.accGen1);
        favGen2 = findViewById(R.id.accGen2);
        accEmail = findViewById(R.id.email);
        genList1 = findViewById(R.id.genList1);
        genList2 = findViewById(R.id.genList2);
        gen1Save = findViewById(R.id.gen1Save);
        gen1Cancel = findViewById(R.id.gen1Cancel);
        gen2Save = findViewById(R.id.gen2Save);
        gen2Cancel = findViewById(R.id.gen2Cancel);
        gen1SC = findViewById(R.id.gen1SC);
        gen2SC = findViewById(R.id.gen2SC);

        dbref = FirebaseDatabase.getInstance().getReference().child("user_details").child(auth.getUid());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                accName.setText(user.getUserName().toString());
                accEmail.setText(user.getUserEmail().toString());
                if(user.getFavGenre1()!=null){
                    favGen1.setText(user.getFavGenre1().toString());
                }
                if(user.getFavGenre2()!=null){
                    favGen2.setText(user.getFavGenre2().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        accName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                dialog.show();
                AlertName = new AlertDialog.Builder(AccountSetting.this);
                edittext = new EditText(AccountSetting.this);
                edittext.setText(accName.getText().toString());
                AlertName.setTitle("Change User Name");
                //alert.setMessage("Enter Your Title");
                AlertName.setView(edittext);
                AlertName.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        final String newName = String.valueOf(edittext.getText());
                        DatabaseReference accountref = FirebaseDatabase.getInstance().getReference()
                                .child("user_details")
                                .child(auth.getUid())
                                .child("userName");

                        accountref.setValue(newName);
                        finish();
                        startActivity(getIntent());
                        dialog.dismiss();
                    }
                });

                AlertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });
                AlertName.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialog.dismiss();
                    }
                });
                AlertName.show();
            }
        });

        genList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(AccountSetting.this, genList1);
                // populate menu with 7 options
                popupMenu.getMenu().add(1, 1, 1, "Autobiography");
                popupMenu.getMenu().add(1, 2, 2, "Comedy");
                popupMenu.getMenu().add(1, 3, 3, "Fiction");
                popupMenu.getMenu().add(1, 4, 4, "Horror");
                popupMenu.getMenu().add(1, 5, 5, "Romanace");
                popupMenu.getMenu().add(1, 6, 6, "Thriller");
                popupMenu.getMenu().add(1, 7, 7, "Novel");
                // show the menu
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        oldGenreSave = storeOldGenre(favGen1);
                        gen1SC.setVisibility(View.VISIBLE);
                        switch(menuItem.getItemId()){
                            case 1 : favGen1.setText("Autobiography");
                                     break;
                            case 2 : favGen1.setText("Comedy");
                                break;
                            case 3 : favGen1.setText("Fiction");
                                break;
                            case 4 : favGen1.setText("Horror");
                                break;
                            case 5 : favGen1.setText("Romance");
                                break;
                            case 6 : favGen1.setText("Thriller");
                                break;
                            case 7 : favGen1.setText("Novel");
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        gen1Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user_details")
                        .child(auth.getUid()).child("favGenre1");
                db.setValue(favGen1.getText().toString());
                gen1SC.setVisibility(View.GONE);
            }
        });
        gen1Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favGen1.setText(oldGenreSave);
                gen1SC.setVisibility(View.GONE);
            }
        });

        genList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(AccountSetting.this, genList2);
                // populate menu with 7 options
                popupMenu.getMenu().add(1, 1, 1, "Autobiography");
                popupMenu.getMenu().add(1, 2, 2, "Comedy");
                popupMenu.getMenu().add(1, 3, 3, "Fiction");
                popupMenu.getMenu().add(1, 4, 4, "Horror");
                popupMenu.getMenu().add(1, 5, 5, "Romanace");
                popupMenu.getMenu().add(1, 6, 6, "Thriller");
                popupMenu.getMenu().add(1, 7, 7, "Novel");
                // show the menu
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        oldGenreSave = storeOldGenre(favGen2);
                        gen2SC.setVisibility(View.VISIBLE);
                        switch(menuItem.getItemId()){
                            case 1 : favGen2.setText("Autobiography");
                                break;
                            case 2 : favGen2.setText("Comedy");
                                break;
                            case 3 : favGen2.setText("Fiction");
                                break;
                            case 4 : favGen2.setText("Horror");
                                break;
                            case 5 : favGen2.setText("Romance");
                                break;
                            case 6 : favGen2.setText("Thriller");
                                break;
                            case 7 : favGen2.setText("Novel");
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        gen2Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user_details")
                        .child(auth.getUid()).child("favGenre2");
                db.setValue(favGen2.getText().toString());
                gen2SC.setVisibility(View.GONE);
            }
        });
        gen2Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favGen2.setText(oldGenreSave);
                gen2SC.setVisibility(View.GONE);
            }
        });
    }



    String storeOldGenre(TextView txt){
        return txt.getText().toString();
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(AccountSetting.this,MainActivity.class);
        startActivity(i);
        finish();

    }
}
