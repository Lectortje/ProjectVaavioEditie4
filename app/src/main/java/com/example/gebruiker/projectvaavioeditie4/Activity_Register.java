package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Register extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //Declaring all the button, textView, String and Firebase variables.
    private DrawerLayout drawer;
    private EditText mEmail, mWachtwoord;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextView mAV;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        mEmail = findViewById(R.id.EditTextEmail);
        mWachtwoord = findViewById(R.id.EditTextWachtwoord);
        mRegister = findViewById(R.id.RegisterBtn);
        mAV = findViewById(R.id.TextViewAlgemeneVoorwaarden);

        // Initializing the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Handling what to do when the register button is clicked
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // First the text in the Edit Text Fields get converted to strings and put in a variable
                String email = mEmail.getText().toString().trim();
                String wachtwoord = mWachtwoord.getText().toString().trim();

                // First, there is a check to make sure the email field isn't empty. If this is the case however, the user gets instructed to fill in a email.
                if (email.isEmpty())
                {
                    Toast.makeText(Activity_Register.this, "Vul een emailadres in", Toast.LENGTH_LONG).show();
                    return;
                }
                // Secondly, the email is being validated. To make sure the filled in text is an email, a check gets executed
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(Activity_Register.this, "Vul een geldig emailadres in", Toast.LENGTH_LONG).show();
                    return;
                }
                // Thirdly, there is a check te make sure the password field isn't empty. If this is the case however, the user gets instructed to fill in a password
                if (wachtwoord.isEmpty())
                {
                    Toast.makeText(Activity_Register.this, "Vul een wachtwoord in", Toast.LENGTH_LONG).show();
                    return;
                }
                // Lastly, there is a check to make sure the password is long enough. Firebase requires passwords longer then 6 tokens.
                if (wachtwoord.length() < 6)
                {
                    Toast.makeText(Activity_Register.this, "Minimale aantal tekens is 6", Toast.LENGTH_LONG).show();
                    return;
                }

                // When all the checks above are correctly checked. The firebase register function gets executed. If the sign up is successful, the user gets a toast
                // message and it gets redirect to the profile page.
                mAuth.createUserWithEmailAndPassword(email, wachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sending the email verification using the method provided by Firebase. If task is successful, a toast get displayed.
                            mUser = mAuth.getCurrentUser();

                            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Activity_Register.this, "U bent geregistreerd, verifieer je email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        // When the registration fails, the failure gets 'test'. The main reason it will fail, is to a email already having an account. This gets
                        // Checked with the first catch (existEmail). When this is the case, an toast will show saying the email adres is already in use.
                        // If this ain't the case however, the failure gets logged to the system.
                        else
                        {
                            try
                            {
                               throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Toast.makeText(Activity_Register.this, "Emailadres is al in gebruik", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
            }
        });

        // Handling the event when clicked on the Algemene voorwaarden text. It will redirect the user to the vaavio site.
        mAV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent voorwaarden = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(voorwaarden);
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
            case R.id.nav_filters:
                Toast.makeText(Activity_Register.this, "Filters", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Activity_Register.this, "Settings", Toast.LENGTH_LONG).show();
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
