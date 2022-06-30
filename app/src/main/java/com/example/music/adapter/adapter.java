package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.MainActivity;
import com.example.music.adapter.Myviewholder;
import com.example.music.R;

import java.util.List;


public class adapter extends RecyclerView.Adapter<Myviewholder> {

    Context c;

    List<String> data;

    public adapter(Context c, List<String> data) {
        this.c = c;
        this.data = data;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(c).inflate(R.layout.list_items,parent,false);
        return new Myviewholder(view);
    }
int spos=-1;
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int i) {
        holder.txt.setText(data.get(i));


        holder.setItemclicklistener(new ItemClickListener() {

            @Override
            public void onItemClick(int pos){
                //Toast.makeText(c,""+holder.txt.getText().toString(),Toast.LENGTH_SHORT).show();
                MainActivity.getInstance().takeit(pos);

            }
        }

        );

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}

