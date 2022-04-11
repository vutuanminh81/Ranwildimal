package com.example.ranwildimal.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ranwildimal.R;
import com.example.ranwildimal.model.GuidelineItem;

import java.util.List;

public class GuidelineAdapter extends RecyclerView.Adapter<GuidelineAdapter.GuidelineViewHolder>{

    private List<GuidelineItem> guidelineItemList;

    public GuidelineAdapter (List<GuidelineItem> guidelineItemList) {
        this.guidelineItemList = guidelineItemList;
    }


    @NonNull
    @Override
    public GuidelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GuidelineViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_guideline, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GuidelineViewHolder holder, int position) {
        holder.setGuidelineData(guidelineItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return guidelineItemList.size();
    }

    class GuidelineViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView descriptionImg;
        private TextView number;

        public GuidelineViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.guidelineTitle);
            descriptionImg = itemView.findViewById(R.id.guidelineImg);
            number = itemView.findViewById(R.id.txtGLnumber);

        }

        void setGuidelineData (GuidelineItem guidelineItem) {
            title.setText(guidelineItem.getTitle());
            descriptionImg.setImageResource(guidelineItem.getDescriptionImg());
            number.setText(guidelineItem.getNumber());
        }
    }

}
