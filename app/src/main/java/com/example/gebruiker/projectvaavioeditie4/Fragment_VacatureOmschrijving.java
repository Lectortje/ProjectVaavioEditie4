package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Fragment_VacatureOmschrijving extends Fragment
{
    //Declaring all the variables.
    private Button mSollicitatie, mContact;
    private TextView mFunctie, mLocatie, mDienstverband, mOpleiding, mSalaris, mOmschrijving;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String UserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_vacatureomschrijving, container, false);

        // Setting up the database reference
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        // Declaring the variables and setting the equal to their corresponding objects
        mFunctie = view.findViewById(R.id.TextViewFunctie);
        mLocatie = view.findViewById(R.id.TextViewLocatie);
        mDienstverband = view.findViewById(R.id.TextViewDienstverband);
        mOpleiding = view.findViewById(R.id.TextViewOpleiding);
        mSalaris = view.findViewById(R.id.TextViewSalaris);
        mOmschrijving = view.findViewById(R.id.TextViewOmschrijving);

        //Assingning the button variables to the button ID.
        mContact = view.findViewById(R.id.ContactBtn);
        mSollicitatie = view.findViewById(R.id.SollicitatieBtn);

        getActivity().setTitle("Vacature omschrijving");

        // Setting up the addValueEventListener to import the data from the database
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // First, there is a check to see if the data send with the Intent from Activity_Vacatures is present. If not present, the
                // app will crash when trying to do something with it, because it's empty. A toast will show saying something went wrong
                // When no key is present.
                if (getActivity().getIntent().hasExtra("Key"))
                {
                    // If a key is present, the Intent extra gets put into a variable key, which can be used in or database reference
                    String key = getActivity().getIntent().getExtras().getString("Key");

                    // With the key, the data corresponding with that key can be loaded in into the placeholder, this is done by getting
                    // a reference to the needed data, and put that data into a string. This string is then loaded into the TextView
                    String functie = dataSnapshot.child("Vacatures").child(key).child("Functie").getValue(String.class);
                    String locatie = dataSnapshot.child("Vacatures").child(key).child("Locatie").getValue(String.class);
                    String dienstverband = dataSnapshot.child("Vacatures").child(key).child("Dienstverband").getValue(String.class);
                    String opleiding = dataSnapshot.child("Vacatures").child(key).child("Opleidingsniveau").getValue(String.class);
                    String salaris = dataSnapshot.child("Vacatures").child(key).child("Salarisschaal").getValue(String.class);
                    String omschrijving = dataSnapshot.child("Vacatures").child(key).child("Omschrijving volledig").getValue(String.class);

                    //The variables are 'set' into text with the corresponding values. This means that the values will be converted into
                    // text and then displayed on screen as text.
                    mFunctie.setText(functie);
                    mLocatie.setText(locatie);
                    mDienstverband.setText(dienstverband);
                    mOpleiding.setText(opleiding);
                    mSalaris.setText(salaris);
                    mOmschrijving.setText(omschrijving);
                }
                else
                {
                    //In case the data can not be send to the database, the user get's an error message saying that he/she should try again.
                    Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        // The button lets the user navigate to the Sollicitatie screen to send out a sollicitation to the employer.
        mSollicitatie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //This is the onClick handling for the button btnSoll.
            public void onClick(View v)
            {
                //This opens the fragment Fragment_Sollicitatie if the button btnSoll is clicked.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Sollicitatie()).addToBackStack("tag").commit();
            }
        });
        // This button starts an intent to the profile activity, in which the create mail fragment will be opened. A few things get send
        // with the intent aswell, these are the email and omschrijving. They first get gotten out of the database with a ValueEventListener.
        mContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //This is the onClick handling for the button btnContact.
            public void onClick(View v)
            {
                myRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String key = getActivity().getIntent().getExtras().getString("Key");

                        String email = dataSnapshot.child("Vacatures").child(key).child("Plaatser").getValue(String.class);
                        String omschrijving = dataSnapshot.child("Vacatures").child(key).child("Omschrijving").getValue(String.class);

                        Intent intent = new Intent(getActivity(), Activity_Profile.class);
                        intent.putExtra("VacOms", "VacOms");
                        intent.putExtra("Email", email);
                        intent.putExtra("Omschrijving", omschrijving);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });




            }
        });

        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }

    // Telling the app when the activity is created that the app has an options menu
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Creating the options menu by inflating the layout to it
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.vacature_omschijving_menu, menu);
        return;
    }

    // Telling the app what to do when the button in the options menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.FavoriteBtn:

                // checking if a user is currently logged in
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null)
                {
                    // An addValueEventListener is created to get the data from the database
                    myRef.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            // The Key that has been send from the vacatures activity gets converted into a string that we can use
                            String key = getActivity().getIntent().getExtras().getString("Key");

                            // The current user get put into a variable
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserID = user.getUid();

                            // Getting the data needed from the dataShot and putting it into string variables.
                            String dienstverband = dataSnapshot.child("Vacatures").child(key).child("Dienstverband").getValue(String.class);
                            String functie = dataSnapshot.child("Vacatures").child(key).child("Functie").getValue(String.class);
                            String functie_locatie = dataSnapshot.child("Vacatures").child(key).child("Functie_Locatie").getValue(String.class);
                            String locatie = dataSnapshot.child("Vacatures").child(key).child("Locatie").getValue(String.class);
                            String omschrijving = dataSnapshot.child("Vacatures").child(key).child("Omschrijving").getValue(String.class);
                            String omschrijving_volledig = dataSnapshot.child("Vacatures").child(key).child("Omschrijving volledig").getValue(String.class);
                            String opleidingsniveau = dataSnapshot.child("Vacatures").child(key).child("Opleidingsniveau").getValue(String.class);
                            String salarisschaal = dataSnapshot.child("Vacatures").child(key).child("Salarisschaal").getValue(String.class);

                            // Putting the strings just created into a HashMap
                            HashMap<String, Object> dataMap = new HashMap<String, Object>();
                            dataMap.put("Functie", functie);
                            dataMap.put("Key", key);
                            dataMap.put("Locatie", locatie);
                            dataMap.put("Omschrijving", omschrijving);
                            dataMap.put("Omschrijving volledig", omschrijving_volledig);
                            dataMap.put("Opleidingsniveau", opleidingsniveau);
                            dataMap.put("Dienstverband", dienstverband);
                            dataMap.put("Salarisschaal", salarisschaal);
                            dataMap.put("Functie_Locatie", functie_locatie);

                            // Sending the HashMap to a new reference in the database where the favorites of a user are stored.
                            myRef.child("Favorites").child(UserID).child(key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(getActivity(), "Vacature aan favorieten toegevoegd", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "Je moet ingelogd zijn om vacatures aan je favorieten toe te voegen", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return onOptionsItemSelected(item);
    }
}
