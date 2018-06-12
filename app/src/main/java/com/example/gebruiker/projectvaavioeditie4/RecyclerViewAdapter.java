package com.example.gebruiker.projectvaavioeditie4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mJobTitles = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mLocaties = new ArrayList<>();
    private ArrayList<String> mOmschrijvingen = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mJobTitles, ArrayList<String> mImages, ArrayList<String> mLocaties, ArrayList<String> mOmschrijvingen) {
        this.mJobTitles = mJobTitles;
        this.mImages = mImages;
        this.mLocaties = mLocaties;
        this.mOmschrijvingen = mOmschrijvingen;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.job_title.setText(mJobTitles.get(position));
        holder.locatie.setText(mLocaties.get(position));
        holder.omschrijving.setText(mOmschrijvingen.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent omschrijving = new Intent(mContext, Activity_VacOmschrijving.class);
                mContext.startActivity(omschrijving);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView job_title;
        TextView locatie;
        TextView omschrijving;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            job_title = itemView.findViewById(R.id.job_title);
            locatie = itemView.findViewById(R.id.locatie);
            omschrijving = itemView.findViewById(R.id.omschrijving);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
