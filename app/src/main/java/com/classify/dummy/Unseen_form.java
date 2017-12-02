package com.classify.dummy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Unseen_form extends AppCompatActivity {



    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseUnseenBeauty;
    Button mapBtn;
    EditText placename,desc;
    ImageView img;
    Button save;
    String imageurl,email,no,total_unseen;
    Uri filePath;
    Bitmap gallery_upload;
    int total_unseen_int,flag=0;
    private static final int PICK_IMAGE_REQUEST = 234;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unseen_form);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Unseen_form.this, Google_login.class));
                } else {
                    email = firebaseAuth.getCurrentUser().getEmail();


                    //  Picasso.with(MainActivity.this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).resize(50, 50).centerCrop().into(userpic);
                    // user.setText(username);
                }
            }
        };

        mDatabaseUnseenBeauty = FirebaseDatabase.getInstance().getReference().child("Unseen");
        mDatabaseUnseenBeauty.keepSynced(true);

        placename = (EditText)findViewById(R.id.edittext1);
        desc = (EditText)findViewById(R.id.edittext2);
        img = (ImageView)findViewById(R.id.imgv);
        save = (Button) findViewById(R.id.save_unseen);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String unseen_place = placename.getText().toString();
                final String description = desc.getText().toString();

                Query query = mDatabaseUnseenBeauty.orderByChild("email").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            no = (String) snapshot.child("no").getValue();
                            total_unseen = (String) snapshot.child("totalUnseen").getValue();
                            total_unseen_int = Integer.parseInt(total_unseen);
                            total_unseen_int++;
                        }
                        if(flag==0)
                        {
                            mDatabaseUnseenBeauty.child(no).child("UnseenPost").child(total_unseen_int+"").child("placename").setValue(unseen_place);
                            mDatabaseUnseenBeauty.child(no).child("UnseenPost").child(total_unseen_int+"").child("description").setValue(description);
                            mDatabaseUnseenBeauty.child(no).child("UnseenPost").child(total_unseen_int+"").child("unseen_no").setValue(total_unseen_int);
                            mDatabaseUnseenBeauty.child(no).child("totalUnseen").setValue(total_unseen_int);
                            flag=1;
                            Intent i = new Intent(Unseen_form.this,For_you_tab.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
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
                    try {
                        addresses = geocoder.getFromLocation(GPSlat, GPSlon, 1);
                        addressline = addresses.get(0).getAddressLine(0);
                        //Toast.makeText(Unseen_form.this, addresses.get(0).getAddressLine(0) + " city", Toast.LENGTH_LONG).show();
                        //Toast.makeText(Unseen_form.this, addresses.get(0).getLocality() + " city", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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

        if (ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        getLocation();


    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Unseen_form.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(Unseen_form.this,"GPS Latitude n Longitude : " + GPSlat + " " + GPSlon,Toast.LENGTH_LONG).show();
            new Unseen_form.GetCity().execute();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!= null)
        {
            filePath = data.getData();
            try {
                gallery_upload = MediaStore.Images.Media.getBitmap(Unseen_form.this.getContentResolver(),filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
/*
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gallery_upload.compress(Bitmap.CompressFormat.JPEG,70,baos);
            byte[] data1 = baos.toByteArray();
            */


            img.setImageBitmap(gallery_upload);
/*
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://tourmadic.appspot.com");
            StorageReference ref = storageRef.child("gallery/"+new Date().toString()+"image.jpeg");

            UploadTask uploadTask = ref.putBytes(data1);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uri = taskSnapshot.getDownloadUrl();
                    final String msg = taskSnapshot.getDownloadUrl().toString();
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            Log.d("nums","3");
                            //sendMsg(msg,"1");
                        }
                    } ).start();

                }
            });*/
        }

    }

    @Override
    public void onStart() {
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


}
