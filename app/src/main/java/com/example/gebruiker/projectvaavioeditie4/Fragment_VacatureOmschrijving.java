package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class Fragment_VacatureOmschrijving extends Fragment
{
    //Declaring all the variables.
    private Button mSollicitatie, mContact;
    private TextView mFunctie, mLocatie, mDienstverband, mOpleiding, mSalaris, mOmschrijving;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRef2;
    private ImageButton mImageButtonFav;

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
        mImageButtonFav = view.findViewById(R.id.imageButtonFav);

        //Assingning the button variables to the button ID.
        mContact = view.findViewById(R.id.ContactBtn);
        mSollicitatie = view.findViewById(R.id.SollicitatieBtn);

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

        //The button lets the user navigate to the Sollicitatie screen to send out a sollicitation to the employer.
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
        //This button lets the user navigate to the Contact screen in order to send out a contactform to inquire more information
        // about the job offer.
        mContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //This is the onClick handling for the button btnContact.
            public void onClick(View v)
            {
                //This opens the fragment Fragment_Contact if the button btnContact is clicked.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Contact()).addToBackStack("tag").commit();
            }

        });

        mImageButtonFav.setOnClickListener(new View.OnClickListener() {
            //This button saves the job offer in the Firebase database under the path: "Vacatures" -> "Favorieten".
            //This can be done by the user in order to save the job offer as a favorite to later be able to quickly navigate to the job offer
            // from the 'Favorieten' screen.
            @Override
            //This is the onClick handling for the button imageButtonFav.
            public void onClick(View v) {
                // First the connection to the database gets initialized.
                mDatabase = FirebaseDatabase.getInstance();
                myRef2 = mDatabase.getReference();

                // To generate a key for the vacature, an empty vacature (it gets filled later) gets pushed (pushing generates a key in Firebase) to
                // the database, after which the key just generated gets instantly gotten and put into a string variable, so that we can use it.
                String key = myRef2.child("Users").child("Favoriete Vacatures").push().getKey();

                // The strings just created get put into a HashMap
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("Key", key);

                // When everything above is created, it can be put in to the database. In this reference as you can see is the key created above used
                // This way the data gets put into that random generated key.
                myRef2.child("Users").child("Favoriete Vacatures").child(key).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Vacature is opgeslagen in favorieten.", Toast.LENGTH_SHORT).show();
                        }
                        // If the data does not get put in the database successful, an toast will show saying that the user needs to be logged in to place a vacature
                        else {
                            Toast.makeText(getActivity(), "Je moet ingelogd zijn om een vacatures te kunnen opslaan als favoriet.", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
                });

        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }
}
