package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartschermFragment extends Fragment {

    @Nullable
    @Override
    //Het koppelen van de layout aan et fragment. 'OnCreate', dus wanneer het fragment wordt gemaakt maakt hij gebruikt van de layout genoemnd in onderstaande code.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_startscherm, container, false);
    }
}
