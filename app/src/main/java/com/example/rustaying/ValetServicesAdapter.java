/************************
 Authors:
 Mohammed Sapin
 Nga Man (Mandy) Cheng
 Purna Haque
 *************************/

package com.example.rustaying;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValetServicesAdapter extends RecyclerView.Adapter<ValetServicesAdapter.ViewHolder> {
    private static final String TAG = "ValetServicesAdapter";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private ArrayList<Service> serviceList;
    private Context mContext;

    public ValetServicesAdapter (Context mContext, ArrayList<Service> serviceList){
        this.serviceList = serviceList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_valet_services,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i){
        Log.d(TAG, "onBindViewHolder: Called");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        final Service info = serviceList.get(i);

        viewHolder.requestTypeV.setText(info.getRequestType());
        viewHolder.requestTimeV.setText(info.getRequestedTimeValet());
        viewHolder.requestDateV.setText(info.getRequestDate());
        viewHolder.startingStreetV.setText(info.getStartingStreet());
        viewHolder.startingCityStateZipV.setText(info.getStartingCityStateZip());
        viewHolder.destinationStreetV.setText(info.getDestinationStreet());
        viewHolder.destinationCityStateZipV.setText(info.getDestinationCityStateZip());
        viewHolder.travelNumber.setText(info.getNumberTraveling());
        viewHolder.statusV.setText(info.getStatus());
        viewHolder.idV.setText(String.valueOf(info.getId()));

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setMessage("Set status of Service:").setPositiveButton("Completed",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Service")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //search for matching id information in database to update status
                                                Map<String, Object> statusUpdate = new HashMap<>();
                                                long currentId=info.getId();
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Log.d(TAG, "ViewServiceClass: =============================" + snapshot.getKey());
                                                    String userId = snapshot.getKey();
                                                    for (DataSnapshot snapshot2 : snapshot.getChildren()){
                                                        String requestID=snapshot2.getKey();
                                                        Log.d(TAG, "ViewServiceClass: =============================" + snapshot2.getKey());
                                                        if (snapshot2.child("id").getValue()!=null) {
                                                            long id = Integer.parseInt(snapshot2.child("id").getValue().toString());
                                                            if (id==currentId){
                                                                statusUpdate.put("status","Completed");
                                                                myRef.child("Service").child(userId).child(requestID).updateChildren(statusUpdate);
                                                                viewHolder.statusV.setText(info.getStatus());
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });

                            }
                        }).setNegativeButton("In Progress  ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Service")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //search for matching id information in database to update status
                                                Map<String, Object> statusUpdate = new HashMap<>();
                                                long currentId=info.getId();
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String userId = snapshot.getKey();
                                                    for (DataSnapshot snapshot2 : snapshot.getChildren()){
                                                        String requestID=snapshot2.getKey();
                                                        if (snapshot2.child("id").getValue()!=null) {
                                                            long id = Integer.parseInt(snapshot2.child("id").getValue().toString());
                                                            if (id==currentId){
                                                                statusUpdate.put("status","In Progress");
                                                                myRef.child("Service").child(userId).child(requestID).updateChildren(statusUpdate);
                                                                viewHolder.statusV.setText(info.getStatus());
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                            }
                        });

                AlertDialog alert = alertDialog.create();
                alert.setTitle("Status Update");
                alert.show();

                Button negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.RED);
                negativeButton.setBackgroundColor(Color.WHITE);

                Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.GREEN);
                positiveButton.setBackgroundColor(Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount(){
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView requestTypeV, requestDateV, requestTimeV, startingStreetV, startingCityStateZipV,
                destinationStreetV, destinationCityStateZipV, travelNumber, statusV, idV;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.ValetCard);
            requestTypeV = itemView.findViewById(R.id.requestTypeV);
            requestDateV = itemView.findViewById(R.id.requestDateV);
            requestTimeV = itemView.findViewById(R.id.requestTimeV);
            startingStreetV = itemView.findViewById(R.id.StartingStreetV);
            startingCityStateZipV = itemView.findViewById(R.id.startingCityStateZipV);
            destinationStreetV = itemView.findViewById(R.id.destinationStreetV);
            destinationCityStateZipV = itemView.findViewById(R.id.destinationCityStateZipV);
            travelNumber=itemView.findViewById(R.id.travelNumber);
            statusV = itemView.findViewById(R.id.statusV);
            idV=itemView.findViewById(R.id.idV);
        }
    }
}