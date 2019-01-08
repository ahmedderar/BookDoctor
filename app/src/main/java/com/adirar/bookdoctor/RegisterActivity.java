package com.adirar.bookdoctor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.adirar.bookdoctor.data.User;

public class RegisterActivity extends AppCompatActivity {
    EditText name;
    EditText phone;

     User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       user = (User) getIntent().getSerializableExtra("user");

        name = (EditText) findViewById(R.id.name_et);
        phone = (EditText) findViewById(R.id.phone_et);

        name.setText(user.getName());
        phone.setText(user.getPhone());
    }
}
