package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.ranwildimal.adapter.GuidelineAdapter;
import com.example.ranwildimal.model.GuidelineItem;

import java.util.ArrayList;
import java.util.List;

public class GuilineActivity extends AppCompatActivity {

    private GuidelineAdapter guidelineAdapter;
    private LinearLayout layoutGuideline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guiline);

//        layoutGuideline = findViewById(R.id.layout)

        setupGuidelineItems();
        ViewPager2 guidelineViewPaper = findViewById(R.id.GuilineViewPaper);
        guidelineViewPaper.setAdapter(guidelineAdapter);
    }

    private void setupGuidelineItems() {
        List<GuidelineItem> guidelineItemList = new ArrayList<>();

        GuidelineItem itemInHome = new GuidelineItem();
        itemInHome.setTitle("IN HOME SCREEN & CAMERA SCREEN");
        itemInHome.setDescriptionImg(R.drawable.guideline_home);

        GuidelineItem itemInSideBar = new GuidelineItem();
        itemInSideBar.setTitle("IN SIDEBAR");
        itemInHome.setDescriptionImg(R.drawable.guideline_home);

        GuidelineItem itemInformation = new GuidelineItem();
        itemInSideBar.setTitle("IN ANIMAL'S INFORMATION SCREEN");
        itemInHome.setDescriptionImg(R.drawable.guideline_home);

        guidelineItemList.add(itemInHome);
        guidelineItemList.add(itemInSideBar);
        guidelineItemList.add(itemInformation);

        guidelineAdapter = new GuidelineAdapter(guidelineItemList);

    }
}