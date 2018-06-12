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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Fragment_AcPersonalInfo extends Fragment
{
    private EditText mNaam;
    private EditText mAchternaam;
    private EditText mEmail;
    private EditText mTelefoonnummer;
    private EditText mAdres;
    private EditText mHuisnummer;
    private EditText mToevoeging;
    private EditText mWoonplaats;
    private EditText mPostcode;
    private EditText mLeeftijd;
    private EditText mGeslacht;
    private EditText mNationaliteit;
    private Button mOpslaan;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_personal_info, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mNaam = (EditText) view.findViewById(R.id.NaamEditText);
        mAchternaam = (EditText) view.findViewById(R.id.AchternaamEditText);
        mEmail = (EditText) view.findViewById(R.id.EmailEditText);
        mTelefoonnummer = (EditText) view.findViewById(R.id.TelefoonEditText);
        mAdres = (EditText) view.findViewById(R.id.AdresEditText);
        mHuisnummer = (EditText) view.findViewById(R.id.HuisnummerEditText);
        mToevoeging = (EditText) view.findViewById(R.id.ToevoegingEditText);
        mWoonplaats = (EditText) view.findViewById(R.id.WoonplaatsEditText);
        mPostcode = (EditText) view.findViewById(R.id.PostcodeEditText);
        mLeeftijd = (EditText) view.findViewById(R.id.LeeftijdEditText);
        mGeslacht = (EditText) view.findViewById(R.id.GeslachtEditText);
        mNationaliteit = (EditText) view.findViewById(R.id.NationaliteitEditText);

        mOpslaan = (Button) view.findViewById(R.id.OpslaanBtn);
        mOpslaan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String naam = mNaam.getText().toString().trim();
                String achternaam = mAchternaam.getText().toString().trim();
                String email = mEmail.getText().toString().trim().toLowerCase();
                String telefoonnummer = mTelefoonnummer.getText().toString().trim();
                String adres = mAdres.getText().toString().trim();
                String huisnummer = mHuisnummer.getText().toString().trim();
                String toevoeging = mToevoeging.getText().toString().trim();
                String woonplaats = mWoonplaats.getText().toString().trim();
                String postcode = mPostcode.getText().toString().trim();
                String leeftijd = mLeeftijd.getText().toString().trim();
                String geslacht = mGeslacht.getText().toString().trim();
                String nationaliteit = mNationaliteit.getText().toString().trim();

                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("Naam", naam);
                dataMap.put("Achternaam", achternaam);
                dataMap.put("Ëmail", email);
                dataMap.put("Telefoonnummer", telefoonnummer);
                dataMap.put("Adres", adres);
                dataMap.put("Huisnummer", huisnummer);
                dataMap.put("Toevoeging", toevoeging);
                dataMap.put("Woonplaats", woonplaats);
                dataMap.put("Postcode", postcode);
                dataMap.put("Leeftijd", leeftijd);
                dataMap.put("Geslacht", geslacht);
                dataMap.put("Nationalieit", nationaliteit);

                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new Fragment_Account()).addToBackStack("tag").commit();
                            Toast.makeText(getActivity(), "Uw persoonlijke informatie is opgeslagen", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Er is iets fout gegaan, probeer het opnieuw", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
