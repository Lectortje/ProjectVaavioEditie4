package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_AcInbox extends Fragment
{
    //A floating button variable is created.
    private FloatingActionButton mFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
            //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
            //This also holds the content of the fragment to display.
            View view = inflater.inflate(R.layout.fragment_ac_inbox, container, false);

            //A floating button enables the user to create a new email.
            FloatingActionButton mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingNieuweMail);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

                @Override
                //The onclick handling is defined below when the button is clicked.
                public void onClick(View view) {
                    //The button will transfer the user to the fragment 'CreateMail'. Here the user can create a new email.
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_CreateMail()).addToBackStack("tag").commit();
                }
            });
            //Returning view in order to show the layout created in the xml for the fragment.
             //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
            return view;
        }
    }
