package com.classify.dummy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map_plan_show extends AppCompatActivity implements OnMapReadyCallback {

    String plan_no,no,lat,lon,placename;
    GoogleMap mGooglemap;
    RecyclerView recyclerView;
    DatabaseReference mDatabaseDayplan;
    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    Marker marker1;
    Marker marker2;
    Polyline line;
    ArrayList<plan_class> al2 = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double NETlat;
    private double GPSlat;
    private double NETlon;
    private double GPSlon;
    private double NETacc;
    private double GPSacc;
    private Geocoder geocoder;
    List<Address> addresses;
    private String pincode;
    private String city;
    private String addressline;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_plan_show);

        plan_no = getIntent().getExtras().getString("plan_no");
        no = getIntent().getExtras().getString("no");

        mDatabaseDayplan = FirebaseDatabase.getInstance().getReference().child("Dayplan");
        mDatabaseDayplan.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.re);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        setUpRecyclerView(recyclerView);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Map_plan_show.this, Google_login.class));
                } else {
                    String name = firebaseAuth.getCurrentUser().getDisplayName();
                    String useremail = firebaseAuth.getCurrentUser().getEmail();
                    String photourl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
                    getLocation();

                    //  Picasso.with(MainActivity.this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).resize(50, 50).centerCrop().into(userpic);
                    // user.setText(username);
                }
            }
        };

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
                recyclerView.setAdapter(new Map_plan_show.adapter(recyclerView.getContext(), al2));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        geocoder = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GPSacc = location.getAccuracy();
                if (GPSacc < 10) {
                    // Toast.makeText(MainActivity.this, "Accuracy : " + GPSacc, Toast.LENGTH_SHORT).show();
                    GPSlat = location.getLatitude();
                    GPSlon = location.getLongitude();
                    flag=1;
                    initMap();
                    try {
                        addresses = geocoder.getFromLocation(GPSlat, GPSlon, 1);
                        addressline = addresses.get(0).getAddressLine(0);
                          //Toast.makeText(Map_plan_show.this, addresses.get(0).getAddressLine(0) + " city", Toast.LENGTH_LONG).show();
                        //Toast.makeText(Map_plan_show.this, addresses.get(0).getLocality() + " city", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (ActivityCompat.checkSelfPermission(Map_plan_show.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map_plan_show.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    locationManager.removeUpdates(locationListener);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        getLocation();
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        android.location.Location locNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String provider;
        double acc;

        String GPSlocation;
        String NETlocation;
        if(locNetwork !=null)
        {
            provider = locNetwork.getProvider();
            NETacc= locNetwork.getAccuracy();
            NETlat = locNetwork.getLatitude();
            NETlon = locNetwork.getLongitude();
            NETlocation = String.format("%s (%f,%f) %f",provider,NETlat,NETlon,NETacc);
            //  Toast.makeText(MainActivity.this,"Network : "+NETacc +" " + NETlat + " " + NETlon,Toast.LENGTH_LONG).show();


        }
        if(locGPS != null)
        {
            provider = locGPS.getProvider();
            GPSacc = locGPS.getAccuracy();
            GPSlat = locGPS.getLatitude();
            GPSlon = locGPS.getLongitude();
            GPSlocation = String.format("%s (%f,%f) %f",provider,GPSlat,GPSlon,GPSacc);
         //   Toast.makeText(Map_plan_show.this,"GPS Latitude n Longitude : " + GPSlat + " " + GPSlon,Toast.LENGTH_LONG).show();
            new Map_plan_show.GetCity().execute();
        }



    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment_save);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;

        if(flag==1)
        {
            MarkerOptions options = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title(addresses.get(0).getLocality())
                    .position(new LatLng(GPSlat,GPSlon));
            mGooglemap.addMarker(options);

        }



        for (int i=0;i<al2.size();i++)
        {
            MarkerOptions options1 = new MarkerOptions()
                    .title(al2.get(i).getPlacename())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(al2.get(i).getPlacename())
                    .position(new LatLng(Double.parseDouble(al2.get(i).getLat()),Double.parseDouble(al2.get(i).getLon())));
            mGooglemap.addMarker(options1);

            if(marker1==null)
            {
                marker1 = mGooglemap.addMarker(options1);
                markers.add(marker1);
            }
            else if(marker2==null)
            {
                marker2 = mGooglemap.addMarker(options1);
                Drawline(marker1,marker2);
                markers.add(marker2);
            }
            else
            {
                marker1 = marker2;
                marker2 = mGooglemap.addMarker(options1);
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private class GetCity extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            try {
                addresses = geocoder.getFromLocation(GPSlat,GPSlon,1);
                pincode = addresses.get(0).getPostalCode();

          /*  String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+GPSlat+","+GPSlon;
            String JsonStr = sh.makeServiceCall(url);*/

                String url = "https://pincode.saratchandra.in/api/pincode/"+pincode;
                String JsonStr = sh.makeServiceCall(url);

                if(JsonStr != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(JsonStr);
                        JSONArray City = jsonObject.getJSONArray("data");
                        JSONObject c = City.getJSONObject(0);
                        city = c.getString("division_name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Toast.makeText(MainActivity.this,city+" "+pincode,Toast.LENGTH_SHORT).show();

        }
    }

    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new Map_plan_show.adapter(rv.getContext(), al2));
        Log.d("Firebase-data", "user adapter");
    }

    private class adapter extends RecyclerView.Adapter<Map_plan_show.adapter.ViewHolder> {
        ArrayList<plan_class> al2 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<plan_class> al) {
            context = c;
            al2 = al;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView place;
/*
            private ImageView img;
            private TextView time;
            private TextView rats;
            private CardView mapcard;
*/

            // private RelativeLayout comment;

            Context context;

            public ViewHolder(View itemView) {
                super(itemView);
                /*img = (ImageView) itemView.findViewById(R.id.map_img);*/
                place = (TextView) itemView.findViewById(R.id.text10);/*
                time = (TextView) itemView.findViewById(R.id.text20);
                rats = (TextView) itemView.findViewById(R.id.text30);
                mapcard = (CardView) itemView.findViewById(R.id.mapcards);*/


//                comment = (RelativeLayout)itemView.findViewById(R.id.cmnt);

            }
        }

        @Override
        public Map_plan_show.adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_cardview, parent, false);
            return new Map_plan_show.adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.place.setText(al2.get(position).getPlacename());
        }



        @Override
        public int getItemCount() {
            return al2.size();
        }

    }


}

