package com.hajime.hajimequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class developer_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_profile);
    }
    public void contact(View view){
        int tag =  Integer.parseInt(view.getTag().toString());
        String address="",defaultaddress="mailto:pratik.agra2002@gmail.com";
        if(tag==1)address="https://www.instagram.com/hajime_pratik/";
        else if (tag==2)address= "https://www.linkedin.com/in/pratikhajime/?original_referer=";
        else if (tag==3)address= "https://t.me/hajime_pratik";
        else if(tag==4)address= "mailto:pratik.agra2002@gmail.com";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(address)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(defaultaddress)));
        }
    }
    public void close(View view){
        Intent i = new Intent(developer_profile.this, MainActivity.class);
        startActivity(i);
    }
}