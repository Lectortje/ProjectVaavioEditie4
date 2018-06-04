package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfielWerknemerFragment extends Fragment
{

    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private Button mLogOut;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profielwerknemer, container, false);

        setupFireBaseListener();

        Button CvBtn = (Button) view.findViewById(R.id.CvBtn);
        CvBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CvWerknemerProfielFragment()).addToBackStack("tag").commit();
            }
        });

        Button PersoonlijkeInfoBtn = (Button) view.findViewById(R.id.PersoonlijkeInfoBtn);
        PersoonlijkeInfoBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PersoonlijkeinformatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        Button SollicitatiesBtn = (Button) view.findViewById(R.id.SollicatiesBtn);
        SollicitatiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new OverzichtSollicitatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        Button ProfielWeergaveBtn = (Button) view.findViewById(R.id.ProfielWeergaveBtn);
        ProfielWeergaveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfielWeergaveProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        Button MotivatieBtn = (Button) view.findViewById(R.id.MotivatieBtn);
        MotivatieBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new MotivatieProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        Button InboxBtn = (Button) view.findViewById(R.id.InboxBtn);
        InboxBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new InboxProfielWerknemer()).addToBackStack("tag").commit();
            }
        });

        Button AccountinstellingenBtn = (Button) view.findViewById(R.id.AccountinstellingenBtn);
        AccountinstellingenBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AccountInstellingenProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        });

        mLogOut = (Button) view.findViewById(R.id.LogOutBtn);
        mLogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FirebaseAuth.getInstance().signOut();

            }
        });

        return view;
    }

    private void setupFireBaseListener()
    {
        mAuthstateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)
                {
                    Toast.makeText(getActivity(), "Uitgelogd", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), InlogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthstateListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthstateListener != null)
        {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthstateListener);
        }
    }
}

