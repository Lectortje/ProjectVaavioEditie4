package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WachtwoordVergetenFragment extends Fragment {

    @Nullable
    @Override
    //Het koppelen van de layout aan et fragment. 'OnCreate', dus wanneer het fragment wordt gemaakt maakt hij gebruikt van de layout genoemnd in onderstaande code.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wwvergeten, container, false);
/*
        Button HerstellenBtn = (Button) view.findViewById(R.id.HerstellenBtn);
        HerstellenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ).commit();
                Toast.makeText(getActivity(), "Volg de intructies in de mail om uw wachtwoord te herstellen", Toast.LENGTH_LONG).show();
            }
        });

        */
        return view;
    }
}
