package com.classify.tourmedic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class City extends AppCompatActivity {

    ImageButton things;
    ImageButton food;
    ImageButton day_paln;
    ImageButton help;
    String placename;
    Toolbar mActionbar;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        placename = getIntent().getExtras().getString("place");

        things = (ImageButton)findViewById(R.id.imageButton1);
        food = (ImageButton)findViewById(R.id.imageButton2);
        day_paln = (ImageButton)findViewById(R.id.imageButton3);
        help = (ImageButton)findViewById(R.id.imageButton4);

        mActionbar = (Toolbar)findViewById(R.id.toolbar_city);
        setSupportActionBar(mActionbar);
        getSupportActionBar().setTitle(placename);

        things.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(City.this,MainActivity.class);
                i.putExtra("cityname",placename);
                startActivity(i);
            }
        });

        day_paln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(City.this,Day_plan.class);
                i.putExtra("cityname",placename);
                startActivity(i);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(City.this,help.class);
                i.putExtra("cityname",placename);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(City.this,Create_trip_form.class);
                i.putExtra("cityname",placename);
                startActivity(i);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
