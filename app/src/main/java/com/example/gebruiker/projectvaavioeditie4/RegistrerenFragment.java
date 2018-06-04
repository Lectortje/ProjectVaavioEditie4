package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class RegistrerenFragment extends Fragment {

    private DatabaseReference mDatabase;
    private EditText mGebruikersnaam;
    private EditText mWachtwoord;
    private EditText mEmail;
    private EditText mTelefoonnummer;
    private Button mRegistreren;

    @Nullable
    @Override
    //Het koppelen van de layout aan et fragment. 'OnCreate', dus wanneer het fragment wordt gemaakt maakt hij gebruikt van de layout genoemnd in onderstaande code.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registreren, container, false);

        TextView AlgemeneVoorwardenTextView = (TextView) view.findViewById(R.id.AlgemeneVoorwaardenTextView);
        AlgemeneVoorwardenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
            }
        });

        // mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // mGebruikersnaam = (EditText) view.findViewById(R.id.EditTextGebruikersnaam);
        mWachtwoord = (EditText) view.findViewById(R.id.EditTextWachtwoord);
        mEmail = (EditText) view.findViewById(R.id.EditTextEmail);
        // mTelefoonnummer = (EditText) view.findViewById(R.id.EditTextTelefoonnummer);
        mRegistreren = (Button) view.findViewById(R.id.RegistratieVoltooienBtn);

        /* mRegistreren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gebruikersnaam = mGebruikersnaam.getText().toString().trim();
                String wachtwoord = mWachtwoord.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String telefoonnummer = mTelefoonnummer.getText().toString().trim();

                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("Gebruikersnaam", gebruikersnaam);
                dataMap.put("Wachtwoord", wachtwoord);
                dataMap.put("Ëmail", email);
                dataMap.put("Telefoonnummer", telefoonnummer);

                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new InloggenFragment()).commit();
                            Toast.makeText(getActivity(), "U account is aangemaakt, u kunt nu inloggen", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }); */

        return view;
    }
}


