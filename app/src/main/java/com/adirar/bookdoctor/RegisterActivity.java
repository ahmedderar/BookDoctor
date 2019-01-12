package com.adirar.bookdoctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adirar.bookdoctor.data.Patient;
import com.adirar.bookdoctor.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    final String TAG="RegisterActivity";

    EditText name;
    EditText phone;
    EditText email;
    Button regBtn;
     User user;

   // private FirebaseDatabase mFirebaseDatabase;
   // private DatabaseReference patientsDatabaseReference;
    FirebaseFirestore firebaseStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       user = (User) getIntent().getSerializableExtra("user");

        name = (EditText) findViewById(R.id.name_et);
        phone = (EditText) findViewById(R.id.phone_et);
        email = (EditText) findViewById(R.id.email_et);
        regBtn = (Button) findViewById(R.id.reg_finish_btn);


        name.setText(user.getName());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());

        //Intitialize firebase
       // mFirebaseDatabase = FirebaseDatabase.getInstance();
        //patientsDatabaseReference = mFirebaseDatabase.getReference().child("patients");
        // thisGameDatabaseReference = gamesDatabaseReference.child(TransrerData.gameId);

        // FireStore
        firebaseStore = FirebaseFirestore.getInstance();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient mPatient = new Patient();
                mPatient.setName(name.getText().toString());
                user.setName(mPatient.getName());
                mPatient.setPhone(phone.getText().toString());
                user.setPhone(mPatient.getPhone());
                mPatient.setEmail(email.getText().toString());
                //Push to firebase
                firebaseStore.collection("patients").document(mPatient.getPhone()).set(mPatient)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG,"Patient data updated successfully");
                                Toast.makeText(RegisterActivity.this, "successfully Sent", Toast.LENGTH_SHORT).show();
                                Intent i  = new Intent(RegisterActivity.this,AppointmentActivity.class);
                                i.putExtra("user",user);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,"Patient data updated Failed");
                        Toast.makeText(RegisterActivity.this, "Failed To Sent", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });
    }
}
