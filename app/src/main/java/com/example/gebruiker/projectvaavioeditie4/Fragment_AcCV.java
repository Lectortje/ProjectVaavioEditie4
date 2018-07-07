package com.example.gebruiker.projectvaavioeditie4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Fragment_AcCV extends Fragment
{
    //Declaring all the button, textView, String and Firebase variables.
    private Button mOpslaanBtn, mUploadCVBtn;
    private EditText mOpleidingen, mErvaring, mTraining, mVaardigheden;
    private String UserID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int PICK_DOCUMENT = 0;

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
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());

        //Assingning the button variables to the button ID's.
        mOpslaanBtn = view.findViewById(R.id.OpslaanBtn);
        mUploadCVBtn = view.findViewById(R.id.UploadCVBtn);
        mOpleidingen = view.findViewById(R.id.EditTextOpleidingen);
        mErvaring = view.findViewById(R.id.EditTextErvaring);
        mTraining = view.findViewById(R.id.EditTextTrainingCursussen);
        mVaardigheden = view.findViewById(R.id.EditTextVaardigheden);

        // Executing the checkFilePersmissions function created down below
        checkFilePermissions();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mFirebaseDatabase.getReference();

        //Setting the OnClick event for the opslaan button.
        mOpslaanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First, a string is created, in which the text that is filled in the edit text gets put.
                String opleidingen = mOpleidingen.getText().toString().trim();
                String ervaring = mErvaring.getText().toString().trim();
                String training = mTraining.getText().toString().trim();
                String vaardigheden = mVaardigheden.getText().toString().trim();

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

        mUploadCVBtn.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*file/*");
            startActivityForResult(intent, PICK_DOCUMENT);
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
                mOpleidingen.setText(opleidingen);
                mErvaring.setText(ervaring);
                mTraining.setText(training);
                mVaardigheden.setText(vaardigheden);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Declaring the request we want to take care of, and checking if the request went successful
        if (requestCode == PICK_DOCUMENT && resultCode == RESULT_OK)
        {
            // Showing the progress dialog while uploading the image
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();

            // Getting the uri
            Uri uri = data.getData();

            // Setting up the path to the location to the image in the database and putting the image in the database
            StorageReference filepath = mStorage.child("CV's").child(UserID);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                // When successful, a toast will show saying the upload was successful and the progress dialog will go.
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(getActivity(), "Upload done.", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    // On android versions higher the lollipop, extra verification is needed to get access to the storage of the phone of the user.
    // So, first the android version of the mobile phone gets checked.
    // If the version is Lollipop or higher, an extra pop-up will show asking the user to grant permission to the storage.
    // If the version is lower then Lollipop, nothing will show.
    private void checkFilePermissions()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0)
            {
                getActivity().requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
        }
    }
}
