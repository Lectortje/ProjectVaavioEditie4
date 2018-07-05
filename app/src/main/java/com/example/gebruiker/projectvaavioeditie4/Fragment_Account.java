package com.example.gebruiker.projectvaavioeditie4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Fragment_Account extends Fragment
{

    // Creating the variables
    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private Button mLogOut, mCv, mPersoonlijkeInfo, mSollicitaties, mProfielWeergave, mMotivatie, mInbox, mSettings;

    // When loading the fragment, the fragment has to be assigned a layout. This happens through the inflate method.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mLogOut = view.findViewById(R.id.LogOutBtn);
        mCv = view.findViewById(R.id.CvBtn);
        mPersoonlijkeInfo = view.findViewById(R.id.PersoonlijkeInfoBtn);
        mSollicitaties = view.findViewById(R.id.SollicatiesBtn);
        mProfielWeergave = view.findViewById(R.id.ProfielWeergaveBtn);
        mMotivatie = view.findViewById(R.id.MotivatieBtn);
        mInbox = view.findViewById(R.id.InboxBtn);
        mSettings = view.findViewById(R.id.AccountinstellingenBtn);

        // Executing the FireBaseListener function created below to log the user out.
        setupFireBaseListener();

        // Setting the onClickListener for the button. When the button is clicked, a fragment transaction will start replacing the
        // Current fragment to the fragment called in the method. The fragment however gets added to the back stack so the user can press
        // The back button in case it wants to go back. This prevents the app from getting closed start away when the back button is pressed.
        // The same method is used for all buttons below, with the only change being the fragment names.
        mCv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcCV()).addToBackStack("tag").commit();
            }
        });

        mPersoonlijkeInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcPersonalInfo()).addToBackStack("tag").commit();
            }
        });

        mSollicitaties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Activity_Favorites.class);
                startActivity(intent);
            }
        });

        mProfielWeergave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcDisplayProfile()).addToBackStack("tag").commit();
            }
        });

        mMotivatie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcMotivation()).addToBackStack("tag").commit();
            }
        });

        mInbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcInbox()).addToBackStack("tag").commit();
            }
        });

        mSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_AcSettings()).addToBackStack("tag").commit();
            }
        });

        // Setting the onClickListener for the LogOut button. When clicked the button starts the log out function from Firebase.
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

    // Creating the FireBaseListener
    private void setupFireBaseListener()
    {
        mAuthstateListener = new FirebaseAuth.AuthStateListener()
        {
            // Declaring what to do when the user gets logged out. First there is a check to see if the user is logged out. For this
            // it gets the current user form firebase, and if the user is null, the code gets executed. When confirmed that the user
            // is indeed logged out, a toast gets displayed and a new intent gets started. This intent redirects the user to the
            // Login activity. It also clears the activity stack that may have stacked up.
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)
                {
                    Toast.makeText(getActivity(), "Uitgelogd", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Activity_Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    // When the activity gets created, the AuthStateListener gets attached to the FirebaseAuth, and it gets removed onStop
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

