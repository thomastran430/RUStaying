/************************
 Authors:
 Mohammed Sapin
 Nga Man (Mandy) Cheng
 Purna Haque
 *************************/

package com.example.rustaying;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomServices extends AppCompatActivity {

    private static final String TAG = "RoomServices";

    //Services List
    private ArrayList<Service> serviceList = new ArrayList<>();


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_services);

        createRecycleView();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: Signed In");
                } else {
                    Log.d(TAG, "onAuthStateChanged: Signed out");
                }
            }
        };

        myRef.child("Service").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createRecycleView(){
        Log.d(TAG, "createRecycleView: Started view");
        RecyclerView recyclerView = findViewById(R.id.viewRoomServiceRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RoomServicesAdapter adapter = new RoomServicesAdapter(this,serviceList);
        recyclerView.setAdapter(adapter);
    }


    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot data : dataSnapshot.getChildren()) {

            Service info = new Service();

            info.setRequestType(data.getValue(Service.class).getRequestType());
            info.setRequestDate(data.getValue(Service.class).getRequestDate());
            info.setRequestedTimeRoomService(data.getValue(Service.class).getRequestedTimeRoomService());
            info.setCheckboxes(data.getValue(Service.class).getCheckboxes());
            info.setInputs(data.getValue(Service.class).getInputs());
            info.setSoap(data.getValue(Service.class).getSoap());
            info.setTowels(data.getValue(Service.class).getTowels());
            info.setBedsheets(data.getValue(Service.class).getBedsheets());
            info.setCleaningservice(data.getValue(Service.class).getCleaningservice());
            info.setId(data.getValue(Service.class).getId());
            info.setStatus(data.getValue(Service.class).getStatus());


            //add object to array list
            //Room Service
            if (info.getRequestType().equals("RoomService")&& (info.getStatus().equals("Incomplete") || info.getStatus().equals("In Progress"))) {
                serviceList.add(new Service(info.getRequestType(), info.getRequestDate(),
                        info.getRequestedTimeRoomService(),  info.getInputs(), info.getTowels(),
                        info.getSoap(), info.getBedsheets(),
                        info.getCleaningservice(),
                        info.getCheckboxes(),info.getStatus(), info.getId()));
                //add array list to recycle view
                createRecycleView();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}