package com.example.civiladvocacyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialHolder> {

    private List<Official> oList;
    private  MainActivity mainActivity;

     OfficialAdapter(List<Official> oList, MainActivity mainActivity) {
        this.oList=oList;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public OfficialHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list, parent, false);

        itemView.setOnClickListener(mainActivity);

        return new OfficialHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialHolder holder, int position) {
        Official official = oList.get(position);
        holder.officePost.setText(official.getOffice());
        holder.nameParty.setText(official.getName() + " (" + official.getParty()+ ")");
    }

    @Override
    public int getItemCount() {
        return oList.size();
    }
}
