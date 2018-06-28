package com.ecodeem.ecodeem.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecodeem.ecodeem.Models.Photo;
import com.ecodeem.ecodeem.Models.UserAccountSettings;
import com.ecodeem.ecodeem.Models.UserSettings;
import com.ecodeem.ecodeem.R;
import com.ecodeem.ecodeem.Utils.BottomNavigationViewHelper;
import com.ecodeem.ecodeem.Utils.FirebaseMethods;
import com.ecodeem.ecodeem.Utils.SectionsPagerAdapter;
import com.ecodeem.ecodeem.Utils.UniversalImageLoader;
import com.ecodeem.ecodeem.ViewPostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private TextView mPosts, mConnections, mEcoCredit, mDisplayName, mUsername, mEditProfileTV, mDescription, mLocation;
    private CircleImageView mProfilePhoto;
    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting.");

        mFirebaseMethods = new FirebaseMethods(mContext);


        setupToolbar();
        setUpWidgets();
        setupViewPager();
        setupFirebaseAuth();
        sendToEditProfile();
        setupBottomNavigationView();
    }

    private void sendToEditProfile() {

        mEditProfileTV = (TextView) findViewById(R.id.editProfileTV);
        mEditProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile));
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
                ProfileActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }


    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent settingsIntent = new Intent(mContext, SettingsActivity.class);
                startActivity(settingsIntent);
                ProfileActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }

   private void setUpWidgets(){

        //Instantiating the Widgets...
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mUsername = (TextView) findViewById(R.id.profileName);
        mDescription = (TextView) findViewById(R.id.profileStatus);
        mLocation = (TextView)findViewById(R.id.location);
        mEditProfileTV = (TextView) findViewById(R.id.editProfileTV);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mPosts = (TextView) findViewById(R.id.profilePostTV);
        mEcoCredit = (TextView) findViewById(R.id.ecoCreditTV);
        mConnections = (TextView) findViewById(R.id.profileConnectionTV);
        mProgressBar = new ProgressBar(mContext);

    }


    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mLocation.setText(settings.getLocation());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mEcoCredit.setText(String.valueOf(settings.getEco_credit()));
        mConnections.setText(String.valueOf(settings.getConnections()));
        mProgressBar.setVisibility(View.GONE);
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostsFragment()); //index 0
        adapter.addFragment(new EcoDirectFragment()); //index 1
        adapter.addFragment(new AwardsFragment()); //index 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.profileTabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.profile_posts);
        tabLayout.getTabAt(1).setText(R.string.eco_direct);
        tabLayout.getTabAt(2).setText(R.string.profile_awards);
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));


                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
