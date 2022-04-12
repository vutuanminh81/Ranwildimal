package com.example.ranwildimal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyEncounterAdapter extends RecyclerView.Adapter<MyEncounterAdapter.MyViewHolder> {
    private ArrayList<Word> wordJapList;
    private ArrayList<Word> wordsearchList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWordName, txtJapName;
        ImageView imgAvatar;
        MyViewHolder(View view) {
            super(view);
            txtWordName = view.findViewById(R.id.txt_normal_name);
            txtJapName = view.findViewById(R.id.txt_japanese_name);
            imgAvatar = view.findViewById(R.id.img_encounter_animal);
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
            String img_id = word.getWord_Des_Id() + ".jpg";
            Bitmap bitmap = null;
            AssetManager assetMan =this.context.getAssets();
            try {
                InputStream is = assetMan.open("avt/"+img_id);
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.imgAvatar.setImageBitmap(bitmap);
            holder.txtWordName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DescriptionActivity.class);
                    DatabaseAccess dbAccess = DatabaseAccess.getInstance(context);
                    dbAccess.openConn();
                    dbAccess.increaseWordSearch(String.valueOf(word.getWord_Des_Id()));
                    intent.putExtra("GETID",wordsearchList.get(holder.getAdapterPosition()).getWord_Des_Id());
                    intent.putExtra("IntentFrom","MyEncounter");
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
