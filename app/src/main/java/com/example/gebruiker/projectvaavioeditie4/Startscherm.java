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

public class Startscherm extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscherm);

        //Omdat actionbar uitgeschakeld is in styles, wordt hier de toolbar aangezien
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Het aanmaken van de drawer, en het toekennen van de layout
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Om de drawer te laten verschijnen
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Wanneer de app geopend wordt moet er niet een lege fragment te zien zijn, bij het opstarten wordt deze dus gevuld met het homescherm.
        de if statement is om te kijken of de persoon de compleet nieuw opstart of weer terug gaat na dat het bijvoorbeeld op de homebutton hebt gedrukt
        Wanneer dat laatste het geval is wordt de code hier onder juist niet uitgevoerd zodat je gewoon weer verder kan gaan waar je bent gebleven.*/
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartschermFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }


    /* de cases die worden uitgevoerd wanneer er op een button wordt gedrukt in het drawer menu. Het zorgt ervoor dat de fragment container
    gevuld wordt met de aan de geklikte knop bij behorende layout  */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StartschermFragment()).commit();
                break;
            case R.id.nav_filters:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FiltersFragment()).commit();
                break;
            case R.id.nav_av:
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InloggenFragment()).commit();
                break;
            case R.id.nav_over_ons:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OverOnsFragment()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactFragment()).commit();
                break;
        }

        //wanneer er op een knop is geklikt wordt de drawer gesloten
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Dit is om de drawer te sluiten wanneer deze op staat met de back button.
    Wanneer er op de terug knop wordt gedrukt checked hij of de drawer open staat, en indien dat het geval is sluit hij deze */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
