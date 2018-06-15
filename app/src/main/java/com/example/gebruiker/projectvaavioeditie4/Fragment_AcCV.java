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
    private Button mBtnOpslaan;
    private EditText mEditTextOpleidingen, mEditTextErvaring, mEditTextTraining, mEditTextVaardigheden;
    private String UserID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_cv, container, false);
        mBtnOpslaan = view.findViewById(R.id.OpslaanBtn);
        mEditTextOpleidingen = view.findViewById(R.id.OpleidingenEditText);
        mEditTextErvaring = view.findViewById(R.id.ErvaringEditText);
        mEditTextTraining = view.findViewById(R.id.TrainingCursussenEditText);
        mEditTextVaardigheden = view.findViewById(R.id.VaardighedenEditText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        mBtnOpslaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String opleidingen = mEditTextOpleidingen.getText().toString().trim();
                String ervaring = mEditTextErvaring.getText().toString().trim();
                String training = mEditTextTraining.getText().toString().trim();
                final String vaardigheden = mEditTextVaardigheden.getText().toString().trim();

                final HashMap<String, Object> childUpdates = new HashMap<String, Object>();
                childUpdates.put("Opleidingen", opleidingen);
                childUpdates.put("Ervaring", ervaring);
                childUpdates.put("Training en cursussen", training);
                childUpdates.put("Vaardigheden", vaardigheden);

                mRef.child("CV").child(UserID).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            mRef.child("CV").child(UserID).setValue(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_Account()).addToBackStack("tag").commit();
                                    Toast.makeText(getActivity(), "Uw CV is succesvol opgeslagen", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Uw CV is niet opgeslagen, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String opleidingen = dataSnapshot.child("CV").child("Opleidingen").child(UserID).getValue(String.class);
                String ervaring = dataSnapshot.child("CV").child("Ervaring").child(UserID).getValue(String.class);
                String training = dataSnapshot.child("CV").child("Trainingen en cursussen").child(UserID).getValue(String.class);
                String vaardigheden = dataSnapshot.child("CV").child("Vaardigheden").child(UserID).getValue(String.class);

                mEditTextOpleidingen.setText(opleidingen);
                mEditTextErvaring.setText(ervaring);
                mEditTextTraining.setText(training);
                mEditTextVaardigheden.setText(vaardigheden);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
