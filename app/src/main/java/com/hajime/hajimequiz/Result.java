package com.hajime.hajimequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static com.hajime.hajimequiz.qna.correctans;
import static com.hajime.hajimequiz.qna.no_of_question;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class Result extends AppCompatActivity {
    TextView currentscore,totalscore,winner1,winner2,winner3;
    CircularProgressBar progressBar;
    String w1,w2,w3;
    int score,totalq,winner1score=0,winner2score=0,winner3score=0;
    DatabaseReference firebase_profile,firebase_winner;
    SharedPreferences profile;
    final int[] currbal = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        hook();
        score=correctans;
        totalq=no_of_question;
        progressBar.setProgressMax((float) no_of_question);
        currentscore.setText(""+String.valueOf(score)+" / "+String.valueOf(no_of_question));
        progressBar.setProgressWithAnimation((float) score, 2000L);
        firebase_profile.child(profile.getString("uniqueid","Pratik")).child("balance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                currbal[0] =score+task.getResult().getValue(Integer.class);
               // Toast.makeText(Result.this, ""+String.valueOf(profile.getInt("balance",0)), Toast.LENGTH_SHORT).show();
                firebase_profile.child(profile.getString("uniqueid","Pratik")).child("balance").setValue(currbal[0]);
                profile.edit().putInt("balance",currbal[0]).apply();
                totalscore.setText("Your Final Score of all time is "+String.valueOf(currbal[0])+" Points.");
                getwinnerdata();
            }
        });





    }
    public void getwinnerdata(){
        firebase_winner.child("name1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                w1=task.getResult().getValue(String.class);
            }
        });
         firebase_winner.child("name2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 w2=task.getResult().getValue(String.class);
             }
         });
         firebase_winner.child("name3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 w3=task.getResult().getValue(String.class);
             }
         });
         firebase_winner.child("1st").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                winner1score=task.getResult().getValue(Integer.class);
             }
         });
         firebase_winner.child("2nd").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 winner2score=task.getResult().getValue(Integer.class);
             }
         });
         firebase_winner.child("3rd").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 winner3score=task.getResult().getValue(Integer.class);
             }
         });
        Handler h= new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkdata();
            }
        },5000);

     }



     public void checkdata(){
        if(winner1score<currbal[0] && !w1.matches(profile.getString("uniqueid","anonymous"))){
            firebase_winner.child("name1").setValue(profile.getString("uniqueid","anonymous"));
            firebase_winner.child("1st").setValue(currbal[0]);
            firebase_winner.child("name2").setValue(w1);
            firebase_winner.child("2nd").setValue(winner1score);
            winner1.setText(profile.getString("uniqueid","anonymous")+"("+String.valueOf(currbal[0])+" pnts)");
            winner2.setText(w1+"("+String.valueOf(winner1score)+" pnts)");

            if(!w2.matches(profile.getString("uniqueid","anonymous"))){
               // Toast.makeText(this, "yeag"+w2+" "+profile.getString("uniqueid","anonymous"), Toast.LENGTH_SHORT).show();
            firebase_winner.child("name3").setValue(w2);
            winner3.setText(w2+"("+String.valueOf(winner2score)+" pnts)");
            firebase_winner.child("3rd").setValue(winner2score);
            }
            else {
                winner3.setText(w3+"("+String.valueOf(winner3score)+" pnts)");
            }


        }
        else if(winner2score<currbal[0]&& !w1.matches(profile.getString("uniqueid","anonymous"))&& !w2.matches(profile.getString("uniqueid","anonymous"))){
            firebase_winner.child("name2").setValue(profile.getString("uniqueid","anonymous"));
            firebase_winner.child("2nd").setValue(currbal[0]);
            firebase_winner.child("name3").setValue(w2);
            firebase_winner.child("3rd").setValue(winner2score);
            winner1.setText(w1+"("+String.valueOf(winner1score)+" pnts)");
            winner2.setText(profile.getString("uniqueid","anonymous")+"("+String.valueOf(currbal[0])+" pnts)");
            winner3.setText(w2+"("+String.valueOf(winner2score)+" pnts)");
        }
        else if(winner3score<currbal[0]&& !w1.matches(profile.getString("uniqueid","anonymous"))&& !w2.matches(profile.getString("uniqueid","anonymous"))){
            firebase_winner.child("name3").setValue(profile.getString("uniqueid","anonymous"));
            firebase_winner.child("3rd").setValue(currbal[0]);
            winner1.setText(w1+"("+String.valueOf(winner1score)+" pnts)");
            winner2.setText(w2+"("+String.valueOf(winner2score)+" pnts)");
            winner3.setText(profile.getString("uniqueid","anonymous")+"("+String.valueOf(currbal[0])+" pnts)");
        }
        else{
            winner1.setText(w1+"("+String.valueOf(winner1score)+" pnts)");
            winner2.setText(w2+"("+String.valueOf(winner2score)+" pnts)");
            winner3.setText(w3+"("+String.valueOf(winner3score)+" pnts)");
            if(w1.matches(profile.getString("uniqueid","anonymous"))) firebase_winner.child("1st").setValue(currbal[0]);
            if(w2.matches(profile.getString("uniqueid","anonymous")))firebase_winner.child("2nd").setValue(currbal[0]);
        }

     }


    public void close(View view){
        Intent i= new Intent(Result.this, MainActivity.class);
        startActivity(i);
    }
    public void hook() {
        currentscore= (TextView) findViewById(R.id.current_score);
        totalscore=(TextView) findViewById(R.id.totalscore);
        winner1=(TextView) findViewById(R.id.winner1);
        winner2=(TextView) findViewById(R.id.winner2);
        winner3= (TextView) findViewById(R.id.winner3);
        progressBar= (CircularProgressBar) findViewById(R.id.circularProgressBar);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase database1= FirebaseDatabase.getInstance();
        firebase_profile= database.getReference("Profile");
        firebase_winner= database1.getReference("Winner");
        profile= getSharedPreferences("Profile",MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        close(winner1);
        super.onBackPressed();
    }
}