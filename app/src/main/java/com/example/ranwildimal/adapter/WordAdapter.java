package com.example.ranwildimal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranwildimal.DescriptionActivity;
import com.example.ranwildimal.R;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.Word_Description;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.MyViewHolder> {
    private ArrayList<Word> wordList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWordName;
        MyViewHolder(View view) {
            super(view);
            txtWordName = view.findViewById(R.id.etxt_normal_search_history);

        }
    }
    public WordAdapter(ArrayList<Word> wordList, Context context) {
        this.wordList = wordList;
        this.context= context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_history_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!wordList.isEmpty()){
            Word word = wordList.get(position);
            holder.txtWordName.setText(word.getWord());
            holder.txtWordName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseAccess dbAccess = DatabaseAccess.getInstance(context);
                    dbAccess.openConn();
                    dbAccess.increaseWordSearch(String.valueOf(word.getWord_Des_Id()));
                    Intent intent = new Intent(context, DescriptionActivity.class);
                    intent.putExtra("GETID",wordList.get(holder.getAdapterPosition()).getWord_Des_Id());
                    intent.putExtra("IntentFrom","Search");
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        }else{

        }
    }
    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void ArrayFilter(ArrayList<Word> newlist){
        this.wordList = newlist;
        notifyDataSetChanged();
    }
}
