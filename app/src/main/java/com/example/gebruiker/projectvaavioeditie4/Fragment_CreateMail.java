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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Fragment_CreateMail extends Fragment {

    //Declaring all the button, textView, String and Firebase variables.
    private EditText mMail, mAan;
    private Button mOpslaan;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_create_mail, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        mMail = view.findViewById(R.id.EditTextMail);
        mAan = view.findViewById (R.id.EditTextAan);
        mOpslaan = view.findViewById(R.id.OpslaanBtn);

        mAuth = FirebaseAuth.getInstance();

        mOpslaan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mUser = mAuth.getCurrentUser();

                // First, a string is created, in which the text that is filled in the edit text gets put.
                String ontvanger = mAan.getText().toString().trim();
                String bericht = mMail.getText().toString().trim();
                String key = myRef.child("Inbox").push().getKey();
                String verzender = mUser.getEmail();

                // When all the strings are created, they get put in an HashMap. This HashMap is used to send the data to the database.
                // For every string, a child name gets declared.
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("Ontvanger", ontvanger);
                dataMap.put("Bericht", bericht);
                dataMap.put("Key", key);
                dataMap.put("Verzender", verzender);

                // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'User' with a
                // Parent equal to there UserID.
                myRef.child("Inbox").child(key).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcInbox()).addToBackStack("tag").commit();
                        }
                        else
                        {
                            // If something goes wrong, the toast below shows an error.
                            Toast.makeText(getActivity(), "Uw e-mail is niet verzonden, probeer het opnieuw", Toast.LENGTH_LONG).show();
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