package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_VacatureOmschrijving extends Fragment
{
    //Declaring all the button variables.
    private Button mSollicitatie, mContact;
    private TextView mFunctie, mLocatie, mDienstverband, mOpleiding, mSalaris, mOmschrijving;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_vacatureomschrijving, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        mFunctie = view.findViewById(R.id.TextViewFunctie);
        mLocatie = view.findViewById(R.id.TextViewLocatie);
        mDienstverband = view.findViewById(R.id.TextViewDienstverband);
        mOpleiding = view.findViewById(R.id.TextViewOpleiding);
        mSalaris = view.findViewById(R.id.TextViewSalaris);
        mOmschrijving = view.findViewById(R.id.TextViewOmschrijving);

        //Assingning the button variables to the button ID.
        mContact = view.findViewById(R.id.ContactBtn);
        mSollicitatie = view.findViewById(R.id.SollicitatieBtn);

        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (getActivity().getIntent().hasExtra("Key"))
                {
                    String key = getActivity().getIntent().getExtras().getString("Key");

                    String functie = dataSnapshot.child("Vacatures").child(key).child("Functie").getValue(String.class);
                    String locatie = dataSnapshot.child("Vacatures").child(key).child("Locatie").getValue(String.class);
                    String dienstverband = dataSnapshot.child("Vacatures").child(key).child("Dienstverband").getValue(String.class);
                    String opleiding = dataSnapshot.child("Vacatures").child(key).child("Opleidingsniveau").getValue(String.class);
                    String salaris = dataSnapshot.child("Vacatures").child(key).child("Salarisschaal").getValue(String.class);
                    String omschrijving = dataSnapshot.child("Vacatures").child(key).child("Omschrijving volledig").getValue(String.class);

                    mFunctie.setText(functie);
                    mLocatie.setText(locatie);
                    mDienstverband.setText(dienstverband);
                    mOpleiding.setText(opleiding);
                    mSalaris.setText(salaris);
                    mOmschrijving.setText(omschrijving);
                }
                else
                {
                    Toast.makeText(getActivity(), "Geen key", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

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
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }
}
