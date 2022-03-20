package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.ranwildimal.adapter.GuidelineAdapter;
import com.example.ranwildimal.model.GuidelineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuilineActivity extends AppCompatActivity {

    private GuidelineAdapter guidelineAdapter;
    private LinearLayout layoutGuideline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guiline);



        setupGuidelineItems();
        ViewPager2 guidelineViewPaper = findViewById(R.id.GuilineViewPaper);
        guidelineViewPaper.setAdapter(guidelineAdapter);
    }

    private void setupGuidelineItems() {
        List<GuidelineItem> guidelineItemList = new ArrayList<>();
        GuidelineItem itemInHome = new GuidelineItem();
        GuidelineItem itemInSideBar = new GuidelineItem();
        GuidelineItem itemInformation = new GuidelineItem();
        Locale locale = getResources().getConfiguration().locale;
        if(locale.toString().equals("ja")){
            itemInHome.setTitle("ホーム画面とカメラ画面");
            itemInHome.setDescriptionImg(R.drawable.guideline_home_jp);

            itemInSideBar.setTitle("サイドバー");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar_jp);

            itemInformation.setTitle("動物の情報画面");
            itemInformation.setDescriptionImg(R.drawable.guideline_information_jp);
        }else if(locale.toString().equals("vi")){
            itemInHome.setTitle("TRANG CHỦ VÀ TRANG CHỤP HÌNH");
            itemInHome.setDescriptionImg(R.drawable.guideline_home_vn);

            itemInSideBar.setTitle("THANH CÔNG CỤ");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar_vn);

            itemInformation.setTitle("TRANG THÔNG TIN CON VẬT");
            itemInformation.setDescriptionImg(R.drawable.guideline_information_vn);
        }else{
            itemInHome.setTitle("IN HOME SCREEN & CAMERA SCREEN");
            itemInHome.setDescriptionImg(R.drawable.guideline_home);

            itemInSideBar.setTitle("IN SIDEBAR");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar);

            itemInformation.setTitle("IN ANIMAL'S INFORMATION SCREEN");
            itemInformation.setDescriptionImg(R.drawable.guideline_information);
        }




        guidelineItemList.add(itemInHome);
        guidelineItemList.add(itemInSideBar);
        guidelineItemList.add(itemInformation);

        guidelineAdapter = new GuidelineAdapter(guidelineItemList);

    }
}