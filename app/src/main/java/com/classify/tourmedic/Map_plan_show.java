package com.classify.tourmedic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Map_plan_show extends AppCompatActivity implements OnMapReadyCallback {

    String plan_no,no,lat,lon,placename;
    GoogleMap mGooglemap;
    DatabaseReference mDatabaseDayplan;

    Marker marker1;
    Marker marker2;
    Polyline line;
    ArrayList<plan_class> al2 = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_plan_show);

        plan_no = getIntent().getExtras().getString("plan_no");
        no = getIntent().getExtras().getString("no");

        mDatabaseDayplan = FirebaseDatabase.getInstance().getReference().child("Dayplan");
        mDatabaseDayplan.keepSynced(true);

        mDatabaseDayplan.child(no).child("Plans").child("plan"+plan_no).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    lat = (String) snapshot.child("lat").getValue();
                    lon = (String) snapshot.child("lon").getValue();
                    placename = (String) snapshot.child("placename").getValue();
                    plan_class frag = snapshot.getValue(plan_class.class);
                    frag.setLat(lat);
                    frag.setLon(lon);
                    frag.setPlacename(placename);
                    al2.add(frag);
                }
                initMap();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment_save);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;

        for (int i=0;i<al2.size();i++)
        {
            MarkerOptions options = new MarkerOptions()
                    .title(al2.get(i).getPlacename())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(al2.get(i).getPlacename())
                    .position(new LatLng(Double.parseDouble(al2.get(i).getLat()),Double.parseDouble(al2.get(i).getLon())));
            mGooglemap.addMarker(options);

            if(marker1==null)
            {
                marker1 = mGooglemap.addMarker(options);
                markers.add(marker1);
            }
            else if(marker2==null)
            {
                marker2 = mGooglemap.addMarker(options);
                Drawline(marker1,marker2);
                markers.add(marker2);
            }
            else
            {
                marker1 = marker2;
                marker2 = mGooglemap.addMarker(options);
                Drawline(marker1,marker2);
                markers.add(marker2);
            }


        }
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
}
