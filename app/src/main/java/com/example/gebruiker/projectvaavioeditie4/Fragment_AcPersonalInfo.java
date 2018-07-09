package com.example.gebruiker.projectvaavioeditie4;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Fragment_AcPersonalInfo extends Fragment
{
    // Declaring the variables
    private EditText mNaam, mAchternaam, mEmail, mTelefoonnummer, mAdres, mHuisnummer, mToevoeging, mWoonplaats, mPostcode;
    private Spinner mGeslacht, mNationaliteit;
    private TextView mGeboortedatum;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mOpslaan, mUpload;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private String UserID;
    private ImageView mProfielFoto;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_personal_info, container, false);

        // Setting up the firebase connection, and getting the UserID of the current logged in user, putting the userID in the string created above.
        // Also setting up the storage connection for the upload button. And creating the progressbar shown while uploading
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());

        // Setting the variables equal to their corresponding objects
        mProfielFoto = view.findViewById(R.id.ImageViewProfilePicture);
        mNaam = view.findViewById(R.id.EditTextNaam);
        mAchternaam = view.findViewById(R.id.EditTextAchternaam);
        mEmail = view.findViewById(R.id.EditTextEmailadres);
        mTelefoonnummer = view.findViewById(R.id.EditTextTelefoon);
        mAdres = view.findViewById(R.id.EditTextAdres);
        mHuisnummer = view.findViewById(R.id.EditTextHuisnummer);
        mToevoeging = view.findViewById(R.id.EditTextToevoeging);
        mWoonplaats = view.findViewById(R.id.EditTextWoonplaats);
        mPostcode = view.findViewById(R.id.EditTextPostcode);
        mGeboortedatum = view.findViewById(R.id.TextViewGeboortedatum);
        mGeslacht = view.findViewById(R.id.SpinnerGeslacht);
        mNationaliteit = view.findViewById(R.id.SpinnerNationaliteit);

        // Setting the Array adapters for the spinners. They get declared the spinner item layout, which can be found in the res map
        final ArrayAdapter<CharSequence> adapterGeslacht = ArrayAdapter.createFromResource(getActivity(), R.array.geslacht, R.layout.spinner_item);
        mGeslacht.setAdapter(adapterGeslacht);

        final ArrayAdapter<CharSequence> adapterNationaliteit = ArrayAdapter.createFromResource(getActivity(), R.array.landen, R.layout.spinner_item);
        mNationaliteit.setAdapter(adapterNationaliteit);

        // Executing the checkFilePersmissions function created down below
        checkFilePermissions();

        getActivity().setTitle("Persoonlijke informatie");

        // Executing the setProfileImage function created down below
        setProfileImage();

        // Handling the event that needs to happen when the upload button is clicked.
        // When the upload button is clicked, a new intent will start opening the gallery on the users phone. In the gallery, the user can pick an image
        // that it wants to upload as the profile picture.
        mUpload = (Button) view.findViewById(R.id.UploadPFBtn);
        mUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        // Setting the OnClickListener for the geboortedatum TextView.
        mGeboortedatum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // First the current day, month and year get out into int variables
                Calendar cal = Calendar.getInstance();
                int jaar = cal.get(Calendar.YEAR);
                int maand = cal.get(Calendar.MONTH);
                int dag = cal.get(Calendar.DAY_OF_MONTH);

                // Second, the DatePickerDialog gets set up. The context is put in, the thema, the DateSetListener (created down below) and the jaar, maand and dag
                // variables declared above to show the current date. Also the background color gets made transparent and lastly the dialog is shown.
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, jaar, maand, dag);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Setting up date DateSetListener
        mDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                // To show the month correctly (January is 0, instead of 1) the month get incremented by 1.
                month = month + 1;

                // The day, month and year selected by the user get put in a string variable called datum together. After which the Text of the geboortedatum TextView
                // gets set equal to the datum string created.
                String datum = dayOfMonth + "/" + month + "/" + year;
                mGeboortedatum.setText(datum);
            }
        };

        // Setting up the ValueEventListener, used to extract data from the database. This is to prefill the edit texts with the data from the
        // database so that the user does not have to fill in the whole list again when it wants te change only 1 field for example.
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                // First there is a string created. This string gets the data form the dataSnapshot. In the case of 'naam'. The string gets the value
                // equal to the Naam child in the child equal to the currently logged in user his UserID. This gets repeated for every string.
                String naam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Naam").getValue(String.class);
                String achternaam = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Achternaam").getValue(String.class);
                String email = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Email").getValue(String.class);
                String telefoonnummer = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Telefoonnummer").getValue(String.class);
                String adres = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Adres").getValue(String.class);
                String huisnummer = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Huisnummer").getValue(String.class);
                String toevoeging = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Toevoeging").getValue(String.class);
                String woonplaats = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Woonplaats").getValue(String.class);
                String postcode = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Postcode").getValue(String.class);
                String geboortedatum = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Geboortedatum").getValue(String.class);
                String geslacht = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Geslacht").getValue(String.class);
                String nationaliteit = dataSnapshot.child("Users").child(UserID).child("Persoonlijke informatie").child("Nationaliteit").getValue(String.class);

                // Here te Edit Texts texts are put equal to the strings values. If the value's are empty, the text field stay empty and you would still see the hint
                // given to the edit text in het xml.
                mNaam.setText(naam);
                mAchternaam.setText(achternaam);
                mEmail.setText(email);
                mTelefoonnummer.setText(telefoonnummer);
                mAdres.setText(adres);
                mHuisnummer.setText(huisnummer);
                mToevoeging.setText(toevoeging);
                mWoonplaats.setText(woonplaats);
                mPostcode.setText(postcode);
                mGeboortedatum.setText(geboortedatum);

                // To set the spinner equal to the value known in the database, a setSelection funtion is called. It takes the string created above, and compares it
                // to the strings known in the adapter. If it finds a string that is equal, it gets the position of that string in the array and passes it in the
                // setSelection function. This makes that position the default selected when loading the fragment again.
                mGeslacht.setSelection(adapterGeslacht.getPosition(geslacht));
                mNationaliteit.setSelection(adapterNationaliteit.getPosition(nationaliteit));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Setting the OnClick event for the opslaan button.
        mOpslaan = (Button) view.findViewById(R.id.OpslaanBtn);
        mOpslaan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // To get item selected in the spinner into the database, the following if else statement is execcuted:
                // First, the position from the currently selected item in the spinner gets passed into a variable.
                // Then the position gets checked. If the position is 0, its the default and nothing should be passed into
                // the database. Therefore, the variable that gets put into the database is equal to "". If the position is not
                // 0, the item corresponding to the position gets selected and converted to a string. This string gets passed
                // into a variable after which the variable gets put into the database. This proces repeats it self for al the spinnners
                int positionGeslacht = mGeslacht.getSelectedItemPosition();
                if (positionGeslacht > 0)
                {
                    String geslacht = mGeslacht.getSelectedItem().toString().trim();
                    myRef.child("Users").child(UserID).child("Persoonlijke informatie").child("Geslacht").setValue(geslacht);
                }
                else
                {
                    String geslacht = "";
                    myRef.child("Users").child(UserID).child("Persoonlijke informatie").child("Geslacht").setValue(geslacht);
                }

                int positionNationaliteit = mNationaliteit.getSelectedItemPosition();
                if (positionNationaliteit > 0)
                {
                    String nationaliteit = mNationaliteit.getSelectedItem().toString().trim();
                    myRef.child("Users").child(UserID).child("Persoonlijke informatie").child("Nationaliteit").setValue(nationaliteit);
                }
                else
                {
                    String nationaliteit = "";
                    myRef.child("Users").child(UserID).child("Persoonlijke informatie").child("Nationaliteit").setValue(nationaliteit);
                }

                // For all the other EditTexts in the fragment, we first create a string in which the text that is filled in the edit text gets put.
                String naam = mNaam.getText().toString().trim();
                String achternaam = mAchternaam.getText().toString().trim();
                String email = mEmail.getText().toString().trim().toLowerCase();
                String telefoonnummer = mTelefoonnummer.getText().toString().trim();
                String adres = mAdres.getText().toString().trim();
                String huisnummer = mHuisnummer.getText().toString().trim();
                String toevoeging = mToevoeging.getText().toString().trim();
                String woonplaats = mWoonplaats.getText().toString().trim();
                String postcode = mPostcode.getText().toString().trim();
                String geboortedatum = mGeboortedatum.getText().toString().trim();

                // When all the strings are created, they get put in an HashMap. This HashMap is used to send the data to the database.
                // For every string, a child name gets declared.
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("Naam", naam);
                dataMap.put("Achternaam", achternaam);
                dataMap.put("Email", email);
                dataMap.put("Telefoonnummer", telefoonnummer);
                dataMap.put("Adres", adres);
                dataMap.put("Huisnummer", huisnummer);
                dataMap.put("Toevoeging", toevoeging);
                dataMap.put("Woonplaats", woonplaats);
                dataMap.put("Postcode", postcode);
                dataMap.put("Geboortedatum", geboortedatum);

                // When the HashMap is completed, the HashMap gets send to the database. The data get put under the child 'User' with a
                // Parent equal to there UserID.
                myRef.child("Users").child(UserID).child("Persoonlijke informatie").updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
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
                            Toast.makeText(getActivity(), "Uw persoonlijke informatie is opgeslagen", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // If something goes wrong, a toast shows an error.
                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    // The set profile image function. This function is used to set the image of the image view, displaying either the photo uploaded by the user,
    // or an stock image. First, the UserID gets converted to a string, as it needed for the storage reference. The profile pictures are uploaded under
    // The users UserID, and are stored in a folder called Profile photos. So to get the profile picture, a reference to the profile pictures map with the UserID
    // is made. When successful, the profile picture get put in the image view with Picasso. If there is no picture present, and on failure listener
    // Will execute, making the image a stock image from vaavio.
    private void setProfileImage()
    {
        String userID = UserID.toString();

        mStorage.child("Profile photos/" + userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                Picasso.get().load(uri).fit().centerCrop().into(mProfielFoto);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Picasso.get().load("https://www.vaavio.nl/wp-content/plugins/wp-jobhunt/assets/images/img-not-found4x3.jpg").fit().centerCrop().into(mProfielFoto);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Declaring the request we want to take care of, and checking if the request went successful
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK)
        {
            // Showing the progress dialog while uploading the image
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();

            // Getting the uri
            Uri uri = data.getData();

            // Setting up the path to the location to the image in the database and putting the image in the database
            StorageReference filepath = mStorage.child("Profile photos").child(UserID);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                // When successful, a toast will show saying the upload was successful and the progress dialog will go.
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(getActivity(), "Upload done.", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.get().load(downloadUri).fit().centerCrop().into(mProfielFoto);
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
                getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
        }
    }
}