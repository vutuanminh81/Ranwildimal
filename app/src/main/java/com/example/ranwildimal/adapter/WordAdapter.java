package com.example.ranwildimal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranwildimal.R;
import com.example.ranwildimal.model.Word;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.MyViewHolder> {
    private ArrayList<Word> wordList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWordName;
        TextView txtWordJName;
        ImageView imgFoodImage;
        MyViewHolder(View view) {
            super(view);
            txtWordName = view.findViewById(R.id.txt_normal_name);
            txtWordJName = view.findViewById(R.id.txt_japanese_name);
        }
    }
    public WordAdapter(List<Word> foodsList, Context context) {
        this.wordList = wordList;
        this.context= context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_encounter_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.txtWordName.setText(word.getWord());
        holder.txtWordJName.setText(word.getWord());
    }
    @Override
    public int getItemCount() {
        return wordList.size();
    }
}
