package com.example.doctors_side;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.concurrent.TimeoutException;

public class AppointmentAdaptor extends FirestoreRecyclerAdapter<Appointment, AppointmentAdaptor.AppointmentHolder> {

    public AppointmentAdaptor(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentHolder holder, int position, @NonNull Appointment model) {
        holder.textViewtime.setText(model.getTime());
        holder.textViewdescription.setText((model.getDescription()));
        holder.textViewpriority.setText(String.valueOf(model.getPriority()));

    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointment_list,viewGroup,false);
        return new AppointmentHolder(v);
    }

    class AppointmentHolder extends RecyclerView.ViewHolder{
        TextView textViewtime;
        TextView textViewdescription;
        TextView textViewpriority;

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewtime=itemView.findViewById(R.id.text_view_time);
            textViewdescription=itemView.findViewById(R.id.text_view_description);
            textViewpriority=itemView.findViewById(R.id.text_view_priority);
        }
    }
}

