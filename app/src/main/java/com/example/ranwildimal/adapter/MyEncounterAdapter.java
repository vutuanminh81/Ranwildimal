package com.example.ranwildimal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranwildimal.DescriptionActivity;
import com.example.ranwildimal.R;
import com.example.ranwildimal.model.Word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyEncounterAdapter extends RecyclerView.Adapter<MyEncounterAdapter.MyViewHolder> {
    private ArrayList<Word> wordJapList;
    private ArrayList<Word> wordsearchList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWordName, txtJapName;
        MyViewHolder(View view) {
            super(view);
            txtWordName = view.findViewById(R.id.txt_normal_name);
            txtJapName = view.findViewById(R.id.txt_japanese_name);
        }
    }
    public MyEncounterAdapter(ArrayList<Word> wordsearchList, Context context) {
        this.wordsearchList = wordsearchList;
        this.context= context;
    }
    @NonNull
    @Override
    public MyEncounterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_encounter_list, parent, false);
        return new MyEncounterAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyEncounterAdapter.MyViewHolder holder, int position) {
        if(!wordsearchList.isEmpty()){
            Word word = wordsearchList.get(position);
            Word wordall = wordJapList.get(position);
            holder.txtWordName.setText(word.getWord());
            holder.txtJapName.setText(wordall.getWord());
            holder.txtWordName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DescriptionActivity.class);
                    intent.putExtra("GETID",wordsearchList.get(holder.getAdapterPosition()).getWord_ID());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        }else{

        }
    }
    @Override
    public int getItemCount() {
        return wordsearchList.size();
    }

    public void ArrayFilter(ArrayList<Word> newlist, ArrayList<Word> alllist){
        this.wordsearchList = newlist;
        wordJapList = alllist;
        notifyDataSetChanged();
    }
}
