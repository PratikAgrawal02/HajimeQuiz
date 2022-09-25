package com.hajime.hajimequiz;

import static com.hajime.hajimequiz.MainActivity.cclicked;
import static com.hajime.hajimequiz.SplashScreen.allquestions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class qna extends AppCompatActivity {
    Button option1,option2,option3,option4,submit;
    CountDownTimer countDownTimer;
    Vibrator vibrator;
    MediaPlayer clocksound;
    ProgressBar progressBar;
    TextView texttimer,question,questionno_text,correctscore,wrongscore;
    ImageView eggimage ;
    List<ModelClass>allques;
    ModelClass current_question;
    Boolean markedQuestion=false;
    ArrayList<ModelClass> eachqlist;
    DatabaseReference questions_firebase;
    int question_index = 0;
    public static int correctans=0,no_of_question= 5;

    final String[] finder = new String[6];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        eachqlist= new ArrayList<>();
        correctans=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        hook();
        setProgressDialog();
//        ProgressDialog pd = new ProgressDialog(qna.this);
//        pd.setMessage("loading");
//        pd.setCancelable(false);
//        pd.show();
//
//        Handler hdl= new Handler();
//        hdl.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pd.cancel();
//            }
//        },4500);

        //Toast.makeText(this, ""+cclicked, Toast.LENGTH_SHORT).show();
         getfirebasedata();





    }

    public void gotdata(){
        allques= eachqlist;
        questionno_text.setText(""+String.valueOf(question_index+1)+" "+"/ "+String.valueOf(no_of_question));
        Collections.shuffle(allques);
        startQuestion();
    }
    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, 60, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("   Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        Handler hdl= new Handler();
        hdl.postDelayed(new Runnable() {
            @Override
            public void run() {
               dialog.cancel();
            }
        },4500);
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    private void getfirebasedata() {
        questions_firebase.child("totalQ").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int foundQ=task.getResult().getValue(Integer.class);
                for(int i=1;i<=foundQ;i++){
                    questions_firebase.child(String.valueOf(i)).child("q").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[0] =task1.getResult().getValue(String.class);
                        }
                    });
                    questions_firebase.child(String.valueOf(i)).child("o1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[1] =task1.getResult().getValue(String.class);
                           // Toast.makeText(qna.this, ""+finder[1], Toast.LENGTH_SHORT).show();
                        }
                    });
                    questions_firebase.child(String.valueOf(i)).child("o2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[2] =task1.getResult().getValue(String.class);
                        }
                    });
                    questions_firebase.child(String.valueOf(i)).child("o3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[3] =task1.getResult().getValue(String.class);
                        }
                    });
                    questions_firebase.child(String.valueOf(i)).child("o4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[4] =task1.getResult().getValue(String.class);
                        }
                    });
                    questions_firebase.child(String.valueOf(i)).child("ans").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            finder[5] =task1.getResult().getValue(String.class);
                            eachqlist.add(new ModelClass(finder[0]+"",finder[1]+"",""+finder[2],""+finder[3],""+finder[4],""+finder[5]));
                        }
                    });


                   // if(i>0)Toast.makeText(qna.this, ""+finder[0]+finder[1]+""+finder[2]+finder[3]+finder[4]+finder[5], Toast.LENGTH_SHORT).show();


                }
                Handler h= new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        gotdata();

                    }
                },4000);



            }
        });
    }

    public void startQuestion(){
        clocksound.start();
        markedQuestion=false;
        submit.setAlpha(0F);


        current_question=allques.get(question_index);
        question.setText(current_question.getQuestion());
        option1.setText(current_question.getOption1());
        option2.setText(current_question.getOption2());
        option3.setText(current_question.getOption3());
        option4.setText(current_question.getOption4());


        progressBar.setMax(10);
        eggimage.setImageResource(R.drawable.ico_egg);
        progressBar.setMin(0);
        progressBar.setProgress(10);
        countDownTimer=new CountDownTimer(10000,1) {  //MADARCHOD isko 10 kr lena
            @Override
            public void onTick(long l) {
                //Toast.makeText(qna.this, "time case"+String.valueOf(l), Toast.LENGTH_LONG).show();
                int sec=(int) TimeUnit.MILLISECONDS.toSeconds(l);
                int mili= (int) (l-(sec*1000))/10;
                if(mili<10)texttimer.setText(""+sec+":0"+mili);
                else texttimer.setText(""+sec+" : "+mili);
                progressBar.setProgress(sec+1);



            }

            @Override
            public void onFinish() {
                markedQuestion=true;
                clocksound.pause();
                Animation shake;
                submit.setAlpha(1F);
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
                progressBar.setProgress(0);
                texttimer.setText("OVER");
                markcorrect();

                eggimage.setImageResource(R.drawable.omelete);
                eggimage.startAnimation(shake);

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(qna.this);
                View timeoutdialoge = getLayoutInflater().inflate(R.layout.timeout_dialoge,null);
                mbuilder.setView(timeoutdialoge);
                AlertDialog timeout = mbuilder.create();
                //timeout.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

                Bitmap map=takeScreenShot(qna.this);
                Bitmap fast=fastblur(map, 10);
                final Drawable draw=new BitmapDrawable(getResources(),fast);
                timeout.getWindow().setBackgroundDrawable(draw);
                timeout.show();
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

                //timeout.getWindow().setGravity(1);

                TextView tryagain= (TextView) timeout.findViewById(R.id.trybutton);
                if(question_index==no_of_question)tryagain.setText("Result");

                tryagain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeout.cancel();
                        correctscore.setText(""+String.valueOf(correctans));
                        wrongscore.setText(""+String.valueOf(question_index-correctans));
//                        if(question_index==no_of_question){
//                            result();
//                        }
//                        else{
//
//                            questionno_text.setText(""+String.valueOf(question_index+1)+" "+"/ "+String.valueOf(no_of_question));
//                            donarmal();
//                            startQuestion();
//
//                        }
                    }
                });




            }
        }.start();
        question_index++;
        if(question_index==no_of_question)submit.setText("Finish");
    }

    public void close(View view){
        clocksound.stop();
        Intent i= new Intent(qna.this, MainActivity.class);
        startActivity(i);



    }

    public void nextQuestion(View view){

       if(question_index==no_of_question){
           result();
       }
       else{
           questionno_text.setText(""+String.valueOf(question_index+1)+" "+"/ "+String.valueOf(no_of_question));
           donarmal();
           startQuestion();
       }

    }
    public void markcorrect(){
        if(option1.getText().toString().matches(current_question.getAnswer()))
            option1.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
        else if(option2.getText().toString().matches(current_question.getAnswer()))
            option2.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
        else if(option3.getText().toString().matches(current_question.getAnswer()))
            option3.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
        else if(option4.getText().toString().matches(current_question.getAnswer()))
            option4.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
    }



    public void checkanswer(View view){
        if(!markedQuestion) {
            countDownTimer.cancel();
            submit.setAlpha(1F);
            Button btn = (Button) view;
            String colour;

           // Toast.makeText(this, ""+btn.getText().toString()+" "+current_question.getAnswer().toString(), Toast.LENGTH_SHORT).show();

            if (btn.getText().toString().matches(current_question.getAnswer()) && !markedQuestion) {//correctans
               // btn.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
                correctans++;
            }
            else {
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.red));
            }
            markedQuestion = true;
            correctscore.setText(""+String.valueOf(correctans));
            wrongscore.setText(""+String.valueOf(question_index-correctans));
            clocksound.pause();
            markcorrect();
        }

    }

    private void result() {
        Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        Intent i= new Intent(qna.this, Result.class);
        startActivity(i);
    }


    public void hook(){

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        texttimer = (TextView)findViewById(R.id.texttimer);
        eggimage= (ImageView)findViewById(R.id.eggimage);
        vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        clocksound=MediaPlayer.create(this,R.raw.clocktick);
        clocksound.setVolume(40,40);
        clocksound.setLooping(true);

        question= (TextView) findViewById(R.id.question);
        option1 = (Button) findViewById(R.id.option1);
        option2= (Button) findViewById(R.id.option2);
        option3= (Button) findViewById(R.id.option3);
        option4= (Button) findViewById(R.id.option4);

        submit= (Button) findViewById(R.id.submit);
        correctscore= (TextView)findViewById(R.id.correctscore);
        wrongscore= (TextView) findViewById(R.id.wrongscore);
        
        questionno_text= (TextView) findViewById(R.id.question_no);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        questions_firebase= database.getReference("Question/"+cclicked);
        
        

    }
    private void donarmal() {
        option1.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.orange));
        option2.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.orange));
        option3.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.orange));
        option4.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.orange));


    }
    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Log.i("height..width",""+String.valueOf(width)+" "+String.valueOf(height));

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }
    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}

