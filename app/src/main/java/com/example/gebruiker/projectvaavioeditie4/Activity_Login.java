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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //Declaring all the button, textView, String and Firebase variables.
    private DrawerLayout drawer;
    private EditText mEmailadres;
    private EditText mWachtwoord;
    private Button mInlog;
    private TextView mRegisteren;
    private TextView mResetPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        // Setting up the refrences to the actual buttons/edittexts/textviews in the app
        mEmailadres = (EditText) findViewById(R.id.EditTextEmailadres);
        mWachtwoord = (EditText) findViewById(R.id.EditTextWachtwoord);
        mInlog = (Button) findViewById(R.id.InlogBtn);
        mRegisteren = (TextView) findViewById(R.id.TextViewRegistreren);
        mResetPassword = (TextView) findViewById(R.id.TextViewWachtwoordVergeten);

        // Initializing the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Creating the AuthStateListener, which is used to check if the user is already logged in or not.
        // If it gets a user from Firebase, instead of going to the InlogActivity, it wil go to the profile activity.
        // If however it doesn't get a current user, it will just go to the InlogActivity.
        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    // Intent to change to the profile activity.
                    Intent profile = new Intent(Activity_Login.this, Activity_Profile.class);
                    startActivity(profile);
                }
            }
        };

        // Handling the event when the user clicks on the Inlog button.
        mInlog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // First the input given by the users in the Edit Text Fields is converted into String and then placed into a string variable
                String email = mEmailadres.getText().toString();
                String wachtwoord = mWachtwoord.getText().toString();

                // Checking if the strings are empty or not. If empty the app will toast the user to fill in all the Edit Text Fields
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(Activity_Login.this, "Vul emailadres in", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(wachtwoord))
                {
                    Toast.makeText(Activity_Login.this, "Vul wachtwoord in", Toast.LENGTH_LONG).show();
                    return;
                }
                // If the fields are not empty, they will be placed in the login method.
                else
                    {
                        // Executing the login method with the email and wachtwoord. Also adding an OnCompleteListener to check if
                        // the login went alright
                        mAuth.signInWithEmailAndPassword(email, wachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                // If the task (the login proces) did not go well (either a wrong passwoord or a wrong email),
                                // The app will show to user to check if their password or email is correct.
                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(Activity_Login.this, "Wachtwoord of gebruikersnaam verkeerd", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
            }
        });

        // Setting the onClickListener for the Register Text. When the user clicks the text he will be redirected to the register activity.
        mRegisteren.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registeren = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(registeren);
            }
        });

        // Setting the onCLickListener for the password reset text. When the user clicks the text he will be redirected to the reset activity
        mResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resetpassword = new Intent(Activity_Login.this, Activity_PasswordForgotten.class);
                startActivity(resetpassword);
            }
        });
    }

    // When the login activity is loaded, you first want to check if the user is logged in or not already. Therefore, onStart, you
    // Execute the AuthStateListener. If the user is logged in, you will go to the profile activity, as intented in the method above
    // If not logged in, you will go to the login activity to login.
    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
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
                Toast.makeText(Activity_Login.this, "Filters", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Activity_Login.this, "Settings", Toast.LENGTH_LONG).show();
                break;
        }
        // After an item is clicked in the menu, the drawer will close itself so you can see the activity/fragment
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Handling the event that happens when you click the (physical) back button on the users phone.
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


