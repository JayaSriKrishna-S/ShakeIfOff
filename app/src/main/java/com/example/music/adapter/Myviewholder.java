package com.example.music.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.MainActivity;
import com.example.music.R;

public class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txt;
    ItemClickListener itemclicklistener;

    public Myviewholder(@NonNull View itemView) {
        super(itemView);

        txt=itemView.findViewById(R.id.text);
        itemView.setOnClickListener(this);

    }
    public void setItemclicklistener(ItemClickListener itemclicklistener){
        this.itemclicklistener=itemclicklistener;
    }

    @Override

    public void onClick(View v) {
        this.itemclicklistener.onItemClick(this.getLayoutPosition());

    }
    }

