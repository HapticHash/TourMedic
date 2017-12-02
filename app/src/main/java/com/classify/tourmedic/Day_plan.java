package com.classify.tourmedic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Day_plan extends AppCompatActivity implements OnMapReadyCallback {


    GoogleMap mGooglemap;
    GoogleApiClient mGoogleApiClient;
    DatabaseReference mDatabaseCity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email,totalplan;
    int totalplan_int;
    int j=0,k=1,l=1,m=0;
    DatabaseReference mDatabaseDayplan;
    List<String> lats = new ArrayList<String>();
    List<String> lons = new ArrayList<String>();
    List<String> url  = new ArrayList<String>();
    List<String> placename = new ArrayList<String>();
    List<String> rating = new ArrayList<String>();
    List<fragment_Class> card = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    int PLACE_PICKER_REQUEST = 1;
    TextView addresstext;
    String cityname,no,no2,lat,lon,imageurl,place,ratings;
    int flag=0;
    int flag1=0;
    Marker marker1;
    Marker marker2;
    Polyline line;
    int total_plan_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_plan);
        addresstext = (TextView)findViewById(R.id.dayplantext);
        cityname = getIntent().getExtras().getString("cityname");
        addresstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(Day_plan.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabs1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Day_plan.this, Planlist.class);
                i.putExtra("no", no2);
                startActivity(i);
            }
        });


        mDatabaseCity = FirebaseDatabase.getInstance().getReference().child("City");
        mDatabaseCity.keepSynced(true);

        mDatabaseDayplan = FirebaseDatabase.getInstance().getReference().child("Dayplan");
        mDatabaseDayplan.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    email = firebaseAuth.getCurrentUser().getEmail();
                    Log.d("email",email);
                }
                getNum1();
            }
        };

        Query query = mDatabaseCity.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    no = (String) postSnapshot.child("no").getValue();
                }
                Log.d("heyyq",no + " ");
                getInfo();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

     public void getNum1()
     {
         Log.d("heyyq","hii");
         Query query = mDatabaseDayplan.orderByChild("email").equalTo(email);
         query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                     no2 = (String) postSnapshot.child("no").getValue();
                     totalplan = (String) postSnapshot.child("totalplan").getValue();

                     totalplan_int = Integer.parseInt(totalplan);
                     totalplan_int++;
                     if(flag1==0)
                     {
                         total_plan_int = totalplan_int;
                         flag1++;
                     }
                     Log.d("heyyq",no + " " + totalplan_int);
                 }
                 //getInfo();
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
             }
         });
     }

    public void getInfo()
    {
        card.clear();
        mDatabaseCity.child(no).child(cityname).child("for you").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    lat = (String) postSnapshot.child("lat").getValue();
                    lon = (String) postSnapshot.child("lon").getValue();
                    imageurl = (String) postSnapshot.child("imageurl").getValue();
                    place = (String) postSnapshot.child("placename").getValue();
                    ratings = (String) postSnapshot.child("rating").getValue();
                    lats.add(lat);
                    lons.add(lon);
                    url.add(imageurl);
                    placename.add(place);
                    rating.add(ratings);
                    fragment_Class frag = postSnapshot.getValue(fragment_Class.class);
                    frag.setRating(ratings);
                    frag.setImageurl(imageurl);
                    frag.setLat(lat);
                    frag.setLon(lon);
                    frag.setPlacename(place);
                    card.add(frag);
                }

                for(fragment_Class frg : card)
                {
                    Log.d("hioo",frg.getRating());
                }
                initMap();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mGooglemap.setMyLocationEnabled(true);
        //gotoLocation(22.3072,73.1812,15);

        for (int i=0;i<lats.size();i++)
        {
            double lat1 = Double.parseDouble(lats.get(i));
            double lon1 = Double.parseDouble(lons.get(i));
            Log.d("hiww",lat1 + " " + lon1);
            MarkerOptions options = new MarkerOptions()
                    .title(placename.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room_black_24dp))
                    .position(new LatLng(lat1,lon1));
            mGooglemap.addMarker(options);
        }
        mGooglemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ArrayList<fragment_Class> cards = new ArrayList<>();
                cards = (ArrayList<fragment_Class>) card;
                if(flag==0)
                {
                    mDatabaseDayplan.child(no2).child("totalplan").setValue(totalplan_int+"");
                    flag++;
                }
                double lat = marker.getPosition().latitude;
                double lon = marker.getPosition().longitude;

               marker.remove();/*
               if(marker.getTitle().equals("barodas"))
               {
                   MarkerOptions options = new MarkerOptions()
                           .title("baroda")
                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room_black_24dp))
                           .position(new LatLng(lat,lon));
                           Marker m1 = mGooglemap.addMarker(options);
                          // Removeline();
               }
              if
               {*/

                   MarkerOptions options1 = new MarkerOptions()
                           .title(marker.getTitle())
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                           .position(new LatLng(lat,lon));
                   if(marker1==null)
                   {
                       marker1 = mGooglemap.addMarker(options1);
                       markers.add(marker1);
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("planname").setValue("nameee");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("totalplace").setValue(k+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("plan_no").setValue(total_plan_int+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("from").setValue(markers.get(j).getTitle());
                       /*mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("rating").setValue(cards.get(j).getRating());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("placename").setValue(cards.get(j).getPlacename());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lat").setValue(markers.get(j-1).getPosition().latitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lon").setValue(markers.get(j-1).getPosition().longitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("imageurl").setValue(cards.get(j).getImageurl());
                       */
                       k++;
                       j++;
                   }
                   else if(marker2==null)
                   {
                       marker2 = mGooglemap.addMarker(options1);
                       Drawline(marker1,marker2);
                       markers.add(marker2);
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("planname").setValue("nameee");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("totalplace").setValue(k+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("plan_no").setValue(total_plan_int+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("to").setValue(markers.get(j).getTitle());
                       /*mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("rating").setValue(cards.get(j).getRating());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("placename").setValue(cards.get(j).getPlacename());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lat").setValue(markers.get(j-1).getPosition().latitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lon").setValue(markers.get(j-1).getPosition().longitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("imageurl").setValue(cards.get(j).getImageurl());
                       */j++;
                       k++;
                   }
                   else
                   {

                       marker1 = marker2;
                       marker2 = mGooglemap.addMarker(options1);
                       Drawline(marker1,marker2);
                       markers.add(marker2);
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("planname").setValue("nameee");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("totalplace").setValue(k+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("plan_no").setValue(total_plan_int+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("to").setValue(markers.get(j).getTitle());
                       /*mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("rating").setValue(cards.get(j).getRating());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("placename").setValue(cards.get(j).getPlacename());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lat").setValue(markers.get(j-1).getPosition().latitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("lon").setValue(markers.get(j-1).getPosition().longitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + k).child("imageurl").setValue(cards.get(j).getImageurl());
                       */
                       j++;
                       k++;
                   }

                       //mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + l).child("rating").setValue(cards.get(m).getRating());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + l).child("placename").setValue(markers.get(m).getTitle());
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + l).child("lat").setValue(markers.get(m).getPosition().latitude+"");
                       mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + l).child("lon").setValue(markers.get(m).getPosition().longitude+"");
                       //mDatabaseDayplan.child(no2).child("Plans").child("plan" + total_plan_int).child("places").child("places" + l).child("imageurl").setValue(cards.get(m).getImageurl());
                       m++;
                       l++;

               //}
               return false;
            }
        });
    }

    public void Drawline(Marker m1,Marker m2)
    {
        PolylineOptions options = new PolylineOptions()
                .add(m1.getPosition())
                .add(m2.getPosition())
                .color(Color.BLUE)
                .width(8);
        line = mGooglemap.addPolyline(options);
    }
    public void Removeline()
    {
        int flag=1;
        Marker mark1 = null;
        for(Marker mark : markers) {
            if (flag == 1) {
                flag = flag + 1;
                mark1 = mark;
            } else {
                Log.d("hey","lineee");
                PolylineOptions options = new PolylineOptions()
                        .add(mark1.getPosition())
                        .add(mark.getPosition())
                        .color(Color.BLUE)
                        .width(10);
                markers.remove(mark1);
                mGooglemap.clear();
                flag = 1;
            }
        }
     /*   PolylineOptions options = new PolylineOptions()
                .add(m1.getPosition())
                .add(m2.getPosition())
                .color(Color.BLUE)
                .visible(false)
                .width(10);
        line = mGooglemap.addPolyline(options);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST )
        {
            if(resultCode == RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data,this);
                String address = String.format("Place: %s",place.getAddress());
                String address1 = String.format("Place: %s",place.getRating());
                String address2 = String.format("Place: %s",place.getPlaceTypes());

                addresstext.setText(address1);


            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
