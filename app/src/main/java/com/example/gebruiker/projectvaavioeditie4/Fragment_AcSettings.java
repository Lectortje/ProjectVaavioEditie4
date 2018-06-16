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
import com.google.firebase.storage.StorageReference;

public class Fragment_AcSettings extends Fragment
{
    private EditText mPasswordChange, mNewPassword, mNewPassword2, mPasswordDelete;
    private Button mBevestigen, mVerwijderen;
    private FirebaseDatabase mDatabase;
    private String UserID;
    private DatabaseReference myRef;
    private StorageReference mStorage;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_settings, container, false);

        mPasswordChange = view.findViewById(R.id.EditTextWachtwoordWijzigen);
        mNewPassword = view.findViewById(R.id.EditTextNieuwWachtwoord);
        mNewPassword2 =  view.findViewById(R.id.EditTextNieuwWachtwoord2);
        mPasswordDelete = view.findViewById(R.id.EditTextWachtwoordDelete);

        mAuth = FirebaseAuth.getInstance();

        mBevestigen = (Button) view.findViewById(R.id.BevestingenBtn);
        mBevestigen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mUser = mAuth.getCurrentUser();
                final String email = mUser.getEmail();
                final String oldpassword = mPasswordChange.getText().toString();

                AuthCredential credential = EmailAuthProvider.getCredential(email, oldpassword);

                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            String newpassword  = mNewPassword.getText().toString();
                            String newpassword2 = mNewPassword2.getText().toString();

                            if (!newpassword.equals(newpassword2))
                            {
                                Toast.makeText(getActivity(), "Nieuw wachtwoord komt niet overeen", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (newpassword.length() < 6)
                            {
                                Toast.makeText(getActivity(), "Het nieuwe wachtwoord moet minimaal 6 tekens lang zijn", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else
                            {
                                mUser.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (!task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                                        }
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
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Verkeerd wachtwoord ingevuld", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        mVerwijderen = (Button) view.findViewById(R.id.AccountVerwijderenBtn);
        mVerwijderen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mUser = mAuth.getCurrentUser();

                final String email = mUser.getEmail();
                final String currentpassword = mPasswordDelete.getText().toString();

                AuthCredential credential = EmailAuthProvider.getCredential(email, currentpassword);

                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Wachtwoord is incorrect", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (!task.isSuccessful())
                                    {
                                        Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                                    }
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
        });

        return view;
    }
}
