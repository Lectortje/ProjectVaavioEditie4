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
        View view = inflater.inflate(R.layout.fragment_ac_contact, container, false);
        mButtonVerstuur = view.findViewById(R.id.buttonVerstuur);
        mEditTextNaam = view.findViewById(R.id.editTextNaam);
        mEditTextAchternaam = view.findViewById(R.id.editTextAchternaam);
        mEditTextEmail = view.findViewById(R.id.editTextEmail);
        mEditTextBericht = view.findViewById(R.id.editTextBericht);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        mButtonVerstuur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String naam = mEditTextNaam.getText().toString().trim();
                String achternaam = mEditTextAchternaam.getText().toString().trim();
                String emailadres = mEditTextEmail.getText().toString().trim().toLowerCase();
                final String bericht = mEditTextBericht.getText().toString().trim();

                final HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("Naam", naam);
                dataMap.put("Achternaam", achternaam);
                dataMap.put("Emailadres", emailadres);

                mRef.child("Users").child(UserID).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {

                            mRef.child("Contact").child("Berichten").child(UserID).setValue(bericht).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_VacatureOmschrijving()).addToBackStack("tag").commit();
                                    Toast.makeText(getActivity(), "Uw bericht is succesvol verzonden", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            // If something goes wrong, a toast shows an error.
                            Toast.makeText(getActivity(), "Uw bericht is niet verzonden, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String naam = dataSnapshot.child("Users").child(UserID).child("Naam").getValue(String.class);
                String achternaam = dataSnapshot.child("Users").child(UserID).child("Achternaam").getValue(String.class);
                String emailadres = dataSnapshot.child("Users").child(UserID).child("Email").getValue(String.class);
                String bericht = dataSnapshot.child("Contact").child("Berichten").child(UserID).child("Bericht").getValue(String.class);

                mEditTextNaam.setText(naam);
                mEditTextAchternaam.setText(achternaam);
                mEditTextBericht.setText(bericht);
                mEditTextEmail.setText(emailadres);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
