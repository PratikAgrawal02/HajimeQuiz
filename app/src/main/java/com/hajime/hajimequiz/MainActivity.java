package com.hajime.hajimequiz;
//TYPES: logo quiz , trivia, celebrity, coding
//theme  https://codecanyon.net/item/elite-quiz-the-flutter-quiz-app/33570423#
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    ImageView avatar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences profile;
    public static String cclicked;
    String cards[]={"trivia","logoguess","celibro","programo"};
    CardView triviaveiw,logoveiw,celibroveiw,programoveiw,cardView,headercard;
    Drawable d;
    TextView email, name,balance;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        profile= getSharedPreferences("Profile",MODE_PRIVATE);
        triviaveiw= (CardView)findViewById(R.id.triviaveiw);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView= (NavigationView)findViewById(R.id.navigationlayout);
        logoveiw=(CardView)findViewById(R.id.logoguessveiw);
        celibroveiw=(CardView)findViewById(R.id.celibroguessveiw);
        programoveiw=(CardView)findViewById(R.id.programoveiw);
        cardView= (CardView)findViewById(R.id.cardveiw1);
        avatar= (ImageView)findViewById(R.id.avatar);
        headercard=(CardView)findViewById(R.id.headercard);
        animatecard();

        d= ContextCompat.getDrawable(this, R.drawable.profile6);

        avatar.setImageBitmap(decodeBase64(profile.getString("avatar",
                ""+encodeTobase64(((BitmapDrawable)d).getBitmap()))));
         name= (TextView) findViewById(R.id.username);
        email= (TextView) findViewById(R.id.email);
        balance= (TextView)findViewById(R.id.balance);
        name.setText(profile.getString("username","User"));
        String s= profile.getString("email","email@gmail.com");
        email.setText(s.substring(0, s.indexOf('@')));
        balance.setText(String.valueOf(profile.getInt("balance",0)));


        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.rate:{
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getResources().getString(R.string.packagename))));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getResources().getString(R.string.packagename))));
                        }


                    }
                    case R.id.Home_menu: {

                        break;

                    }
                    case R.id.aboutus_menu: {
                        Intent intent = new Intent(MainActivity.this,Contact_us.class);
                        startActivity(intent);
                       // Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.developer_menu: {
                        Intent intent = new Intent(MainActivity.this,developer_profile.class);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.privacy_policy_menu: {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://privacypolicypratik.blogspot.com/2022/06/privacy-policy-hajquiz.html")));
                        //Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.profile_menu: {
                        profilepage(avatar);

                        break;
                    }
                    case R.id.share_app_menu: {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, ""+getString(R.string.app_name));
                            String shareMessage= "\nHey there\nHave you ever tried this app "+getString(R.string.app_name)+" . Let me recommend you this cool quiz application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getResources().getString(R.string.packagename) +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "Choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        //Toast.makeText(MainActivity.this, "6", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.TnC_menu: {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://privacypolicypratik.blogspot.com/2022/06/terms-and-conditions-hajquiz.html")));


                        //Toast.makeText(MainActivity.this, "7", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    default: return true;

                }

                return true;
            }
        });




    }
    public void navigate(View view){
        drawerLayout.openDrawer(GravityCompat.START);
       // Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
    }

    public void cardclicked(View view){
        //Toast.makeText(this, "The logo clicked be you is "+view.toString()+"", Toast.LENGTH_SHORT).show();
        CardView cc = (CardView)view;
        cclicked= cards[Integer.parseInt(cc.getTag().toString())-1];
        if(Objects.equals(cclicked, "logoguess")){
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(MainActivity.this, qna.class);
            startActivity(i);
        }

    }

    public void profilepage(View view){
        Toast.makeText(this, "Profile Page", Toast.LENGTH_SHORT).show();
        Intent i= new Intent(MainActivity.this, Profile.class);
        startActivity(i);
    }



    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
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



    public void animatecard(){
       // programoveiw.animate().scaleX(0.94F).scaleY(0.94F).setDuration(100);
        cardView.setTranslationY(1500);
        headercard.setAlpha(0);
        celibroveiw.setTranslationY(-800);
        programoveiw.setTranslationY(-800);
        triviaveiw.setTranslationX(-1000);
        logoveiw.setTranslationX(1000);
        cardView.animate().translationYBy(-1500).setDuration(600);
        headercard.animate().alpha(1F).setDuration(1600);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                triviaveiw.animate().translationXBy(1000).setDuration(500);
                logoveiw.animate().translationXBy(-1000).setDuration(500);
            }
        },600);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                celibroveiw.animate().translationYBy(800).setDuration(500);
                programoveiw.animate().translationYBy(800).setDuration(500);
            }
        },1100);


    }
    @Override
    protected void onStart(){
        avatar.setImageBitmap(decodeBase64(profile.getString("avatar",
                ""+encodeTobase64(((BitmapDrawable)d).getBitmap()))));
        name.setText(profile.getString("username","User"));
        String s= profile.getString("email","email@gmail.com");
        email.setText(s.substring(0, s.indexOf('@')));
        super.onStart();
    }
}
