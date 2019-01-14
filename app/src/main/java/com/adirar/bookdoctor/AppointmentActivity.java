package com.adirar.bookdoctor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.adirar.bookdoctor.data.Appointment;
import com.adirar.bookdoctor.data.Patient;
import com.adirar.bookdoctor.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {
    final String TAG = "AppointmentActivity";
    Calendar myCalendar;
    EditText dateEditText;
    DatePickerDialog.OnDateSetListener date;
    TextView appointmentResultTv;
    TextView appointmentTimeTv;
    Button bookAppointment;

    User user;
    Appointment appointment;
    Patient patient;
    int appointmentCount = 0;

    FirebaseFirestore firebaseStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        user = (User) getIntent().getSerializableExtra("user");
        firebaseStore = FirebaseFirestore.getInstance();

        appointmentResultTv = (TextView) findViewById(R.id.appointment_result);
        appointmentTimeTv = (TextView) findViewById(R.id.appointment_time);
        bookAppointment = (Button) findViewById(R.id.book_appointment_btn);

        myCalendar = Calendar.getInstance();

        dateEditText = (EditText) findViewById(R.id.date_et);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();


                ////************************/////////////////////////////
                SimpleDateFormat weekdateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                String dayOfWeek = weekdateformat.format(date);
                if (dayOfWeek.equalsIgnoreCase("saturday") || dayOfWeek.equalsIgnoreCase("monday")
                        || dayOfWeek.equalsIgnoreCase("wednesday")) {
                    appointmentResultTv.setText(dayOfWeek);
                    String d = dateEditText.getText().toString();
                    getAppointmentCount(d);
                    if (appointmentCount < 2 ){
                        appointmentResultTv.setText("Date is Avaliable");
                        bookAppointment.setVisibility(View.VISIBLE);

                    }
                } else {
                    appointmentResultTv.setText("Please choose Sat Or Mon Or Tue");
                }

                /////////*********/////////////////////////////////////////

            }


        };

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AppointmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    bookAppointment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bookNewAppointment(dateEditText.getText().toString());
        }
    });




    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));


    }

    private void getAppointmentCount(String date) {
        String fixdate = date.replace("/","");
        firebaseStore.collection("appointment_count").document(fixdate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        appointmentCount = (int) document.get("count");

                    } else {
                        Log.d(TAG, "No such document");
                        //   Map<String, Object> names
                        //   document.getReference().set();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }
    private void icreamentAppointmentCount(String date){
        appointmentCount++;
        String fixdate = date.replace("/","");
        Map<String,Object> counts = new HashMap<>();
        counts.put("count",appointmentCount);
        firebaseStore.collection("appointment_count").document(fixdate).set(counts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Appointments Count Updated");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    private void bookNewAppointment(final String mdate){
        appointment = new Appointment();
        appointment.setDate(mdate);
        appointment.setPatientId(user.getUid());
        appointment.setId(String.valueOf(appointmentCount + 1));
        firebaseStore.collection("appointments").document().set(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "New Appointment Added ");
                        icreamentAppointmentCount(mdate);
                        appointmentTimeTv.setText("" + appointmentCount);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}

