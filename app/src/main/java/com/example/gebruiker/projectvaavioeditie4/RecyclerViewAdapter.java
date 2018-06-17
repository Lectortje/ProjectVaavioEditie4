package com.example.gebruiker.projectvaavioeditie4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VacatureViewHolder>
{

    // Creating the variable. The list is set to the type create in the VacatureModel
    private List<VacatureModel> list;
    public Context mContext;

    // Setting up the RecyclerViewAdapter. The adapter needs the context and list to work properly
    public RecyclerViewAdapter(Context mContext, List<VacatureModel> list)
    {
        this.list = list;
        this.mContext = mContext;
    }

    // Connecting the layout to the view holder onCreate
    @Override
    public VacatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new VacatureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (VacatureViewHolder holder, int position)
    {

        VacatureModel vacature = list.get(position);

        // Setting the text of the items in the layout equal to the text of the items in the list
        holder.mTitle.setText(vacature.Title);
        holder.mLocatie.setText(vacature.Locatie);
        holder.mOmschrijving.setText(vacature.Omschrijving);

        // Declaring what to do when an item in the RecyclerView is clicked.
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, Activity_VacOmschrijving.class);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class VacatureViewHolder extends RecyclerView.ViewHolder
    {

        TextView mTitle, mLocatie, mOmschrijving;

        public VacatureViewHolder(View view)
        {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.job_title);
            mLocatie = (TextView) view.findViewById(R.id.locatie);
            mOmschrijving = (TextView) view.findViewById(R.id.omschrijving);
        }

    }
}
