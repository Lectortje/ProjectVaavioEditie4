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

    //Declaring all the List and Context variables.
    private List<InboxModule> list;
    public Context mContext;

    //A public viewadapter containing the Context and the List is set up.
    public InboxViewAdapter(Context mContext, List<InboxModule> list)
    {
        //The Context and List are set equal to their varaibeles assigned earlier.
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    // Connecting the layout to the view holder onCreate
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //In order to be able to view the items in the application, an inflation of the layout is set up. The layout inflation is used to
        // instantiate the correct layout and load it into the corresponding screen in order to be able to see it. In this case it is the
        // layout_inbox that is needed in the Fragment_AcInbox.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inbox, parent, false);
        final InboxViewHolder holder = new InboxViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder (final InboxViewHolder holder, int position)
    {

        final InboxModule mail = list.get(position);

        // Setting the text of the items in the layout equal to the text of the items in the list. The list is filled with the data from the
        // Database.
        holder.mVerzender.setText(mail.Verzender);
        holder.mBericht.setText(mail.Bericht);
        holder.mOnderwerp.setText(mail.Onderwerp);

        // Declaring what to do when an item in the RecyclerView is clicked. In this case it's not just starting an intent to a new activity,
        // It is also sending the Key of the item clicked in the RecyclerView with that intent. This way, in vacature omschrijving we know
        // Which data needs te be loaded in.
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, Activity_Mail.class);
                intent.putExtra("Key", mail.Key);
                mContext.startActivity(intent);
            }
        });

    }


    //The items are being loaded, one by one, into the recyclerview so that they are displayed to the user.
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class InboxViewHolder extends RecyclerView.ViewHolder
    {

        //The variables are being assigned as TextView's to be able to set them equal to TextView functions inside the InboxViewHolder.
        TextView mVerzender, mBericht, mOnderwerp;

        public InboxViewHolder(View view)
        {
            super(view);
            //The variables are set equal to the corresponding TextView's so that they are correctly displayed in the recyclerview list.
            mVerzender = view.findViewById(R.id.TextViewAfzender);
            mBericht = view.findViewById(R.id.TextViewBericht);
            mOnderwerp = view.findViewById(R.id.TextViewOnderwerp);
        }

    }

}
