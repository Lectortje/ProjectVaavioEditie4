package com.example.gebruiker.projectvaavioeditie4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VacatureViewAdapter extends RecyclerView.Adapter<VacatureViewAdapter.VacatureViewHolder>
{

    // Creating the variable. The list is set to the type create in the VacatureModule
    private List<VacatureModule> list;
    public Context mContext;

    // Setting up the VacatureViewAdapter. The adapter needs the context and list to work properly
    public VacatureViewAdapter(Context mContext, List<VacatureModule> list)
    {
        this.list = list;
        this.mContext = mContext;
    }

    // Connecting the layout to the view holder onCreate
    @Override
    public VacatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vacatures, parent, false);
        final VacatureViewHolder holder = new VacatureViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder (final VacatureViewHolder holder, int position)
    {

        final VacatureModule vacature = list.get(position);

        // Setting the text of the items in the layout equal to the text of the items in the list. The list is filled with the data from the
        // Database.
        holder.mFunctie.setText(vacature.Functie);
        holder.mLocatie.setText(vacature.Locatie);
        holder.mOmschrijving.setText(vacature.Omschrijving);

        // Declaring what to do when an item in the RecyclerView is clicked. In this case it's not just starting an intent to a new activity,
        // It is also sending the Key of the item clicked in the RecyclerView with that intent. This way, in vacature omschrijving we know
        // Which data needs te be loaded in.
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, Activity_VacOmschrijving.class);
                intent.putExtra("Key", vacature.Key);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    // Declaring the ViewHolder for the vacatures. Here fore the TextView's from the layout_vacatures get added to there corresponding variables
    // This way, the TextView's can get edited in the onBindViewHolder.
    class VacatureViewHolder extends RecyclerView.ViewHolder
    {

        TextView mFunctie, mLocatie, mOmschrijving;

        public VacatureViewHolder(View view)
        {
            super(view);

            mFunctie = (TextView) view.findViewById(R.id.TextViewRVFunctie);
            mLocatie = (TextView) view.findViewById(R.id.TextViewRVLocatie);
            mOmschrijving = (TextView) view.findViewById(R.id.TextViewRVOmschrijving);
        }

    }
}
