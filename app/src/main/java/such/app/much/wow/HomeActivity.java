package such.app.much.wow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static ArrayList<Photo> mPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("IIITD MEMES");
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Creating post feature will be implemented soon :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //setting up adapter
//        for(int i=0; i<10; i++){
//            Photo photo = new Photo();
//            photo.setImage("test");
//            photo.setText("This is a post no: " + Integer.toString(i));
//            photo.setTime("2 hours.");
//            mPhotos.add(photo);
//        }
        mAdapter = new RecyclerAdapter(mPhotos);
        mRecyclerView.setAdapter(mAdapter);
        //recycler view scroll listener;
        setRecyclerViewScrollListener();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.nav_header_layout)
            View headerLayout = navigationView.getHeaderView(0);
            TextView name = (TextView) headerLayout.findViewById(R.id.name);
            TextView email = (TextView) headerLayout.findViewById(R.id.email);
            name.setText(currentUser.getUsername());
            email.setText(currentUser.getEmail());
        } else {
            // show the signup or login screen
        }

        //getting posts
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        //query.whereExists("Tag");
        query.orderByAscending("createdAt");
        //query.setLimit(skipNum * 1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                //Log.d("getParseFile", "XXX");
                Log.d("getParseFile", Integer.toString(objects.size()));
                for (ParseObject p : objects) {
                    ParseFile parseFile = p.getParseFile("image");
                    String text = p.getString("text");
                    Date date = p.getDate("time");
                    Integer likes = p.getInt("likes");
                    long epoch = date.getTime();
                    //Log.d("getParseFileTime", text);
                    Photo photo = new Photo();
                    photo.setText(text);
                    photo.setLikes(likes);
                    photo.setParseFile(parseFile);
                    photo.setTime(epoch);
                    mPhotos.add(photo);
                    //mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemInserted(mPhotos.size() - 1);
                }
            }
        });

        //query.whereExists("Tag");
//        query.countInBackground(new CountCallback() {
//            public void done(int count, ParseException e) {
//                if (e == null) {
//                    Log.d("getCount", Integer.toString(count));
//                    // The count request succeeded. Run the query multiple times using the query count
//                    //int numQueries = (int) Math.ceil(count / 1000); //Gives you how many queries to run
//                    int numQueries = count;
//                    for (int skipNum = 0; skipNum < numQueries; skipNum++) {
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
//                        //query.whereExists("Tag");
//                        //query.orderByAscending("Type");
//                        //query.setLimit(skipNum * 1000);
//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> objects, ParseException e) {
//                                Log.d("getParseFile", "XXX");
//                                for (ParseObject p : objects) {
//                                    Log.d("getParseFile", Integer.toString(objects.size()));
//                                    ParseFile parseFile = p.getParseFile("image");
//                                    String text = p.getString("text");
//                                    Date date = p.getDate("time");
//                                    Photo photo = new Photo();
//                                    photo.setText(text);
//                                    photo.setParseFile(parseFile);
//                                    photo.setTime(date);
//                                    mPhotos.add(photo);
//                                    mAdapter.notifyItemInserted(mPhotos.size() - 1);
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    // The request failed
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private int getLastVisibleItemPosition() {
        return mLinearLayoutManager.findLastVisibleItemPosition();
    }

    private void setRecyclerViewScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                // TODO add requet image here.
//                if (!mImageRequester.isLoadingData() && totalItemCount == getLastVisibleItemPosition() + 1) {
//                    requestPhoto();
//                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

