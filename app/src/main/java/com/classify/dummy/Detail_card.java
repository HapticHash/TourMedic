package com.classify.dummy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Detail_card extends AppCompatActivity {

    ImageView img;
    TextView place;
    TextView shortdesc;
    TextView longdesc;
    TextView rating;
    RatingBar ratingBar;
    String url,placename,sDesc,lDesc,ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);
        img = (ImageView)findViewById(R.id.image_detail);
        place = (TextView)findViewById(R.id.placeName_detail);
        shortdesc = (TextView)findViewById(R.id.placeShortDesc);
        longdesc = (TextView)findViewById(R.id.placeDetail);
        rating = (TextView)findViewById(R.id.ratingText);
        ratingBar = (RatingBar) findViewById(R.id.ratingstar_detail);

        placename = getIntent().getExtras().getString("placename");
        url = getIntent().getExtras().getString("url");
        sDesc = getIntent().getExtras().getString("cityname");
        lDesc = getIntent().getExtras().getString("desc");
        ratings = getIntent().getExtras().getString("rating");

        Picasso.with(Detail_card.this).load(url).fit().into(img);
        place.setText(placename);
        longdesc.setText(lDesc);
        rating.setText(ratings);

    }
}
