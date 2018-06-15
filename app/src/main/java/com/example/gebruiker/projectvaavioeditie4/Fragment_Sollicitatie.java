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

    //Declaring all the button, textView, String and Firebase variables.
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
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_ac_sollicitatie, container, false);

        //Setting up the Firebase authentication and connection to read and write data from and to the database
        //and getting the UserID of the current logged in user, putting the userID in the string created above.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        //Assingning the button variables to the button ID's.
        mButtonVerstuurSoll = view.findViewById(R.id.btnVerstuurSoll);
        mEditTextNaamSoll = view.findViewById(R.id.editTextNaamSoll);
        mEditTextAchternaamSoll = view.findViewById(R.id.editTextAchternaamSoll);
        mEditTextEmailSoll = view.findViewById(R.id.editTextEmailSoll);
        mEditTextBerichtSoll = view.findViewById(R.id.editTextBerichtSoll);

        //Setting the OnClick event for the verstuur button.
        mButtonVerstuurSoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First, a string is created, in which the text that is filled in the edit text gets put.
                String naam = mEditTextNaamSoll.getText().toString().trim();
                String achternaam = mEditTextAchternaamSoll.getText().toString().trim();
                String emailadres = mEditTextEmailSoll.getText().toString().trim().toLowerCase();
                final String bericht = mEditTextBerichtSoll.getText().toString().trim();

                // When all the strings are created, they get put in an HashMap. This HashMap is used to send the data to the database.
                // For every string, a child name gets declared.
                HashMap<String, Object> childUpdates = new HashMap<String, Object>();
                childUpdates.put("Naam", naam);
                childUpdates.put("Achternaam", achternaam);
                childUpdates.put("Emailadres", emailadres);

                // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'User' with a
                // Parent equal to there UserID.
                myRef.child("Users").child(UserID).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'Sollicitaties' with a
                            // Parent equal to there UserID.
                            myRef.child("Sollicitaties").child(UserID).setValue(bericht).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    // If the data is put in correctly, the user gets directed to the Account menu and a toast shows telling everything
                                    // Went successfully.
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_VacatureOmschrijving()).addToBackStack("tag").commit();
                                    Toast.makeText(getActivity(), "Uw sollicitatie is succesvol verzonden", Toast.LENGTH_LONG).show();
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

        // Setting up the ValueEventListener, used to extract data from the database. This is to prefill the edit texts with the data from the
        // database so that the user does not have to fill in the whole list again when it wants te change only 1 field for example.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // First there is a string created. This string gets the data form the dataSnapshot. In the case of 'naam'. The string gets the value
                // equal to the Naam child in the child equal to the currently logged in user his UserID. This gets repeated for every string.
                String naam = dataSnapshot.child("Users").child(UserID).child("Naam").getValue(String.class);
                String achternaam = dataSnapshot.child("Users").child(UserID).child("Achternaam").getValue(String.class);
                String emailadres = dataSnapshot.child("Users").child(UserID).child("Email").getValue(String.class);
                String bericht = dataSnapshot.child("Sollicitaties").child(UserID).child("Bericht").getValue(String.class);

                // Here te Edit Texts texts are put equal to the strings values. If the value's are empty, the text field stay empty and you would still see the hint
                // given to the edit text in het xml.
                mEditTextNaamSoll.setText(naam);
                mEditTextAchternaamSoll.setText(achternaam);
                mEditTextBerichtSoll.setText(bericht);
                mEditTextEmailSoll.setText(emailadres);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }
}
