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

public class Fragment_Sollicitatie extends Fragment
{
    private Button mButtonVerstuurSoll;
    private EditText mEditTextNaamSoll;
    private EditText mEditTextEmailSoll;
    private EditText mEditTextAchternaamSoll;
    private EditText mEditTextBerichtSoll;
    private String UserID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_sollicitatie, container, false);

        mButtonVerstuurSoll = view.findViewById(R.id.btnVerstuurSoll);
        mEditTextNaamSoll = view.findViewById(R.id.editTextNaamSoll);
        mEditTextAchternaamSoll = view.findViewById(R.id.editTextAchternaamSoll);
        mEditTextEmailSoll = view.findViewById(R.id.editTextEmailSoll);
        mEditTextBerichtSoll = view.findViewById(R.id.editTextBerichtSoll);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        mButtonVerstuurSoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String naam = mEditTextNaamSoll.getText().toString().trim();
                String achternaam = mEditTextAchternaamSoll.getText().toString().trim();
                String emailadres = mEditTextEmailSoll.getText().toString().trim().toLowerCase();
                final String bericht = mEditTextBerichtSoll.getText().toString().trim();

                HashMap<String, Object> childUpdates = new HashMap<String, Object>();
                childUpdates.put("Naam", naam);
                childUpdates.put("Achternaam", achternaam);
                childUpdates.put("Emailadres", emailadres);

                myRef.child("Users").child(UserID).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            myRef.child("Sollicitaties").child(UserID).setValue(bericht).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_VacatureOmschrijving()).addToBackStack("tag").commit();
                                    Toast.makeText(getActivity(), "Uw sollicitatie is succesvol verzonden", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
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
                String bericht = dataSnapshot.child("Sollicitaties").child(UserID).child("Bericht").getValue(String.class);

                mEditTextNaamSoll.setText(naam);
                mEditTextAchternaamSoll.setText(achternaam);
                mEditTextBerichtSoll.setText(bericht);
                mEditTextEmailSoll.setText(emailadres);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
