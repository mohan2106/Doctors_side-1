package com.example.doctors_side;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.*;

import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    Intent in,in2;
    public String UserIdToDisplay="";
    public static final String MyPREFERENCES = "Doctor" ;
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;
    public TextView doctorname;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    public String date=cd();
    public String docname="Guwahati1";
    private CollectionReference apointmentref= db.collection("Doctors").document("India").collection("Guwahati").document(docname).collection("21-5-2019");
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
        in2 = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(in2);
        finish();
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
       if(mtoggle.onOptionsItemSelected(item))
       {
           return true;
       }
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

        sharedpreferences= getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("passKey",null)==null){
            in = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(in);
            finish();
        }
        else
        {
            docname=sharedpreferences.getString("passKey",null);
        }
        apointmentref= db.collection("Doctors").document("India").collection("Guwahati").document(docname).collection("22-5-2019");

        setContentView(R.layout.activity_main);

        mDrawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        mtoggle=new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        setUpRecyclerView();
        mDrawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navheaderView = navigationView.getHeaderView(0);
        doctorname=(TextView)navheaderView.findViewById(R.id.Doctor_name);
        doctorname.setText(sharedpreferences.getString("nameKey","Doctor Name"));


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

        adaptor.setOnItemClickListener(new AppointmentAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id=documentSnapshot.getId();
                UserIdToDisplay=id;
                in = new Intent(MainActivity.this,UserProfile.class);
                in.putExtra("UserId",id);
                startActivity(in);
//                finish();

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        adaptor.startListening();
    }


}
