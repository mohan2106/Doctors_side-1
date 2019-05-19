package com.example.doctors_side;

import android.app.DatePickerDialog;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {



FirebaseFirestore db= FirebaseFirestore.getInstance();
    public String date=cd();
    private CollectionReference apointmentref= db.collection("Doctors").document("India").collection("Guwahati").document("Guwahati1").collection(date);
    private int mYear, mMonth, mDay;
    private AppointmentAdaptor adaptor;
    public void change_date()
    {
        final GregorianCalendar c = new GregorianCalendar();

        mYear = c.get(GregorianCalendar.YEAR);
        mMonth = c.get(GregorianCalendar.MONTH);
        mDay = c.get(GregorianCalendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date=String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        apointmentref = db.collection("Doctors").document("India").collection("Guwahati").document("Guwahati1").collection(date);
                        setUpRecyclerView();
                        adaptor.startListening();


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();


    }
    public void change_doctor()
    {

    }
    public String cd()
    {
        final GregorianCalendar c = new GregorianCalendar();

        mYear = c.get(GregorianCalendar.YEAR);
        mMonth = c.get(GregorianCalendar.MONTH);
        mDay = c.get(GregorianCalendar.DAY_OF_MONTH);
        String date0=String.valueOf(mDay+"-"+(mMonth+1)+"-"+mYear);
        return date0;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_appointment_menu, menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                change_date();
                return true;
            case R.id.action_settings1:
                change_doctor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();
    }

    private void setUpRecyclerView()
    {
        Query query = apointmentref.orderBy("TID",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>().setQuery(query,Appointment.class).build();

        adaptor=new AppointmentAdaptor(options)
        {
            @Override
            public void onDataChanged() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,-1);
            }

        };

        RecyclerView recyclerView = findViewById(R.id.Recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        adaptor.startListening();
    }


}
