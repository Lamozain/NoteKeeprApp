package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static androidx.core.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private LinearLayoutManager mNotesLayoutManager;
    private RecyclerView mMRecyclerItems;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private GridLayoutManager mCoursesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_notes, R.id.nav_courses)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        initializeDisplayContent();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        mMRecyclerItems = findViewById(R.id.list_items);
        mNotesLayoutManager = new LinearLayoutManager(this);
        mCoursesLayoutManager = new GridLayoutManager(this, 2);


        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);
        displayNotes();

    }

    private void displayNotes() {
        mMRecyclerItems.setLayoutManager(mNotesLayoutManager);
        mMRecyclerItems.setAdapter(mNoteRecyclerAdapter);

        selectNavigationMenuItem(R.id.nav_notes);

    }

    private void selectNavigationMenuItem(int id) {
        mNavigationView = findViewById(R.id.nav_view);
        Menu menu = mNavigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    private void displayCourses() {
        mMRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mMRecyclerItems.setAdapter(mCourseRecyclerAdapter);
        selectNavigationMenuItem(R.id.nav_courses);
    }

    @Override
    public void onBackPressed() {
       mDrawer = findViewById(R.id.drawer_layout);
       if (mDrawer.isDrawerOpen(START)) {
           mDrawer.closeDrawer(START);
       } else {
           super.onBackPressed();
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_notes) {
            displayNotes();
        } else if (id == R.id.nav_courses) {
            displayCourses();
        } else if (id == R.id.nav_share) {
            handleSelection("Do you still need to share?");
        } else if (id == R.id.nav_send) {
            handleSelection("Shall we begin sending?");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(START);
        return true;


    }

    private void handleSelection(String message) {
        View view = findViewById(R.id.list_items);
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
