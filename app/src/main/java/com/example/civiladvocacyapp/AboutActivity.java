package com.example.civiladvocacyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class AboutActivity extends AppCompatActivity {

    private TextView underlinedata;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //set underline for the API text
        underlinedata = findViewById(R.id.underlineData);
        SpannableString content = new SpannableString(underlinedata.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        underlinedata.setText(content);
    }
    public void doApiLink(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/civic-information/"));
        startActivity(intent);
    }
}
