package com.adirar.bookdoctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adirar.bookdoctor.data.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user;

    TextView textView;
    Button registerBtn;
    Button addAppointmentBtn;
    Button signoutBtn;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        registerBtn = (Button) findViewById(R.id.register_btn);
        addAppointmentBtn = (Button) findViewById(R.id.appointment_btn);
        signoutBtn = (Button) findViewById(R.id.sign_out_btn);


     //Add Appointment Button Action
        addAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AppointmentActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
            }
        });


      //Register Button Action
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                mUser = new User();
                mUser.setName(user.getDisplayName());
                mUser.setPhone(user.getPhoneNumber());
                mUser.setEmail(user.getEmail());
                **/
                 Intent  intent = new Intent(MainActivity.this,RegisterActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
            }
        });



        //Signing Out
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // User is signed in
                    Log.i(TAG,"Already Sign In");
                    mUser = setmUser(user);
                  //  intialSignIN(user.getDisplayName());
                    Toast.makeText(MainActivity.this,
                            "You're now signed in. Welcome",
                            Toast.LENGTH_SHORT).show();

                }else {
                    // user is sign out
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
           // IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i(TAG,"Sign In Request");
            if (resultCode == RESULT_OK) {
                Log.i(TAG,"Logged in Successfully");
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                textView.setText(user.getDisplayName());
                mUser = setmUser(user);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG,"Faild to Login");
                textView.setText("Wrong logging");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"ON Resume");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"On Pause");
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private User setmUser(FirebaseUser firebaseUser){
        User temp = new User();
        temp.setName(user.getDisplayName());
        temp.setPhone(user.getPhoneNumber());
        temp.setEmail(user.getEmail());
        temp.setUid(firebaseUser.getUid());
        return temp;
    }
}
