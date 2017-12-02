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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Planlist extends AppCompatActivity {

    DatabaseReference mDatabaseDayplan;
    RecyclerView recyclerView;
    String no,rating,imageurl,lat,lon,planname,plan_no,totalplace,from,to;
    int j=1,flag=0;
    ArrayList<plan_class> al2 = new ArrayList<>();


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planlist);

        no = getIntent().getExtras().getString("no");
       // Toast.makeText(Planlist.this,no,Toast.LENGTH_LONG).show();
        Log.d("noooo",no);

        mDatabaseDayplan = FirebaseDatabase.getInstance().getReference().child("Dayplan");
        mDatabaseDayplan.keepSynced(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_dayplan_cards);
        setUpRecyclerView(recyclerView);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    Log.d("abcd","hi1");
                    getDate();
                    Log.d("abcd","hi2");
                }


            }
        };  //    photourl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();


    }



    public void getDate()
    {
        Log.d("abcd","hi222 "+ no);
        al2.clear();
        mDatabaseDayplan.child(no).child("Plans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    planname = (String) postSnapshot.child("planname").getValue();
                    totalplace = (String) postSnapshot.child("totalplace").getValue();
                    plan_no = (String) postSnapshot.child("plan_no").getValue();
                    from = (String) postSnapshot.child("from").getValue();
                    to = (String) postSnapshot.child("to").getValue();

                    plan_class frag = postSnapshot.getValue(plan_class.class);
                    frag.setFrom(from);
                    frag.setPlan_no(plan_no);
                    frag.setPlanname(planname);
                    frag.setTo(to);
                    frag.setTotalplace(totalplace);
                    al2.add(frag);
                }
                recyclerView.setAdapter(new Planlist.adapter(recyclerView.getContext(), al2));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
/*
    public void getData()
    {
        for(plan_class frg : al2)
        {
            Log.d("1234",frg.getFrom());
            Log.d("1234",frg.getTo());
        }
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new Planlist.adapter(rv.getContext(), al2));
        Log.d("Firebase-data", "user adapter");
    }

    private class adapter extends RecyclerView.Adapter<Planlist.adapter.ViewHolder> {
        ArrayList<plan_class> al2 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<plan_class> al) {
            context = c;
            al2 = al;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView from;
            private TextView to;
            private TextView plannames;
            private CardView plancard;

            // private RelativeLayout comment;

            Context context;

            public ViewHolder(View itemView) {
                super(itemView);
                from = (TextView) itemView.findViewById(R.id.fttv2);
                to = (TextView) itemView.findViewById(R.id.fftv4);
                plannames = (TextView) itemView.findViewById(R.id.fftv5);
                plancard = (CardView) itemView.findViewById(R.id.cardview_dayplan_card);


//                comment = (RelativeLayout)itemView.findViewById(R.id.cmnt);

            }
        }

        @Override
        public Planlist.adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_plan_card, parent, false);
            return new Planlist.adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.from.setText(al2.get(position).getFrom());
            holder.to.setText(al2.get(position).getTo());
            holder.plannames.setText(al2.get(position).getPlanname());
            holder.plancard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Planlist.this,Map_plan_show.class);
                    i.putExtra("plan_no",al2.get(position).getPlan_no());
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
}
