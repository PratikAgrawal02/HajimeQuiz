package com.hajime.hajimequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Contact_us extends AppCompatActivity {
    EditText name,email,body,subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        name= (EditText) findViewById(R.id.PersonName);
        email= (EditText) findViewById(R.id.EmailAddress);
        body= (EditText) findViewById(R.id.Bodymail);
        subject=(EditText) findViewById(R.id.subject);
    }
    public void sendmail(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String initial = "Hello , My name is "+name.getText().toString()+" and my email address for communication is "+email.getText().toString()+" . I have some problem regarding app : ";
        intent.setData(Uri.parse("mailto:pratik.agra2002@gmail.com")); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT,initial+body.getText().toString());
        startActivity(intent);

    }
    public void close(View view){
        Intent i = new Intent(Contact_us.this, MainActivity.class);
        startActivity(i);
    }
}