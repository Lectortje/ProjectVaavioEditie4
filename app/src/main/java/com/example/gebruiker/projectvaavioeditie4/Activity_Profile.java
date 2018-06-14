package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Activity_Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;
    private ImageView mIVNavHeader;
    private TextView mTV1NavHeader;
    private TextView mTV2NavHeader;
    private StorageReference mStorage;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Taking the toolbar and set it as the actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Declaring the drawer and navigationview. Also making the current page selected in the drawer menu
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Making the drawer menu 'hamburger' and adding it to the actionbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setting the nav_header of the drawer menu, using the layout created.
        View hView = navigationView.inflateHeaderView(R.layout.nav_header);

        // Setting up the firebase connection, getting the current user, if present
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            // When a user is logged in, the user ID gets put in a string
            UserID = user.getUid();

            String userID = UserID.toString();

            // Declaring the image view of the nav header
            mIVNavHeader = (ImageView) hView.findViewById(R.id.ImageViewNavHeader);

            // Getting the profile image of the user, if it has uploaded one. When successful, the image view in het nav header gets replaced
            // By the profile picture of the user. Picasso is used fot this processed. The image get loaded from the uri, fit in the image view
            // and cropped centerly into the view.
            mStorage.child("Profile photos/" + userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Picasso.get().load(uri).fit().centerCrop().into(mIVNavHeader);
                }
            });

            // Declaring the TextView's of the nav header of the drawer menu
            mTV1NavHeader = (TextView) hView.findViewById(R.id.TextViewNaamNavHeader);
            mTV2NavHeader = (TextView) hView.findViewById(R.id.TextViewEmailNavHeader);

            // addValueEventListener to change the default text view to the name and email adres of the user, if it has provided these in the
            // profile information section of the account. A dataSnapshot is used to get the name and email, which get put in a string, after which the
            // string gets put in het Text View.
            myRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String naam = dataSnapshot.child("Users").child(UserID).child("Naam").getValue(String.class);
                    String email = dataSnapshot.child("Users").child(UserID).child("Email").getValue(String.class);

                    mTV1NavHeader.setText(naam);
                    mTV2NavHeader.setText(email);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }

        // Making the nav_home button in the drawer menu selected on start up
        navigationView.setCheckedItem(R.id.nav_home);

        // Because the page only consists of a empty fragment container, the activity ahs to be loaded with a fragment.
        // So when loading the activity, the fragment gets replaced with the profile fragment.
        // The profile button in the drawer menu also gets selected.
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_Account()).commit();
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    // The cases for the items in the Navigation drawer. When clicking on an item in the menu, the method corresponding with
    // The clicked item will be executed.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                // Intent to switch form the current activity to the intended activity, in this case the Startscherm
                // When executing this event, it will also clear the activity stack so that there ar no activity's
                // Stacking up. When the intended activity is started, the activity which will be left will also be closed.
                Intent navhome = new Intent(this, Activity_Homescreen.class);
                navhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navhome);
                finish();
                break;
            case R.id.nav_filters:
                Toast.makeText(this, "Filters", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_av:
                // Intent that redirects the user to the Vaavio website outside the app
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
                break;
            case R.id.nav_profile:
                Intent navprofile = new Intent(this, Activity_Login.class);
                navprofile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navprofile);
                finish();
                break;
            case R.id.nav_over_ons:
                Intent overons = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/over-ons/"));
                startActivity(overons);
                break;
            case R.id.nav_contact:
                Intent contact = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/contact/"));
                startActivity(contact);
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
        }
        // After an item is clicked in the menu, the drawer will close itself so you can see the activity/fragment
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // If the drawer is open, the back button will instead of going back to the previous page, close the drawer.
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        // if the drawer isn't open, the back button will operate just as normal.
        {
            super.onBackPressed();
        }
    }
}
