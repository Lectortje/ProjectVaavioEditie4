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

    private List<VacatureModel> list;
    public Context mContext;

    public RecyclerViewAdapter(Context mContext, List<VacatureModel> list)
    {
        this.list = list;
        this.mContext = mContext;
    }

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

        holder.mTitle.setText(vacature.Title);
        holder.mLocatie.setText(vacature.Locatie);
        holder.mOmschrijving.setText(vacature.Omschrijving);

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
