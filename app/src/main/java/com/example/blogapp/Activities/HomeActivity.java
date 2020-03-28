package com.example.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blogapp.Fragments.InfoFragment;
import com.example.blogapp.Fragments.MainFragment;
import com.example.blogapp.Fragments.ProfileFragment;

import com.example.blogapp.Fragments.SettingsFragment;
import com.example.blogapp.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    //FirebaseDatabase mDatabase;
    //DatabaseReference myRef;

    Boolean googleLogout = false;

    Boolean titleHome = true;
    Boolean titleProfile = false;
    Boolean titleSettings = false;
    Boolean titleInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(savedInstanceState != null){

            titleHome = savedInstanceState.getBoolean("titleHome");
            titleProfile = savedInstanceState.getBoolean("titleProfile");
            titleSettings = savedInstanceState.getBoolean("titleSettings");
            titleInfo = savedInstanceState.getBoolean("titleInfo");
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //mDatabase = FirebaseDatabase.getInstance();
        //myRef = mDatabase.getReference("Users");

        //myRef.setValue(mAuth.getCurrentUser().getEmail());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        if(currentUser.getEmail() != null && currentUser.getDisplayName() != null){

            navUserMail.setText(currentUser.getEmail());
            navUsername.setText(currentUser.getDisplayName());
        }

        if(currentUser.getPhotoUrl() != null){

            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(navUserPhoto);
        }else
            Glide.with(this).load(R.drawable.userphoto).into(navUserPhoto);

        if(savedInstanceState == null){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
        }

        if(titleHome == false && titleProfile == true && titleSettings == false && titleInfo == false){

            getSupportActionBar().setTitle("Account");
        }

        if(titleHome == true && titleProfile == false && titleSettings == false && titleInfo == false){

            getSupportActionBar().setTitle("Home");
        }

        if(titleHome == false && titleProfile == false && titleSettings == true && titleInfo == false){

            getSupportActionBar().setTitle("Settings");
        }

        if(titleHome == false && titleProfile == false && titleSettings == false && titleInfo == true){

            getSupportActionBar().setTitle("Info");
        }
    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if(menuItem.getItemId() == R.id.home){

            titleHome = true;
            titleProfile = false;
            titleSettings = false;
            titleInfo = false;

            getSupportActionBar().setTitle("Home");

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.account){

            titleHome = false;
            titleProfile = true;
            titleSettings = false;
            titleInfo = false;

            getSupportActionBar().setTitle("Account");

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ProfileFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.groups){


        }

        if(menuItem.getItemId() == R.id.info){

            titleHome = false;
            titleProfile = false;
            titleSettings = false;
            titleInfo = true;

            getSupportActionBar().setTitle("Info");

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new InfoFragment());
            fragmentTransaction.commit();


        }

        if(menuItem.getItemId() == R.id.settings){

            titleHome = false;
            titleProfile = false;
            titleSettings = true;
            titleInfo = false;

            getSupportActionBar().setTitle("Settings");

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SettingsFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.logout){

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if(account != null){

                googleLogout = true;
            }

            LoginManager.getInstance().logOut();

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            loginActivity.putExtra("googleLogout", googleLogout);
            startActivity(loginActivity);
            finish();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("titleHome", titleHome);
        outState.putBoolean("titleProfile", titleProfile);
        outState.putBoolean("titleSettings", titleSettings);
        outState.putBoolean("titleInfo", titleInfo);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("titleHome", titleHome);
        savedInstanceState.putBoolean("titleProfile", titleProfile);
        savedInstanceState.putBoolean("titleSettings", titleSettings);
        savedInstanceState.putBoolean("titleInfo", titleInfo);
    }

    public void updateNavHeaderPhoto(FirebaseUser currentUser){

        View headerView = navigationView.getHeaderView(0);

        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        if(currentUser.getPhotoUrl() != null) {

            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(navUserPhoto);
        }
    }

    public void updateNavHeaderName(FirebaseUser currentUser){

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.nav_username);

        if(currentUser.getDisplayName() != null) {

            navUsername.setText(currentUser.getDisplayName());
        }
    }

    public void updateNavHeaderMail(FirebaseUser currentUser){

        View headerView = navigationView.getHeaderView(0);

        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        if(currentUser.getEmail() != null) {

            navUserMail.setText(currentUser.getEmail());
        }
    }
}