package com.example.gebruiker.projectvaavioeditie4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InboxViewAdapter extends RecyclerView.Adapter<InboxViewAdapter.InboxViewHolder>
{

    private List<InboxModule> list;
    public Context mContext;

    public InboxViewAdapter(Context mContext, List<InboxModule> list)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inbox, parent, false);
        final InboxViewHolder holder = new InboxViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder (final InboxViewHolder holder, int position)
    {

        final InboxModule mail = list.get(position);

        holder.mVerzender.setText(mail.Verzender);
        holder.mBericht.setText(mail.Bericht);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, Activity_VacOmschrijving.class);
                intent.putExtra("Key", mail.Key);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class InboxViewHolder extends RecyclerView.ViewHolder
    {

        TextView mVerzender, mBericht;

        public InboxViewHolder(View view)
        {
            super(view);

            mVerzender = (TextView) view.findViewById(R.id.TextViewAfzender);
            mBericht = (TextView) view.findViewById(R.id.TextViewBericht);
        }

    }

}
