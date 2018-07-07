package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Fragment_AcFavorites extends Fragment
{
    private RecyclerView mRecyclerView;
    private List<VacatureModule> result;
    private VacatureViewAdapter adapter;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private String UserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ac_favorites, container, false);

        // Setting the result as an Array list
        result = new ArrayList<>();

        // Making the refrence to the recyclerview
        mRecyclerView = view.findViewById(R.id.recycler_view);

        // Setting up the adapter and LayoutManager for the RecyclerView
        adapter = new VacatureViewAdapter(getActivity(), result);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutmanager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        // Executing the create result function to create placeholders for the RecyclerView, created down below
        updateList();

        return view;
    }

    // Adding the data from the database into the RecyclerView.
    private void updateList()
    {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference().child("Favorites").child(UserID);
        myRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                result.add(dataSnapshot.getValue(VacatureModule.class));
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
