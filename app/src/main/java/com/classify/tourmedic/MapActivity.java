package com.classify.tourmedic;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGooglemap;
    GoogleApiClient mGoogleApiClient;
    DatabaseReference mDatabaseCity;
    String cityname,no,lat,lon;
    List<String> lats = new ArrayList<String>();
    List<String> lons = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  if (googleServiceAvailable()) {

            setContentView(R.layout.activity_map);
            cityname = getIntent().getExtras().getString("cityname");
            Log.d("places1", cityname);

            mDatabaseCity = FirebaseDatabase.getInstance().getReference().child("City");
            mDatabaseCity.keepSynced(true);
            Query query = mDatabaseCity.orderByChild("cityname").equalTo(cityname);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        no = (String) postSnapshot.child("no").getValue();
                    }
                    getInfo();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
      /*  } else {
        }*/
    }


    public void getInfo()
    {
        mDatabaseCity.child(no).child(cityname).child("for you").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    lat = (String) postSnapshot.child("lat").getValue();
                    lon = (String) postSnapshot.child("lon").getValue();
                    lats.add(lat);
                    lons.add(lon);
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
        mapFragment.getMapAsync(this);
    }

    private boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(MapActivity.this, "Can't Connect to pplay services", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                    .title("baroda")
                    .position(new LatLng(lat1,lon1));
            mGooglemap.addMarker(options);
        }



//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        mGoogleApiClient.connect();
    }

    private void gotoLocation(double lat, double lon, float zoom) {
        LatLng ll = new LatLng(lat, lon);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGooglemap.moveCamera(update);
    }
    LocationRequest locationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1000);
//
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
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        if(location==null)
//        {
//            Toast.makeText(MapActivity.this,"Can't get Current Loocation",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            LatLng ll =new LatLng(location.getLatitude(),location.getLongitude());
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
//          //  Toast.makeText(this,"get Current Loocation",Toast.LENGTH_SHORT).show();
//            mGooglemap.moveCamera(update);
//
//        }
    }
}
