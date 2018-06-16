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
import android.widget.EditText;
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

public class Fragment_Contact extends Fragment
{
    //Declaring all the button, textView, String and Firebase variables.
    private Button mButtonVerstuur;
    private EditText mEditTextNaam;
    private EditText mEditTextEmail;
    private EditText mEditTextAchternaam;
    private EditText mEditTextBericht;
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
        View view = inflater.inflate(R.layout.fragment_ac_contact, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        //Assingning the button variables to the button ID's.
        mButtonVerstuur = view.findViewById(R.id.buttonVerstuur);
        mEditTextNaam = view.findViewById(R.id.editTextNaam);
        mEditTextAchternaam = view.findViewById(R.id.editTextAchternaam);
        mEditTextEmail = view.findViewById(R.id.editTextEmail);
        mEditTextBericht = view.findViewById(R.id.editTextBericht);


        //Setting the OnClick event for the verstuur button.
        mButtonVerstuur.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // First, a string is created, in which the text that is filled in the edit text gets put.
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null)
                {
                    String naam = mEditTextNaam.getText().toString().trim();
                    String achternaam = mEditTextAchternaam.getText().toString().trim();
                    String emailadres = mEditTextEmail.getText().toString().trim().toLowerCase();
                    final String bericht = mEditTextBericht.getText().toString().trim();

                    // When all the strings are created, they get put in an HashMap. This HashMap is used to send the data to the database.
                    // For every string, a child name gets declared.
                    HashMap<String, Object> childUpdates = new HashMap<String, Object>();
                    childUpdates.put("Naam", naam);
                    childUpdates.put("Achternaam", achternaam);
                    childUpdates.put("Emailadres", emailadres);

                    // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'User' with a
                    // Parent equal to there UserID.

                    mAuth = FirebaseAuth.getInstance();
                    UserID = user.getUid();
                    mRef.child("Users").child(UserID).child("Persoonlijke informatie").updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'Contact' with a
                                // Parent equal to there UserID.
                                mRef.child("Contact").child("Berichten").child(UserID).setValue(bericht).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        // If the data is put in correctly, the user gets directed to the Account menu and a toast shows telling everything
                                        // Went successfully.
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, new Fragment_VacatureOmschrijving()).commit();
                                        Toast.makeText(getActivity(), "Uw bericht is succesvol verzonden", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                // If something goes wrong, a toast shows an error.
                                Toast.makeText(getActivity(), "Uw bericht is niet verzonden, probeer het opnieuw", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "U moet ingelogd zijn om een bericht te versturen", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Setting up the ValueEventListener, used to extract data from the database. This is to prefill the edit texts with the data from the
        // database so that the user does not have to fill in the whole list again when it wants te change only 1 field for example.
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            mRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    UserID = user.getUid();
                    // First there is a string created. This string gets the data form the dataSnapshot. In the case of 'naam'. The string gets the value
                    // equal to the Naam child in the child equal to the currently logged in user his UserID. This gets repeated for every string.
                    String naam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Naam").getValue(String.class);
                    String achternaam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Achternaam").getValue(String.class);
                    String emailadres = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Email").getValue(String.class);
                    String bericht = dataSnapshot.child("Contact").child("Berichten").child(UserID).child("Bericht").getValue(String.class);

                    // Here te Edit Texts texts are put equal to the strings values. If the value's are empty, the text field stay empty and you would still see the hint
                    // given to the edit text in het xml.
                    mEditTextNaam.setText(naam);
                    mEditTextAchternaam.setText(achternaam);
                    mEditTextBericht.setText(bericht);
                    mEditTextEmail.setText(emailadres);
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }
}
