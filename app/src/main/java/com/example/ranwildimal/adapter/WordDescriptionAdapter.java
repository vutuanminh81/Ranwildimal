package com.example.ranwildimal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranwildimal.DescriptionActivity;
import com.example.ranwildimal.R;
import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.Word;

import java.util.ArrayList;

public class WordDescriptionAdapter extends RecyclerView.Adapter<WordDescriptionAdapter.MyViewHolder> {
    private ArrayList<Example> listSelectExample;
    private ArrayList<Example> listJpExample;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSelectedWordExample;
        TextView txtJPWordExample;
        MyViewHolder(View view) {
            super(view);
            txtSelectedWordExample = view.findViewById(R.id.txt_current_exemple);
            txtJPWordExample = view.findViewById(R.id.txt_japanese_exemple);

        }
    }
    public WordDescriptionAdapter(ArrayList<Example> listSelectExample, ArrayList<Example> listJpExample, Context context) {
        this.listSelectExample = listSelectExample;
        this.listJpExample = listJpExample;
        this.context= context;
    }
    @NonNull
    @Override
    public WordDescriptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_description_exemple_list, parent, false);
        return new WordDescriptionAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(WordDescriptionAdapter.MyViewHolder holder, int position) {
        if(!listSelectExample.isEmpty()){
            if(listSelectExample.size() == 1){
                holder.txtSelectedWordExample.setText(listSelectExample.get(0).getExample());
                holder.txtJPWordExample.setText(listJpExample.get(0).getExample());
            }
            else if(listSelectExample.size() > 1){
                holder.txtSelectedWordExample.setText(listSelectExample.get(0).getExample());
                holder.txtJPWordExample.setText(listJpExample.get(0).getExample());
                holder.txtSelectedWordExample.setText(listSelectExample.get(1).getExample());
                holder.txtJPWordExample.setText(listJpExample.get(1).getExample());
            }
        }
    }
    @Override
    public int getItemCount() {
        return listSelectExample.size();
    }
}
