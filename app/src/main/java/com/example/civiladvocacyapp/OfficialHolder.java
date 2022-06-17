package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialHolder extends RecyclerView.ViewHolder{
    TextView officePost;
    TextView nameParty;
    public OfficialHolder(@NonNull View itemView) {
        super(itemView);
        officePost = itemView.findViewById(R.id.officePost);
        nameParty = itemView.findViewById(R.id.nameParty);
    }
}
