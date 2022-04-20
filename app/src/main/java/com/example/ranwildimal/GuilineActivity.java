package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
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
        if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("en")){
            setLocale("en");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("vi")){
            setLocale("vi");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("ja")){
            setLocale("ja");
        }
        setContentView(R.layout.activity_guiline);
        statusBarColor();


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

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }
    public void Back(View view){
        this.finish();
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preference
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
        editor.commit();
    }
}