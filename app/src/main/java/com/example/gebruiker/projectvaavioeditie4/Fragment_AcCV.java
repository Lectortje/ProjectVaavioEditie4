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

public class Fragment_AcCV extends Fragment
{
    //Declaring all the button, textView, String and Firebase variables.
    private Button mBtnOpslaan, mUploadCVBtn;
    private EditText mEditTextOpleidingen, mEditTextErvaring, mEditTextTraining, mEditTextVaardigheden;
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
        View view = inflater.inflate(R.layout.fragment_ac_cv, container, false);

        //Setting up the Firebase authentication and connection to read and write data from and to the database
        //and getting the UserID of the current logged in user, putting the userID in the string created above.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        //Assingning the button variables to the button ID's.
        mBtnOpslaan = view.findViewById(R.id.OpslaanBtn);
        mUploadCVBtn = view.findViewById(R.id.UploadCVBtn);
        mEditTextOpleidingen = view.findViewById(R.id.OpleidingenEditText);
        mEditTextErvaring = view.findViewById(R.id.ErvaringEditText);
        mEditTextTraining = view.findViewById(R.id.TrainingCursussenEditText);
        mEditTextVaardigheden = view.findViewById(R.id.VaardighedenEditText);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        //Setting the OnClick event for the opslaan button.
        mBtnOpslaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First, a string is created, in which the text that is filled in the edit text gets put.
                String opleidingen = mEditTextOpleidingen.getText().toString().trim();
                String ervaring = mEditTextErvaring.getText().toString().trim();
                String training = mEditTextTraining.getText().toString().trim();
                String vaardigheden = mEditTextVaardigheden.getText().toString().trim();

                // When all the strings are created, they get put in an HashMap. This HashMap is used to send the data to the database.
                // For every string, a child name gets declared.
                final HashMap<String, String> datamap = new HashMap<String, String>();
                datamap.put("Opleidingen", opleidingen);
                datamap.put("Ervaring", ervaring);
                datamap.put("Training en cursussen", training);
                datamap.put("Vaardigheden", vaardigheden);

                // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'User' with a
                // Parent equal to there UserID.
                mRef.child("Users").child(UserID).child("CV").setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // If the data is put in correctly, the user gets directed to the Account menu and a toast shows telling everything
                            // Went successfully.
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new Fragment_Account()).addToBackStack("tag").commit();
                            Toast.makeText(getActivity(), "Uw CV is succesvol opgeslagen", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // If something goes wrong, a toast shows an error.
                            Toast.makeText(getActivity(), "Uw CV is niet opgeslagen, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Setting up the ValueEventListener, used to extract data from the database. This is to prefill the edit texts with the data from the
        // database so that the user does not have to fill in the whole list again when it wants te change only 1 field for example.
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // First there is a string created. This string gets the data form the dataSnapshot. In the case of 'opleidingen'. The string gets the value
                // equal to the Opleidingen child in the child equal to the currently logged in user his UserID. This gets repeated for every string.
                String opleidingen = dataSnapshot.child("Users").child(UserID).child("CV").child("Opleidingen").getValue(String.class);
                String ervaring = dataSnapshot.child("Users").child(UserID).child("CV").child("Ervaring").getValue(String.class);
                String training = dataSnapshot.child("Users").child(UserID).child("CV").child("Training en cursussen").getValue(String.class);
                String vaardigheden = dataSnapshot.child("Users").child(UserID).child("CV").child("Vaardigheden").getValue(String.class);

                // Here te Edit Texts texts are put equal to the strings values. If the value's are empty, the text field stay empty and you would still see the hint
                // given to the edit text in het xml.
                mEditTextOpleidingen.setText(opleidingen);
                mEditTextErvaring.setText(ervaring);
                mEditTextTraining.setText(training);
                mEditTextVaardigheden.setText(vaardigheden);

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
