package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ranwildimal.adapter.GuidelineAdapter;
import com.example.ranwildimal.model.GuidelineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuilineActivity extends AppCompatActivity {

    private GuidelineAdapter guidelineAdapter;

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
            itemInHome.setNumber("1/3");

            itemInSideBar.setTitle("サイドバー");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar_jp);
            itemInSideBar.setNumber("2/3");

            itemInformation.setTitle("動物の情報画面");
            itemInformation.setDescriptionImg(R.drawable.guideline_information_jp);
            itemInformation.setNumber("3/3");
        }else if(locale.toString().equals("vi")){
            itemInHome.setTitle("TRANG CHỦ VÀ TRANG CHỤP HÌNH");
            itemInHome.setDescriptionImg(R.drawable.guideline_home_vn);
            itemInHome.setNumber("1/3");

            itemInSideBar.setTitle("THANH CÔNG CỤ");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar_vn);
            itemInSideBar.setNumber("2/3");

            itemInformation.setTitle("TRANG THÔNG TIN CON VẬT");
            itemInformation.setDescriptionImg(R.drawable.guideline_information_vn);
            itemInformation.setNumber("3/3");
        }else{
            itemInHome.setTitle("IN HOME SCREEN & CAMERA SCREEN");
            itemInHome.setDescriptionImg(R.drawable.guideline_home);
            itemInHome.setNumber("1/3");

            itemInSideBar.setTitle("IN SIDEBAR");
            itemInSideBar.setDescriptionImg(R.drawable.guideline_insidebar);
            itemInSideBar.setNumber("2/3");

            itemInformation.setTitle("IN ANIMAL'S INFORMATION SCREEN");
            itemInformation.setDescriptionImg(R.drawable.guideline_information);
            itemInformation.setNumber("3/3");
        }




        guidelineItemList.add(itemInHome);
        guidelineItemList.add(itemInSideBar);
        guidelineItemList.add(itemInformation);

        guidelineAdapter = new GuidelineAdapter(guidelineItemList);

    }

    public void Back(View view){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
        this.finish();
    }
}