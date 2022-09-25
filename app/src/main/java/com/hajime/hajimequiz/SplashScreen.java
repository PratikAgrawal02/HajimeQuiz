package com.hajime.hajimequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DatabaseReference questions_list;
    ImageView loading,intro;
    Handler h= new Handler();
    public static ArrayList<ModelClass>allquestions;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        hook();

        loading.animate().rotation(3600).setDuration(5000);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                intro.setAlpha(0F);
                intro.setImageResource(R.drawable.intro2);
                intro.animate().alpha(1F).translationXBy(100F).setDuration(500);

            }
        },1500);
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                if("mrx".matches(sharedPreferences.getString("uniqueid","mrx"))){
                 Intent i = new Intent(SplashScreen.this, Profile.class);
                 startActivity(i);
                 finish();
                }
                else{
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
         allquestions= new ArrayList<>();



        allquestions.add(new ModelClass("What is Your Name","Pratik","Pratik","Hajime","Dont know","Pratik"));
        allquestions.add(new ModelClass("What is Your age","1","2","4","19","19"));
        allquestions.add(new ModelClass("What is Your Name3","Pratik","Pratik","Hajime","Dont know","Pratik"));






    }

    private void hook() {
        sharedPreferences= getSharedPreferences("Profile",MODE_PRIVATE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        questions_list= database.getReference("Question");
      loading = (ImageView) findViewById(R.id.loading);
        intro = (ImageView) findViewById(R.id.intro);
    }


}
