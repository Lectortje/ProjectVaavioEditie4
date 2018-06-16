package com.example.gebruiker.projectvaavioeditie4;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_AcDisplayProfile extends Fragment
{
    //Declaring all the button, textView, String and Firebase variables.
    private TextView mTextViewNationaliteit, mTextViewNaamWerkn, mTextViewAdres, mTextViewPostcode, mTextViewPlaats, mTextViewEmailadres;
    private TextView mTextViewOpleidingen, mTextViewErvaring, mTextViewTrainingen, mTextViewVaardigheden, mTextViewTelefoonNr, mTextViewGeslacht;
    private String UserID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_ac_display_profile, container, false);

        //Setting up the Firebase authentication and connection to read and write data from and to the database
        //and getting the UserID of the current logged in user, putting the userID in the string created above.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        //Assingning the button variables to the button ID's.
        mTextViewNationaliteit = view.findViewById(R.id.textViewNationaliteitDis);
        mTextViewAdres = view.findViewById(R.id.textViewAdresDis);
        mTextViewErvaring = view.findViewById(R.id.textViewErvaringDis);
        mTextViewNaamWerkn = view.findViewById(R.id.textViewNaamWerknDis);
        mTextViewOpleidingen = view.findViewById(R.id.textViewOpleidingenDis);
        mTextViewPlaats = view.findViewById(R.id.textViewPlaatsDis);
        mTextViewPostcode = view.findViewById(R.id.textViewPostcodeDis);
        mTextViewTrainingen = view.findViewById(R.id.textViewTrainingenDis);
        mTextViewVaardigheden = view.findViewById(R.id.textViewVaardighedenDis);
        mTextViewTelefoonNr = view.findViewById(R.id.textViewTelefoonNrDis);
        mTextViewEmailadres = view.findViewById(R.id.textViewEmailadresDis);
        mTextViewGeslacht = view.findViewById(R.id.textViewGeslachtDis);


        // Setting up the ValueEventListener, used to extract data from the database. This is to prefill the edit texts with the data from the
        // database so that the user does not have to fill in the whole list again when it wants te change only 1 field for example.
        mRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // First there is a string created. This string gets the data form the dataSnapshot. In the case of 'telefoonnummer'. The string gets the value
                // equal to the Telefoonnummer child in the child equal to the currently logged in user his UserID. This gets repeated for every string.
                String telefoonnummer = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Telefoonnummer").getValue(String.class);
                String emailadres = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Email").getValue(String.class);
                String geslacht = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Geslacht").getValue(String.class);
                String naam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Naam").getValue(String.class);
                String achternaam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Achternaam").getValue(String.class);
                String adres = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Adres").getValue(String.class);
                String nationaliteit = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Nationaliteit").getValue(String.class);
                String plaats = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Woonplaats").getValue(String.class);
                String postcode = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Postcode").getValue(String.class);
                String huisnummer = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Huisnummer").getValue(String.class);
                String toevoeging = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Toevoeging").getValue(String.class);
                String ervaring = dataSnapshot.child("Users").child(UserID).child("CV").child("Ervaring").getValue(String.class);
                String opleidingen = dataSnapshot.child("Users").child(UserID).child("CV").child("Opleidingen").getValue(String.class);
                String trainingen = dataSnapshot.child("Users").child(UserID).child("CV").child("Training en cursussen").getValue(String.class);
                String vaardigheden = dataSnapshot.child("Users").child(UserID).child("CV").child("Vaardigheden").getValue(String.class);

                // Here te Edit Texts texts are put equal to the strings values. If the value's are empty, the text field stay empty and you would still see the hint
                // given to the edit text in het xml.
                mTextViewAdres.setText(adres + " " + huisnummer + toevoeging);
                mTextViewErvaring.setText(ervaring);
                mTextViewNaamWerkn.setText(naam + " " + achternaam);
                mTextViewNationaliteit.setText(nationaliteit);
                mTextViewOpleidingen.setText(opleidingen);
                mTextViewPlaats.setText(plaats);
                mTextViewPostcode.setText(postcode);
                mTextViewTrainingen.setText(trainingen);
                mTextViewVaardigheden.setText(vaardigheden);
                mTextViewTelefoonNr.setText(telefoonnummer);
                mTextViewEmailadres.setText(emailadres);
                mTextViewGeslacht.setText(geslacht);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }

}
