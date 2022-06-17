package com.example.civiladvocacyapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private Picasso picasso;
    private ScrollView sw;
    private TextView officiallocation;
    private TextView officedata;
    private TextView namedata;
    private TextView partydata;
    private String party;
    private TextView addresdata;
    private TextView addressheading;
    private TextView phonedata;
    private TextView phoneheading;
    private TextView emaildata;
    private TextView emailheading;
    private TextView websitedata;
    private TextView websiteheading;

    private ImageView officialphoto;
    private ImageView partyphoto;

    private ImageView fbicon;
    private String facebook;
    private ImageView twiticon;
    private String twitter;
    private ImageView yticon;
    private String youtube;

    private Official officialforactivity;
    int red = Color.parseColor("#FF0000");
    int blue = Color.parseColor("#0000FF");
    int black = Color.parseColor("#000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        Intent intent = getIntent();
        if (intent.hasExtra(Official.class.getName())) {
            final Official official = (Official) intent.getSerializableExtra(Official.class.getName());
            officialforactivity=official;
            if (official == null)
                return;
            sw = findViewById(R.id.sw);
            officedata = findViewById(R.id.office_data);
            officiallocation = findViewById(R.id.location_official);
            namedata = findViewById(R.id.name_data);
            partydata = findViewById(R.id.party_data);
            addresdata = findViewById(R.id.address_data);
            addressheading = findViewById(R.id.address);
            phonedata = findViewById(R.id.phone_data);
            phoneheading = findViewById(R.id.phone);
            emaildata = findViewById(R.id.email_data);
            emailheading = findViewById(R.id.email);
            websitedata = findViewById(R.id.website_data);
            websiteheading = findViewById(R.id.website);
            officialphoto = findViewById(R.id.official_photo);
            partyphoto = findViewById(R.id.party_photo);
            fbicon = findViewById(R.id.fb_icon);
            twiticon = findViewById(R.id.twit_icon);
            yticon = findViewById(R.id.yt_icon);

            officiallocation.setText(MainActivity.locationtodisplay);
            officedata.setText(official.getOffice());
            namedata.setText(official.getName());
            party = official.getParty();
            partydata.setText("("+official.getParty()+")");
            if (official.getAddress() == null) {
                addresdata.setVisibility(View.GONE);
                addressheading.setVisibility(View.GONE);
            } else {

                addresdata.setText(official.getAddress());
                SpannableString content = new SpannableString(addresdata.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                addresdata.setText(content);
            }
            if (official.getNum() == null) {
                phonedata.setVisibility(View.GONE);
                phoneheading.setVisibility(View.GONE);
            } else {
                phonedata.setText(official.getNum());
                Linkify.addLinks(phonedata, Linkify.ALL);
            }
            if (official.getEmail() == null) {
                emaildata.setVisibility(View.GONE);
                emailheading.setVisibility(View.GONE);
            } else {
                emaildata.setText(official.getEmail());
                Linkify.addLinks(emaildata, Linkify.ALL);
            }
            if (official.getWebsite() == null) {
                websitedata.setVisibility(View.GONE);
                websiteheading.setVisibility(View.GONE);
            } else {
                websitedata.setText(official.getWebsite());
                Linkify.addLinks(websitedata, Linkify.ALL);
            }
            if(this.hasNetworkConnection()) {
                loadRemoteImage(official.getI_url(), officialphoto);
            }else{
                officialphoto.setImageResource(R.drawable.brokenimage);
            }
            if (official.getParty().equals("Republican Party")) {
                partyphoto.setImageResource(R.drawable.rep_logo);
                sw.setBackgroundColor(red);
            } else if (official.getParty().equals("Democratic Party")) {
                partyphoto.setImageResource(R.drawable.dem_logo);
                sw.setBackgroundColor(blue);
            } else {
                partyphoto.setVisibility(View.GONE);
                sw.setBackgroundColor(black);
            }
            if (official.getFacebook() == null) {
                fbicon.setVisibility(View.GONE);
            } else {
                facebook = official.getFacebook();
            }
            if (official.getTwitter() == null) {
                twiticon.setVisibility(View.GONE);
            } else {
                twitter = official.getTwitter();
            }
            if (official.getYoutube() == null) {
                yticon.setVisibility(View.GONE);
            } else {
                youtube = official.getYoutube();
            }
        }
    }
    public void clickMap(View v) {
        String address = addresdata.getText().toString();

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        // Check if there is an app that can handle geo intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Application found that handles ACTION_VIEW (geo) intents", Toast.LENGTH_SHORT ).show();
        }
    }
    public void partyIcon(View v) {
        Intent intent;
        if(party.equals("Republican Party")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com"));
            startActivity(intent);
        }
        else if(party.equals("Democratic Party")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org/"));
            startActivity(intent);
        }
    }
    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + facebook;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + facebook;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
    public void twitterClicked(View v) {
        Intent intent = null;
        String name = twitter;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }
    public void youtubeClicked(View v) {
        String name = youtube;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    private void loadRemoteImage(String imageURL, ImageView image) {

        if (imageURL == null) {
            Picasso.get().load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.missing)
                    .into(image);
        } else {
            Picasso.get().load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }
    }
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    public void openPhotoAct(View view){
        if(officialforactivity.getI_url() != null){
            Intent intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra(Official.class.getName(), officialforactivity);
            startActivity(intent);
        }
    }
}
