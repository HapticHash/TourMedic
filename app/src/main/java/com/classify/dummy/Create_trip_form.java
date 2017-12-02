package com.classify.dummy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Create_trip_form extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LinearLayout parentLinearLayout;
    DatabaseReference mDatabaseCreateTrip;
    Button btn,btn2;
    String no,email,totalTrip;
    EditText edit_first;
    EditText tripname,Sdate,Edate;
    int i=1;
    int flag=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crete_trip_form);

        mDatabaseCreateTrip = FirebaseDatabase.getInstance().getReference().child("CreateTrip");
        mDatabaseCreateTrip.keepSynced(true);
        edit_first = (EditText)findViewById(R.id.number_edit_text1);
        tripname = (EditText)findViewById(R.id.trip_name);
        Sdate = (EditText)findViewById(R.id.edittext2_date);
        Edate = (EditText)findViewById(R.id.edittext3_date);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        btn = (Button) findViewById(R.id.add_field_button);
        btn2 = (Button) findViewById(R.id.add_field_button2);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    email = firebaseAuth.getCurrentUser().getEmail();
                }
                Query query = mDatabaseCreateTrip.orderByChild("email").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            no = (String) postSnapshot.child("no").getValue();
                            totalTrip = (String) postSnapshot.child("totalTrip").getValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

                    btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = new EditText(Create_trip_form.this);
                        editText.setHint("Add Destination");
                        editText.setId(i);
                        parentLinearLayout.addView(editText);
                        i++;
                    }
                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String viewValue;
                        String tripnameOf = tripname.getText().toString();
                        String sdate = Sdate.getText().toString();
                        String edate = Edate.getText().toString();

                        ViewGroup rootView = (ViewGroup) parentLinearLayout;
                        int count = rootView.getChildCount();
                        Log.d("heyb","count " + count +" "+ sdate +" "+edate);
                        int j=1;
                        int total_trip_int = Integer.parseInt(totalTrip);
                        total_trip_int++;

                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("tripname").setValue(tripnameOf);
                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("trip_no").setValue(total_trip_int+"");
                        String firstPlace = edit_first.getText().toString();
                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("destination").setValue(firstPlace);
                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("startdate").setValue(sdate);
                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("enddate").setValue(edate);
                        j=2;
                        //count = count+1;
                        for (int i = 0; i < count; i++) {
                            View view = rootView.getChildAt(i);
                            if (view instanceof EditText) {
                                viewValue = ((EditText) view).getText().toString();
                                    mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("destination").setValue(viewValue);
                                    mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("startdate").setValue(sdate);
                                    mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("places").child("places" + j).child("enddate").setValue(edate);
                                    j=j+1;
                            }
                        }
                        j=j-1;
                        mDatabaseCreateTrip.child(no).child("Trips").child("Trip" + total_trip_int).child("totalPlace").setValue(j+"");
                        mDatabaseCreateTrip.child(no).child("totalTrip").setValue(total_trip_int+"");
                    }
                });

            }

/*
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Create_trip_form.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }*/
        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

            public void onDelete(View v) {
                parentLinearLayout.removeView((View) v.getParent());
            }
        }
