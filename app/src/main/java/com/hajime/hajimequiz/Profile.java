package com.hajime.hajimequiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;



public class Profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ImageView profileavatar, profile1,profile2,profile3,profile4,profile5,profile6,profile7
            ,profile8,profile9,profile10,profile11,profile12,profile13,profile14,profile15,selected;
    EditText namefeild, emailfeild;

    DatabaseReference firebase_profile;

    Boolean validmail=true;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeprofilephotos();
        sharedPreferences= getSharedPreferences("Profile",MODE_PRIVATE);
        
        
        namefeild= (EditText)findViewById(R.id.editTextTextPersonName);
        emailfeild=(EditText)findViewById(R.id.editTextTextEmailAddress); 


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebase_profile= database.getReference("Profile");
        namefeild.setText(sharedPreferences.getString("username", ""));
        emailfeild.setText(sharedPreferences.getString("email",""));


       
        resetalphaavatar();
        selected = (ImageView) findViewById(sharedPreferences.getInt("avatarno",2131361810));
        selected.setAlpha(0.5F);
        Drawable d= ContextCompat.getDrawable(this, R.drawable.profile6);
        Drawable drawable= new BitmapDrawable(getResources(), decodeBase64(sharedPreferences.getString("avatar",""+ encodeTobase64(((BitmapDrawable)d).getBitmap()))));
        profileavatar.setBackground(drawable);



        emailfeild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                validmail= (emailfeild.getText().toString().trim()).matches(emailPattern) && editable.length() > 0;
            }
        });

    }
    
    public void savedata(View view){
        String namet=namefeild.getText().toString();
        String emailt=emailfeild.getText().toString();
        if(!namet.isEmpty() && !emailt.isEmpty()) {
            if(validmail)
            {
                if("no".equals(sharedPreferences.getString("uniqueid", "no")))sharedPreferences.edit().putString("uniqueid",""+namet).apply();
                sharedPreferences.edit().putString("username", "" + namet).apply();
                sharedPreferences.edit().putString("email", "" + emailt).apply();
                firebase_profile.child(sharedPreferences.getString("uniqueid","no")).child("username").setValue(""+namet);
                firebase_profile.child(sharedPreferences.getString("uniqueid","no")).child("email").setValue(""+emailt);
                makewallet(sharedPreferences.getString("uniqueid","Pratik"));


                Toast.makeText(this, "Data saved Successfully!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Please fill up both the feilds", Toast.LENGTH_SHORT).show();
            
        }

    }

    public void makewallet(String str){
        firebase_profile.child(""+str).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int val=0;
                if(!snapshot.exists())firebase_profile.child(""+str).child("balance").setValue(0);
                else val=snapshot.getValue(Integer.class);
                sharedPreferences.edit().putInt("balance", val).apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void close(View view){

        if("no".equals(sharedPreferences.getString("uniqueid", "no"))){
            Toast.makeText(this, "Please Enter Your data first to Go into APP", Toast.LENGTH_SHORT).show();
        }
     else {
            Intent i = new Intent(Profile.this, MainActivity.class);
            startActivity(i);
        }


    }


    public void avatarchange(View view) {
        resetalphaavatar();
        view.setAlpha(0.5F);
        Drawable drawable = ((ImageView) view).getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        sharedPreferences.edit().putString("avatar", ""+encodeTobase64(bitmap)).apply();
        sharedPreferences.edit().putInt("avatarno", view.getId()).apply();
        int a= sharedPreferences.getInt("avatarno", 2131361810);

        profileavatar.setBackground(drawable);
    }
    public void resetalphaavatar(){
        profile1.setAlpha(1F);
        profile2.setAlpha(1F);
        profile3.setAlpha(1F);
        profile4.setAlpha(1F);
        profile5.setAlpha(1F);
        profile6.setAlpha(1F);
        profile7.setAlpha(1F);
        profile8.setAlpha(1F);
        profile9.setAlpha(1F);
        profile10.setAlpha(1F);
        profile11.setAlpha(1F);
        profile12.setAlpha(1F);
        profile13.setAlpha(1F);
        profile14.setAlpha(1F);
        profile15.setAlpha(1F);

    }
    public void initializeprofilephotos(){
        profileavatar= (ImageView)findViewById(R.id.user_profile_photo);
        profile1= (ImageView) findViewById(R.id.Profile1);
        profile2= (ImageView) findViewById(R.id.Profile2);
        profile3= (ImageView) findViewById(R.id.Profile3);
        profile4= (ImageView) findViewById(R.id.Profile4);
        profile5= (ImageView) findViewById(R.id.Profile5);
        profile6= (ImageView) findViewById(R.id.Profile6);
       // Toast.makeText(this, ""+Integer.toString(R.id.Profile6), Toast.LENGTH_SHORT).show();
        profile7= (ImageView) findViewById(R.id.Profile7);
        profile8= (ImageView) findViewById(R.id.Profile8);
        profile9= (ImageView) findViewById(R.id.Profile9);
        profile10= (ImageView) findViewById(R.id.Profile10);
        profile11= (ImageView) findViewById(R.id.Profile11);
        profile12= (ImageView) findViewById(R.id.Profile12);
        profile13= (ImageView) findViewById(R.id.Profile13);
        profile14= (ImageView) findViewById(R.id.Profile14);
        profile15= (ImageView) findViewById(R.id.Profile15);
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


}