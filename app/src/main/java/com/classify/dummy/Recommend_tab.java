package com.classify.dummy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ADMIN on 25-11-2017.
 */



public class Recommend_tab extends Fragment {

    DatabaseReference mDatabaseCity;
    Button mapBtn;
    private RecyclerView recyclerView;
    String placename;
    ArrayList<search> al = new ArrayList<>();
    ArrayList<fragment_Class> al1 = new ArrayList<>();
    String no;
    String Address;
    String Description;
    String Rating;
    String imageUrl;
    String lat;
    String lon;
    String no_place;
    String placename_of_city;
    int flag = 0;

    public Recommend_tab() {
    }

    //@SuppressLint("ValidFragment")
    public Recommend_tab(String placename) {
        this.placename = placename;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseCity = FirebaseDatabase.getInstance().getReference().child("City");
        mDatabaseCity.keepSynced(true);

        //Log.d("place",placename);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_recommend);
        setUpRecyclerView(recyclerView);

//
//        mapBtn = (Button) rootView.findViewById(R.id.map);
//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), MapActivity.class);
//                i.putExtra("cityname", placename);
//                startActivity(i);
//            }
//        });



       Query query = mDatabaseCity.orderByChild("cityname").equalTo(placename);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("place","hi");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    no = (String) postSnapshot.child("no").getValue();
                    Log.d("place",no);
                }
                getInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }


    public void getInfo() {
        al1.clear();
        Query query = mDatabaseCity.child(no).child(placename).child("for you").orderByChild("rating");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Address = (String) postSnapshot.child("address").getValue();
                    Description = (String) postSnapshot.child("description").getValue();
                    Rating = (String) postSnapshot.child("rating").getValue();
                    imageUrl = (String) postSnapshot.child("imageurl").getValue();
                    lat = (String) postSnapshot.child("lat").getValue();
                    lon = (String) postSnapshot.child("lon").getValue();
                    no_place = (String) postSnapshot.child("no").getValue();
                    placename_of_city = (String) postSnapshot.child("placename").getValue();
                    Log.d("hiw", Address);
                    Log.d("hiw", Description);
                    Log.d("hiw", Rating);
                    Log.d("hiw", imageUrl);
                    Log.d("hiw", lat);
                    Log.d("hiw", lon);
                    Log.d("hiw", no_place);
                    Log.d("hiw", placename_of_city);
                    Double rating = Double.parseDouble(Rating);
                    if(rating>4.0){
                        fragment_Class frag = postSnapshot.getValue(fragment_Class.class);
                        frag.setAddress(Address);
                        frag.setDescription(Description);
                        frag.setRating(Rating);
                        frag.setImageurl(imageUrl);
                        frag.setLat(lat);
                        frag.setLon(lon);
                        frag.setNo(no_place);
                        frag.setPlacename(placename_of_city);
                        al1.add(frag);
                    }
                }
                Collections.reverse(al1);
                recyclerView.setAdapter(new adapter(recyclerView.getContext(), al1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(), al1));
        Log.d("Firebase-data", "user adapter");
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        ArrayList<fragment_Class> al1 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<fragment_Class> al) {
            context = c;
            al1 = al;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageurl;
            private RatingBar ratingStar;
            private TextView placename;
            private TextView rating;
            private TextView desccription;
            private TextView time;
            private CardView card;

            // private RelativeLayout comment;

            Context context;

            public ViewHolder(View itemView) {
                super(itemView);
                placename = (TextView) itemView.findViewById(R.id.place_card);
                rating = (TextView) itemView.findViewById(R.id.rating_thing);
                desccription = (TextView) itemView.findViewById(R.id.deesc_thing);
                time = (TextView) itemView.findViewById(R.id.time_thing);
                imageurl = (ImageView) itemView.findViewById(R.id.img_things);
                ratingStar = (RatingBar) itemView.findViewById(R.id.ratingBar_thing);
                card = (CardView) itemView.findViewById(R.id.cardview_things);
//                comment = (RelativeLayout)itemView.findViewById(R.id.cmnt);

            }
        }

        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.things_card, parent, false);
            return new adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

                holder.placename.setText(al1.get(position).getPlacename());
                holder.rating.setText(al1.get(position).getRating());
                holder.desccription.setText(al1.get(position).getDescription());
                Picasso.with(context).load(al1.get(position).getImageurl()).fit().into(holder.imageurl);
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(),Detail_card.class);
                        i.putExtra("placename",al1.get(position).getPlacename());
                        i.putExtra("rating",al1.get(position).getRating());
                        i.putExtra("desc",al1.get(position).getDescription());
                        i.putExtra("url",al1.get(position).getImageurl());
                        startActivity(i);
                    }
                });

        }
        @Override
        public int getItemCount() {
            return al1.size();
        }

    }
}
