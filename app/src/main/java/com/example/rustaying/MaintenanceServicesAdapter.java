package com.example.rustaying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MaintenanceServicesAdapter extends RecyclerView.Adapter<MaintenanceServicesAdapter.ViewHolder> {
    private static final String TAG = "MaintenanceServicesAdapter";

    private ArrayList<Service> serviceList;
    private Context mContext;

    public MaintenanceServicesAdapter (Context mContext, ArrayList<Service> serviceList){
        this.serviceList = serviceList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_maintenance_services,viewGroup,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        Log.d(TAG, "onBindViewHolder: Called");

        Service info = serviceList.get(i);

        viewHolder.requestType.setText(info.getRequestType());
        viewHolder.requestTime.setText(info.getRequestedTimeMaintenance());
        viewHolder.requestDate.setText(info.getRequestDate());
        viewHolder.checkboxes.setText(info.getCheckboxes());
        viewHolder.inputs.setText(info.getInputs());
    }

    @Override
    public int getItemCount(){
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView requestType, requestTime, requestDate, checkboxes, inputs;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            requestType = itemView.findViewById(R.id.requestTypeM);
            requestTime = itemView.findViewById(R.id.requestTimeM);
            requestDate = itemView.findViewById(R.id.requestDateM);
            checkboxes = itemView.findViewById(R.id.checkboxesM);
            inputs = itemView.findViewById(R.id.inputsM);
        }
    }
}
