package com.example.doctors_side;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText ed1,ed2;
    Button b1;
    Intent in;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private DocumentReference DoctorName;

    public static final String MyPREFERENCES = "Doctor" ;
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ed1=(EditText)findViewById(R.id.signin_email);
        ed2=(EditText)findViewById(R.id.signin_password);
        final SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        b1=(Button)findViewById(R.id.signinButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name  = ed1.getText().toString();
                final String password  = ed2.getText().toString();
//                Toast.makeText(getApplicationContext(),password,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

                DoctorName= db.collection("Doctors").document("India").collection("Guwahati").document(password);
                final Map<String, Object>[] data1 = new Map[]{new HashMap<>()};
                DoctorName.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name1=(String)(document.getData()).get("Name");
//                                String name2 = name;
//                                Toast.makeText(getApplicationContext(),name2+"*",Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),name1+"#",Toast.LENGTH_SHORT).show();
                                if(name1.equals(name))
                                {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Password, password);
                                    editor.putString(Name, name);
                                    editor.commit();
//                                    Toast.makeText(getApplicationContext(),name1+"#",Toast.LENGTH_SHORT).show();
                                    in= new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(in);
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(),name1,Toast.LENGTH_SHORT).show();


                            } else {
                            }
                        } else {
                        }
                    }
                });



//                SharedPreferences.Editor editor = sharedpreferences.edit();
//
////                editor.putString(Name, n);
//                editor.putString(Password, password);
//                editor.putString(Name, name);
//                editor.commit();

//                in = new Intent(MainActivity.this,Main2Activity.class);
//                startActivity(in);
//                finish();
            }
        });

    }
}
