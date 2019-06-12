package com.example.doctors_side;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

//import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int PICK_IMAGE=1;
    private ImageView profile_image;
    private TextView name;
    private TextView mobile,email,weight;
    private Button btn;
    private StorageReference mStorage=FirebaseStorage.getInstance().getReference().child("UserImages");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private Uri imageUri;
    private ProgressBar pb;
    private ProgressDialog pd;
    public String id="";
    private String blood;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profile_image=(ImageView) findViewById(R.id.profile_image);
        name=(TextView)findViewById(R.id.profile_name);
        email=(TextView)findViewById(R.id.profile_email);
        mobile=(TextView) findViewById(R.id.profile_mobile);
        weight=(TextView) findViewById(R.id.profile_weight);
        btn=(Button)findViewById(R.id.profile_save_change);
        pb=(ProgressBar)findViewById(R.id.progressBarProfile);
        pd=new ProgressDialog(UserProfile.this);
        Intent i=getIntent();
        id=i.getStringExtra("UserId");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitle("Profile");
        final Spinner spinner = (Spinner) findViewById(R.id.profile_blood_group);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(UserProfile.this);


        final Spinner spinner2 = (Spinner) findViewById(R.id.profile_sex);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(UserProfile.this);

        imageUri=null;
//        profile_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
//            }
//        });
        pd.setMessage("Loading Patient's profile data....");
        pd.show();
        firestore.collection("USERS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getString("UserName"));
                email.setText(documentSnapshot.getString("UserEmail"));
                weight.setText(documentSnapshot.getString("UserWeight"));
                mobile.setText(documentSnapshot.getString("UserPhone"));
//                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                Glide.with(UserProfile.this).load(documentSnapshot.getString("image")).into(profile_image);
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            imageUri=data.getData();
            profile_image.setImageURI(imageUri);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
