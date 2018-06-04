package com.example.gebruiker.projectvaavioeditie4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InloggenFragment extends Fragment {

    private EditText mEmail;
    private EditText mWachtwoord;
    private Button mInlog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inloggen, container, false);

        TextView TextViewRegistreren = (TextView) view.findViewById(R.id.TextViewRegistreren);
        TextViewRegistreren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegistrerenFragment()).addToBackStack("tag").commit();
            }
        });

        TextView TextViewWachtwoordVergeten = (TextView) view.findViewById(R.id.TextViewWachtwoordVergeten);
        TextViewWachtwoordVergeten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new WachtwoordVergetenFragment()).addToBackStack("tag").commit();
            }
        });

        mEmail = (EditText) view.findViewById(R.id.EditTextEmail);
        mWachtwoord = (EditText) view.findViewById(R.id.EditTextWachtwoord);
        mInlog = (Button) view.findViewById(R.id.InlogBtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new ProfielWerknemerFragment()).addToBackStack("tag").commit();

                }
            }
        };

        mInlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String wachtwoord = mWachtwoord.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(wachtwoord)){
                    Toast.makeText(getActivity(), "Velden zijn leeg", Toast.LENGTH_LONG).show();
                } else {

                    mAuth.signInWithEmailAndPassword(email, wachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Probleem bij het inloggen", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

       /* Button InlogBtn = (Button) view.findViewById(R.id.InlogBtn);
        InlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfielWerknemerFragment()).addToBackStack("tag").commit();
            }
        }); */

        return view;

    }
}

