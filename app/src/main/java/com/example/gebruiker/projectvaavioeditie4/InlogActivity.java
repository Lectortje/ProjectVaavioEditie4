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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InlogActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private DrawerLayout drawer;
    private EditText mEmailadres;
    private EditText mWachtwoord;
    private Button mInlog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlog);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mEmailadres = (EditText) findViewById(R.id.EditTextEmailadres);
        mWachtwoord = (EditText) findViewById(R.id.EditTextWachtwoord);
        mInlog = (Button) findViewById(R.id.InlogBtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                if (firebaseAuth.getCurrentUser() != null)
                {
                    Intent profile = new Intent(InlogActivity.this, ProfielWerknemerActivity.class);
                    startActivity(profile);
                }
            }
        };

        mInlog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String email = mEmailadres.getText().toString();
                String wachtwoord = mWachtwoord.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(wachtwoord))
                {
                    Toast.makeText(InlogActivity.this, "Vul gebruikersnaam en wachtwoord in", Toast.LENGTH_LONG).show();
                }
                else
                    {

                        mAuth.signInWithEmailAndPassword(email, wachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {

                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(InlogActivity.this, "Wachtwoord of gebruikersnaam verkeerd", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                Intent navhome = new Intent(this, Startscherm.class);
                navhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navhome);
                finish();
                break;
            case R.id.nav_filters:
                Toast.makeText(InlogActivity.this, "Filters", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_av:
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
                break;
            case R.id.nav_profile:
                Intent navprofile = new Intent(this, InlogActivity.class);
                navprofile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navprofile);
                finish();
                break;
            case R.id.nav_over_ons:
                Toast.makeText(InlogActivity.this, "Over ons", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_contact:
                Toast.makeText(InlogActivity.this, "Contact", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(InlogActivity.this, "Settings", Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
                super.onBackPressed();
            }
    }
}


