package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Fragment_AcInbox extends Fragment
{
    //A floating button variable is created.
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private List<InboxModule> result;
    private InboxViewAdapter adapter;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //In order to be able to return the view at the end there has to be a variable created called 'view' which is done here with 'View view ='.
        //This also holds the content of the fragment to display.
        View view = inflater.inflate(R.layout.fragment_ac_inbox, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        //A floating button enables the user to create a new email.
        mFloatingActionButton = view.findViewById(R.id.floatingNieuweMail);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //The onclick handling is defined below when the button is clicked.
            public void onClick(View view)
            {
                //The button will transfer the user to the fragment 'CreateMail'. Here the user can create a new email.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_CreateMail()).addToBackStack("tag").commit();
            }
        });

        // Setting the result as an Array list
        result = new ArrayList<>();

        // Making the refrence to the recyclerview
        mRecyclerView = view.findViewById(R.id.recycler_view);

        // Setting up the adapter and LayoutManager for the RecyclerView
        adapter = new InboxViewAdapter(getActivity(), result);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutmanager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        updateList();

        //Returning view in order to show the layout created in the xml for the fragment.
        //This is also in order to ensure that the buttons inside the fragment can be assigned and can be clicked and open other screens (activities or fragments).
        return view;
    }

    // Adding the data from the database into the RecyclerView.
    private void updateList()
    {
        // First, the database connection gets initialized.
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String user = mUser.getEmail();
        {
            Query userquery = myRef.child("Inbox").orderByChild("Ontvanger").equalTo(user);
            userquery.addChildEventListener(new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s)
                {
                    result.add(dataSnapshot.getValue(InboxModule.class));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s)
                {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot)
                {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s)
                {

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
    }

}
