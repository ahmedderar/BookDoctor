package com.adirar.bookdoctor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppointmentActivity extends AppCompatActivity {
    final String TAG = "AppointmentActivity";
    Calendar myCalendar;
    EditText dateEditText;
    DatePickerDialog.OnDateSetListener date;
    TextView appointmentResultTv;
    TextView appointmentTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        appointmentResultTv = (TextView) findViewById(R.id.appointment_result);
        appointmentTimeTv = (TextView) findViewById(R.id.appointment_time);

        myCalendar = Calendar.getInstance();

        dateEditText= (EditText) findViewById(R.id.date_et);
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
                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                String dayOfWeek = weekdateformat.format(date);
                if(dayOfWeek.equalsIgnoreCase("saturday")||dayOfWeek.equalsIgnoreCase("monday")
                        ||dayOfWeek.equalsIgnoreCase("wednesday")){
                    appointmentResultTv.setText(dayOfWeek);
                }else {
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

    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));




    }

}
