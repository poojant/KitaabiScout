package com.example.poojan.kitaabiscout;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    android.support.v7.widget.Toolbar toolbar;
    public boolean closeview = false;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userName, userEmail;
    DatabaseReference dbuser;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager layoutManager;
    LinearLayout filterLayout;
    TextView filterName;
    Button applyFilter;
    String filter = "books";
    FirebaseRecyclerAdapter<Book, BookAdapterViewHolder> adapter;
    private static final int ITEM_TO_LOAD = 30;
    private int mCurrentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.feature_req_toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_bookList);
        mSwipeRefreshLayout = findViewById(R.id.swip);
        filterLayout = findViewById(R.id.filterLayout);
        filterName = findViewById(R.id.filterName);
        applyFilter = findViewById(R.id.applyFilter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();

                        if(menuItem.getItemId() == R.id.logout){
                            auth.signOut();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user == null) {
                                // user auth state is changed - user is null
                                // launch login activity
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                            return true;
                        }
                        if(menuItem.getItemId() == R.id.accSetting){
                            Intent intent = new Intent(MainActivity.this, AccountSetting.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.bookmark){
                            Intent intent = new Intent(MainActivity.this, Bookmark.class);
                            startActivity(intent);
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        navigationView.setItemIconTintList(null);
        userName = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.head_email);

        Log.d("auth_id",auth.getUid());
        dbuser = FirebaseDatabase.getInstance().getReference().child("user_details").child(auth.getUid());
        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName().toString());
                userEmail.setText(user.getUserEmail().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, filterLayout);
                // populate menu with 7 options
                popupMenu.getMenu().add(1, 1, 1, "Autobiography");
                popupMenu.getMenu().add(1, 2, 2, "Comedy");
                popupMenu.getMenu().add(1, 3, 3, "Fiction");
                popupMenu.getMenu().add(1, 4, 4, "Horror");
                popupMenu.getMenu().add(1, 5, 5, "Romanace");
                popupMenu.getMenu().add(1, 6, 6, "Thriller");
                popupMenu.getMenu().add(1, 7, 7, "Novel");
                popupMenu.getMenu().add(1,8,8,"All Genre");
                // show the menu
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case 1 : filterName.setText("Autobiography");
                                break;
                            case 2 : filterName.setText("Comedy");
                                break;
                            case 3 : filterName.setText("Fiction");
                                break;
                            case 4 : filterName.setText("Horror");
                                break;
                            case 5 : filterName.setText("Romance");
                                break;
                            case 6 : filterName.setText("Thriller");
                                break;
                            case 7 : filterName.setText("Novel");
                                break;
                            case 8 : filterName.setText("All Genre");
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filterName.getText().toString().equals("All Genre")){
                    filter = "books";
                }else{
                    filter = filterName.getText().toString();
                }
                mCurrentPage = 1;
                loaddata();
                refreshPage();
            }
        });

        loaddata();
        refreshPage();
    }

    public void loaddata(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child(filter);
        dbref.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<Book, BookAdapterViewHolder>
                (Book.class, R.layout.book_list_item,
                        BookAdapterViewHolder.class,
                        dbref.limitToLast(mCurrentPage * ITEM_TO_LOAD).orderByChild("average_rating")) {

            public void populateViewHolder(final BookAdapterViewHolder bookAdapterViewHolder,
                                           final Book book, final int position) {
                String key = this.getRef(position).getKey().toString();
                Log.d("key",key);
                Log.d("Position", String.valueOf(position));

                bookAdapterViewHolder.setKey(key);
                bookAdapterViewHolder.setContext(getApplicationContext());
                bookAdapterViewHolder.setTitle(book.getTitle());
                bookAdapterViewHolder.setAuthor(book.getAuthors());
                bookAdapterViewHolder.setGenre(book.getGenre());
                bookAdapterViewHolder.setAvgRating(book.getAverage_rating());
                bookAdapterViewHolder.setImage(book.getImage_url());
            }
        };
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refreshPage(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage ++;

                loaddata();
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }, 200);


            }
        });
    }
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (closeview) {
            closeview = false;
            finish();
            startActivity(getIntent());

        } else {
            super.onBackPressed();
        }
    }

    public void closeview(Boolean value) {
        closeview = value;
    }
}
