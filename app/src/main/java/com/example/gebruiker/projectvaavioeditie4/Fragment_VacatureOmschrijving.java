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

public class Fragment_VacatureOmschrijving extends Fragment
{
    //Declaring all the button variables.
    private Button mSollicitatie, mContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_vacatureomschrijving, container, false);

        //Assingning the button variables to the button ID.
        mSollicitatie = view.findViewById(R.id.btnSoll);
        mSollicitatie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //This is the onClick handling for the button btnSoll.
            public void onClick(View v)
            {
                //This opens the fragment Fragment_Sollicitatie if the button btnSoll is clicked.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Sollicitatie()).addToBackStack("tag").commit();
            }

        });
        //Assingning the button variables to the button ID.
        mContact = view.findViewById(R.id.btnContact);
        mContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //This is the onClick handling for the button btnContact.
            public void onClick(View v)
            {
                //This opens the fragment Fragment_Contact if the button btnContact is clicked.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Contact()).addToBackStack("tag").commit();
            }

        });
        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }
}
