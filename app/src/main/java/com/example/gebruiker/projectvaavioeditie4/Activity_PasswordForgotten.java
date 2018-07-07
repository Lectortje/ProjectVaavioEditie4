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
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_PasswordForgotten extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Declaring all the drawer, button, EditText and Firebase variables.
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private Button mHerstellen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordforgotten);

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

        // Setting the title that is showing on the toolbar
        setTitle("Vaavio");

        // Initializing the Auth refrence
        mAuth = FirebaseAuth.getInstance();

        // Setting the refrence to the button and EditText
        mEmail = findViewById(R.id.EditTextPasswordForgoten);
        mHerstellen = findViewById(R.id.HerstellenBtn);

        mHerstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Converting the input in the edit text to a string variable
                String email = mEmail.getText().toString().trim();

                // First, there is a check to make sure the email field isn't empty. If this is the case however, the user gets instructed to fill in a email.
                if (email.isEmpty())
                {
                    Toast.makeText(Activity_PasswordForgotten.this, "Vul een emailadres in", Toast.LENGTH_LONG).show();
                    return;
                }
                // Secondly, the email is being validated. To make sure the filled in text is an email, a check gets executed
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(Activity_PasswordForgotten.this, "Vul een geldig emailadres in", Toast.LENGTH_LONG).show();
                    return;
                }

                // Sending the email to reset the password using the email provided in the edit text.
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            // If email is send successfully, the user gets redirected to the login screen, where it can login again after the password is changed.
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(Activity_PasswordForgotten.this, Activity_Login.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Activity_PasswordForgotten.this, "Email om wachtwoord te herstellen is verstuurd", Toast.LENGTH_LONG).show();
                            }
                            else
                            // If an email is entered that doesn't match any email in the database,  the sending email part won't work. If that is the case, an toast
                            // Will be displayed saying that the email is unknown in the system.
                            {
                                Toast.makeText(Activity_PasswordForgotten.this, "Het ingevoerde emailadres is niet bij ons bekend", Toast.LENGTH_LONG).show();
                            }
                        }
                });
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
                break;
            case R.id.nav_av:
                // Intent that redirects the user to the Vaavio website outside the app
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
                break;
            //Intent that switches the user to his/her profile page from the current activity.
            case R.id.nav_profile:
                Intent navprofile = new Intent(this, Activity_Login.class);
                navprofile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navprofile);
                finish();
                break;
            //Intent that redirects the user to the "about us" page of Vaavio.
            case R.id.nav_over_ons:
                Intent overons = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/over-ons/"));
                startActivity(overons);
                break;
            //Intent that redirects the user to the "contact" page of Vaavio.
            case R.id.nav_contact:
                Intent contact = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/contact/"));
                startActivity(contact);
                break;
            //Intent that shows a toast "Settings" when clicked.
            case R.id.nav_settings:
                Toast.makeText(Activity_PasswordForgotten.this, "Settings", Toast.LENGTH_SHORT).show();
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
