package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Activity_Homescreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    // creating a variable and setting it as a drawer
    private DrawerLayout drawer;
    private Button mZoeken;
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
        setContentView(R.layout.activity_homescreen);

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

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header);
        mTV1NavHeader = (TextView)hView.findViewById(R.id.TextViewNaamNavHeader);
        mTV1NavHeader.setText("Test");

        mTV2NavHeader = (TextView)hView.findViewById(R.id.TextViewEmailNavHeader);
        mTV2NavHeader.setText("Test");

        mIVNavHeader = (ImageView)hView.findViewById(R.id.ImageViewNavHeader);
        Picasso.get().load("https://www.vaavio.nl/wp-content/plugins/wp-jobhunt/assets/images/img-not-found4x3.jpg").fit().centerCrop().into(mIVNavHeader);

        /* FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
            String userID = UserID.toString();

            mStorage.child("Profile photos/" + userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(mIVNavHeader);
                }
            }); */


        // Making the nav_home button in the drawer menu selected on start up
        navigationView.setCheckedItem(R.id.nav_home);

        // Setting the on click event for the zoeken button. Which redirects the user to the activity with the vacatures.
        mZoeken = (Button) findViewById(R.id.ZoekenBtn);
        mZoeken.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent zoeken = new Intent(Activity_Homescreen.this, Activity_Vacatures.class);
                startActivity(zoeken);
            }
        });
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
            case R.id.nav_filters:
                Toast.makeText(Activity_Homescreen.this, "Filters", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Activity_Homescreen.this, "Settings", Toast.LENGTH_LONG).show();
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
