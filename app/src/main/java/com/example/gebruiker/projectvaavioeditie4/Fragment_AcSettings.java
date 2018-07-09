package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Fragment_AcSettings extends Fragment
{
    //Declaring the variables
    private EditText mPasswordChange, mNewPassword, mNewPassword2, mPasswordDelete;
    private Button mBevestigen, mVerwijderen;
    private String UserID;
    private StorageReference mStorage;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_settings, container, false);

        // Setting the variables equal to their corresponding objects
        mPasswordChange = view.findViewById(R.id.EditTextWachtwoordWijzigen);
        mNewPassword = view.findViewById(R.id.EditTextNieuwWachtwoord);
        mNewPassword2 =  view.findViewById(R.id.EditTextNieuwWachtwoord2);
        mPasswordDelete = view.findViewById(R.id.EditTextWachtwoordDelete);
        mBevestigen = view.findViewById(R.id.BevestingenBtn);

        mAuth = FirebaseAuth.getInstance();

        getActivity().setTitle("Account settings");

        // Setting the OnClickListener  for the bevestigen button
        mBevestigen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Getting the current logged in user and putting it in a variable
                mUser = mAuth.getCurrentUser();

                // Getting the email from the current user and putting it in a email string. Also getting the password filled in the edit text
                // And putting it in a string variable as well
                final String email = mUser.getEmail();
                final String oldpassword = mPasswordChange.getText().toString();

                // Checking if the oldpassword isn't empty when the button is clicked, if the case however, a toast will show asking the user
                // to fill in the password.
                if (oldpassword.isEmpty())
                {
                    Toast.makeText(getActivity(), "Vul uw oude wachtwoord in", Toast.LENGTH_LONG).show();
                }
                // If the password edit text ain't empty, the password and email get combined as a credential. This credential gets the checked
                // with the database to see if the user trying to reset the password is really him.
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldpassword);

                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            // If the reauthentication is successful, the following gets executed
                            if (task.isSuccessful())
                            {
                                // First, the inputs of the new password fields get put in a variable string
                                String newpassword  = mNewPassword.getText().toString().trim();
                                String newpassword2 = mNewPassword2.getText().toString().trim();

                                // Secondly, there is a check to see if new password 1 ain't empty, if that's the case, a toast will show
                                // Asking the user to fill in a new password
                                if (newpassword.isEmpty())
                                {
                                    Toast.makeText(getActivity(), "Vul een nieuw wachtwoord in", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                // Thirdly, there is a check to see if new password 2 ain't empty, if that's the case, a toast will show
                                // Asking the user to repeat there new password
                                if (newpassword2.isEmpty())
                                {
                                    Toast.makeText(getActivity(), "Herhaal het nieuwe wachtwoord", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                // After that, the new passwords length gets checked. Firebase Auth requires passwords longer then 6 tokens. Therefore,
                                // The new password needs to be at least 6. If this ain't the case, a toast will show asking the user to make a longer password
                                if (newpassword.length() < 6)
                                {
                                    Toast.makeText(getActivity(), "Het nieuwe wachtwoord moet minimaal 6 tekens lang zijn", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                // At last, the 2 password fields get compared to see if they are the same. If not, the user gets a toast asking to
                                // recheck the filled in password to see if they are the same
                                if (!newpassword.equals(newpassword2))
                                {
                                    Toast.makeText(getActivity(), "Nieuw wachtwoord komt niet overeen", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // If all above checks are fine, the users password gets updated.
                                mUser.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        // If the task isn't successful, a toast will show something went wrong
                                        if (!task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                                        }
                                        // When the task is successful, the user gets signed out and the user gets redirected to the login screen. Also, the back
                                        // stack gets cleared. Lastly, a toast will show everything went successful and asking the user to login again
                                        else
                                        {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(getActivity(), Activity_Login.class);
                                            startActivity(intent);
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            Toast.makeText(getActivity(), "Wachtwoord succesvol veranderd, log opnieuw in", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                            // If the reauthentication fails, a toast will show saying a wrong password is filled in
                            else
                            {
                                Toast.makeText(getActivity(), "Verkeerd wachtwoord ingevuld", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        mVerwijderen = (Button) view.findViewById(R.id.AccountVerwijderenBtn);
        mVerwijderen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Getting the current logged in user and putting it in a variable
                mUser = mAuth.getCurrentUser();

                // Getting the email from the current user and putting it in a email string. Also getting the password filled in the edit text
                // And putting it in a string variable as well
                final String email = mUser.getEmail();
                final String currentpassword = mPasswordDelete.getText().toString();

                // Checking if the oldpassword isn't empty when the button is clicked, if the case however, a toast will show asking the user
                // to fill in the password.
                if (currentpassword.isEmpty())
                {
                    Toast.makeText(getActivity(), "Vul uw huidige wachtwoord in", Toast.LENGTH_LONG).show();
                }
                // If the password edit text ain't empty, the password and email get combined as a credential. This credential gets the checked
                // with the database to see if the user trying to reset the password is really him.
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(email, currentpassword);

                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            // If the reauthentication is not successful, the user will get a toast saying the password filled in is incorrect
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(getActivity(), "Wachtwoord is incorrect", Toast.LENGTH_LONG).show();
                            }
                            // When te reauthentication is successful, all the data connected to the users userId will be deleted from
                            // the database and the storage. For this, a connection to the database and storage get made, and for every
                            // entry in the database connected to the UserID the data will be deleted.
                            else
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserID = user.getUid();
                                String userID = UserID.toString();
                                mDatabase = FirebaseDatabase.getInstance();
                                myRef = mDatabase.getReference();
                                mStorage = FirebaseStorage.getInstance().getReference();

                                myRef.child("Users").child(UserID).removeValue();
                                myRef.child("Sollicitaties").child(UserID).removeValue();
                                myRef.child("Contact").child("Berichten").child(UserID).removeValue();
                                mStorage.child("Profile photos/" + userID).delete();

                                // When all the data is deleted, the account itself gets deleted.
                                mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        // When the account does not get successfully deleted, a toast will show saying something went wrong
                                        if (!task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                                        }
                                        // When the account gets deleted, the user gets signed out and redirected to the login page. The back stack
                                        // also gets cleared and a toast will show saying the account is deleted successfully.
                                        else
                                        {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(getActivity(), Activity_Login.class);
                                            startActivity(intent);
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            Toast.makeText(getActivity(), "Uw account is verwijderd", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}
