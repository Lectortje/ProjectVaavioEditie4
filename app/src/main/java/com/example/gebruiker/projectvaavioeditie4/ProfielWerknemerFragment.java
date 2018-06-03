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

public class ProfielWerknemerFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profielwerknemer, container, false);

        Button CvBtn = (Button) view.findViewById(R.id.CvBtn);
        CvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CvWerknemerProfielFragment()).addToBackStack("tag").commit();
            }
        });
        Button PersoonlijkeInfoBtn = (Button) view.findViewById(R.id.PersoonlijkeInfoBtn);
        PersoonlijkeInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PersoonlijkeinformatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });
        Button SollicitatiesBtn = (Button) view.findViewById(R.id.SollicatiesBtn);
        SollicitatiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new OverzichtSollicitatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });
        Button ProfielWeergaveBtn = (Button) view.findViewById(R.id.ProfielWeergaveBtn);
        ProfielWeergaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfielWeergaveProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });
        Button MotivatieBtn = (Button) view.findViewById(R.id.MotivatieBtn);
        MotivatieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new MotivatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });
        Button InboxBtn = (Button) view.findViewById(R.id.InboxBtn);
        InboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new InboxProfielWerknemer()).addToBackStack("tag").commit();
            }
        });
        Button AccountinstellingenBtn = (Button) view.findViewById(R.id.AccountinstellingenBtn);
        AccountinstellingenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AccountInstellingenProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });


        return view;
    }
}
