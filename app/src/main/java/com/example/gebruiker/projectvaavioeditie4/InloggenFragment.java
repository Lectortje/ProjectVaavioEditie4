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
import android.widget.TextView;

public class InloggenFragment extends Fragment {

    @Nullable
    @Override
    //Het koppelen van de layout aan et fragment. 'OnCreate', dus wanneer het fragment wordt gemaakt maakt hij gebruikt van de layout genoemnd in onderstaande code.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inloggen, container, false);

        TextView TextViewRegistreren = (TextView) view.findViewById(R.id.TextViewRegistreren);
        TextViewRegistreren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegistrerenFragment()).addToBackStack("tag").commit();
            }
        });

        TextView TextViewWachtwoordVergeten = (TextView) view.findViewById(R.id.TextViewWachtwoordVergeten);
        TextViewWachtwoordVergeten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new WachtwoordVergetenFragment()).addToBackStack("tag").commit();
            }
        });
        Button InlogBtn = (Button) view.findViewById(R.id.InlogBtn);
        InlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        return view;
    }

}

