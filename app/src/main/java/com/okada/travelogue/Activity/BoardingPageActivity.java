package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.okada.travelogue.Adapters.SlideAdapter;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class BoardingPageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private SlideAdapter slideAdapter;
    private Button boardingPageStartedBtn,boardingPageNextBtn,boardingPageSkipBtn;
    private Animation animation;
    private int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_page);
        findingViews();
        checkLanguage();
        slideAdapter=new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);
        createDots(0);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void checkLanguage() {
        Resources resources = LanguageResourceClass.getResource(this);
        boardingPageNextBtn.setText(resources.getString(R.string.next));
        boardingPageSkipBtn.setText(resources.getString(R.string.skip));
    }


    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            createDots(position);
            currentPosition=position;
            if (position==3){
                animation= AnimationUtils.loadAnimation(BoardingPageActivity.this,R.anim.boarding_anim_right);
                boardingPageStartedBtn.setAnimation(animation);
                boardingPageStartedBtn.setVisibility(View.VISIBLE);
                boardingPageNextBtn.setVisibility(View.INVISIBLE);
                boardingPageSkipBtn.setVisibility(View.INVISIBLE);
            }else {
                boardingPageStartedBtn.setVisibility(View.INVISIBLE);
                boardingPageNextBtn.setVisibility(View.VISIBLE);
                boardingPageSkipBtn.setVisibility(View.VISIBLE);

            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void findingViews(){
        viewPager=findViewById(R.id.view_pager);
        dotsLayout =findViewById(R.id.boarding_page_dots_layout);
        boardingPageStartedBtn=findViewById(R.id.boarding_page_started_btn);
        boardingPageNextBtn=findViewById(R.id.boarding_page_next_btn);
        boardingPageSkipBtn=findViewById(R.id.boarding_page_skip_btn);
    }
    public void skipSlide(View view){
        Intent intent=new Intent(BoardingPageActivity.this,HomeActivity.class);
        startActivity(intent);
        Animatoo.animateFade(BoardingPageActivity.this);
        finish();
    }
    public void toNextSlide(View view){
        viewPager.setCurrentItem(currentPosition+1);
    }

    private void createDots(int position) {
        TextView[] dots=new TextView[4];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }
    }
}