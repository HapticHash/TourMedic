package com.classify.tourmedic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    ArrayList<String> al = new ArrayList<>();
    ArrayList<search> al1 = new ArrayList<>();
    //ArrayList<fragment_Class> al2 = new ArrayList<>();
    private RecyclerView recyclerView;
    SpinnerDialog spinnerDialog;
    DatabaseReference mDatabaseSearchList;
    DatabaseReference mDatabaseCreateTrip;
    FirebaseUser user;
    int flag=0,flags1=0;
    String email,no,tripname,trip_no;
    EditText searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        try{
            firebaseDatabase.setPersistenceEnabled(true);
        }catch (Exception e){}

        mDatabaseSearchList = FirebaseDatabase.getInstance().getReference().child("Searchlist");
        mDatabaseSearchList.keepSynced(true);
        mDatabaseCreateTrip = FirebaseDatabase.getInstance().getReference().child("CreateTrip");
        mDatabaseCreateTrip.keepSynced(true);

/*
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_mainscreen);
        setUpRecyclerView(recyclerView);
*/


        searchbar = (EditText)findViewById(R.id.searchbar);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, Google_login.class));
                    flags1=1;
                    Log.d("1234","1");
                } else {
                    email = firebaseAuth.getCurrentUser().getEmail();
                    if(flags1==0) {
                        Log.d("1234","2");
                        getInfo();
                        Log.d("1234","3");
                    }
                    if (flag == 0) {
                        initItems();
                        flag = 1;
                    } else {
                        //spinnerDialog.showSpinerDialog();
                    }
                    spinnerDialog = new SpinnerDialog(MainActivity.this, al, "Search city");
                    spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                           /* Intent i = new Intent(MainActivity.this, City.class);
                            i.putExtra("place", item);
                            startActivity(i);*/
                        }
                    });
                }
            }
        };
    }

    public void getInfo() {

        Log.d("1234","4");
        Query query = mDatabaseCreateTrip.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    no = (String) snapshot.child("no").getValue();
                    Log.d("hii",no);
                }
              //  getData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /*public void getData() {
        Log.d("hii","hii2");
        al2.clear();
        mDatabaseCreateTrip.child(no).child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    tripname = (String) snapshot.child("tripname").getValue();
                    trip_no = (String) snapshot.child("trip_no").getValue();
                    Log.d("hii",tripname);
                    fragment_Class frag = snapshot.getValue(fragment_Class.class);
                    frag.setTripname(tripname);
                    frag.setTrip_no(trip_no);
                    al2.add(frag);
                }
                recyclerView.setAdapter(new MainActivity.adapter(recyclerView.getContext(), al2));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
*/
    private void initItems() {

        mDatabaseSearchList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al1.clear();
                int j=1;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String name;
                    name = (String) postSnapshot.child("name").getValue();
                    Log.d("hi11", name + "");
                    search cardobj = postSnapshot.getValue(search.class);
                    cardobj.setName(name);
                    al1.add(cardobj);
                    j++;
                }
                String cityname;
                for (int i = 0; i < al1.size(); i++) {
                    Log.d("hi112", al1.get(i).getName() + "");
                    cityname = al1.get(i).getName();
                    al.add(cityname);
                }
                //spinnerDialog.showSpinerDialog();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }


   /* private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(), al2));
        Log.d("Firebase-data", "user adapter");
    }
*/
   /* private class adapter extends RecyclerView.Adapter<MainActivity.adapter.ViewHolder> {
        ArrayList<fragment_Class> al2 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<fragment_Class> al) {
            context = c;
            al2 = al;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tripname;
            private CardView tripcard;

            // private RelativeLayout comment;

            Context context;

            public ViewHolder(View itemView) {
                super(itemView);
                tripname = (TextView) itemView.findViewById(R.id.mstv1);
                tripcard = (CardView) itemView.findViewById(R.id.cardview_trip);

//                comment = (RelativeLayout)itemView.findViewById(R.id.cmnt);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tripcard_mainscreen, parent, false);
            return new MainActivity.adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.tripname.setText(al2.get(position).getTripname());
            holder.tripcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,Tripcard_details.class);
                    i.putExtra("trip_no",al2.get(position).getTrip_no());
                    i.putExtra("no",no);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return al2.size();
        }

    }
*/
}
