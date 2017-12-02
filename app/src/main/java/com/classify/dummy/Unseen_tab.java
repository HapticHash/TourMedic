package com.classify.dummy;

import android.content.Context;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by ADMIN on 25-11-2017.
 */

public class Unseen_tab extends Fragment
{


    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseUnseenBeauty;
    Button mapBtn;
    EditText placename,desc;
    private RecyclerView recyclerView;
    ImageView img;
    Button save;
    String imageurl,email,no,total_unseen,place,descript;
    ArrayList<unseen_class> al1 = new ArrayList<>();
    int total_unseen_int,flag=0;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDatabaseUnseenBeauty = FirebaseDatabase.getInstance().getReference().child("Unseen");
        mDatabaseUnseenBeauty.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getContext(), Google_login.class));
                } else {
                    email = firebaseAuth.getCurrentUser().getEmail();

                    //  Picasso.with(MainActivity.this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).resize(50, 50).centerCrop().into(userpic);
                    // user.setText(username);
                }
            }
        };


    }

    public void getdata() {

        mDatabaseUnseenBeauty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    descript = (String) snapshot.child("desc").getValue();
                    imageurl = (String) snapshot.child("imageurl").getValue();
                    place = (String) snapshot.child("place").getValue();
                    Log.d("hey11",descript + " " + imageurl + " "+ place  + " jjhjs");
                    unseen_class frag = snapshot.getValue(unseen_class.class);
                    frag.setPlace(place);
                    frag.setImageurl(imageurl);
                    frag.setDesc(descript);
                    al1.add(frag);
                }
                recyclerView.setAdapter(new adapter(recyclerView.getContext(), al1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.unseen_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_unseenss);
        setUpRecyclerView(recyclerView);
        getdata();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(), al1));
        Log.d("Firebase-data", "user adapter");
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        ArrayList<unseen_class> al1 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<unseen_class> al) {
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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.placename.setText(al1.get(position).getPlace());
            holder.desccription.setText(al1.get(position).getDesc());
          //  Picasso.with(context).load(al1.get(position).getImageurl()).fit().into(holder.imageurl);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),Detail_card.class);
                    i.putExtra("placename",al1.get(position).getPlace());
                    i.putExtra("desc",al1.get(position).getDesc());
                    //i.putExtra("url",al1.get(position).getImageurl());
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
