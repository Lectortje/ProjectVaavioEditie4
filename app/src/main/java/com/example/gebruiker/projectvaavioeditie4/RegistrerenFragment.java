package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

public class RegistrerenFragment extends Fragment {

    @Nullable
    @Override
    //Het koppelen van de layout aan et fragment. 'OnCreate', dus wanneer het fragment wordt gemaakt maakt hij gebruikt van de layout genoemnd in onderstaande code.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registreren, container, false);

        Button RegistratieVoltooirenBtn = (Button) view.findViewById(R.id.RegistratieVoltooienBtn);
        RegistratieVoltooirenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new InloggenFragment()).commit();
                Toast.makeText(getActivity(), "U account is aangemaakt, u kunt nu inloggen", Toast.LENGTH_LONG).show();
            }
        });
        TextView AlgemeneVoorwardenTextView = (TextView) view.findViewById(R.id.AlgemeneVoorwaardenTextView);
        AlgemeneVoorwardenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent av = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vaavio.nl/terms-and-conditions/"));
                startActivity(av);
            }
        });

        return view;
    }
}


