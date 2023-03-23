package com.example.bloodoor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bloodoor.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.alterac.blurkit.BlurLayout;

public class verifyotp_User extends AppCompatActivity {

    EditText inputnumber1, inputnumber2, inputnumber3, inputnumber4, inputnumber5, inputnumber6;
    String getotpbackend;
    CardView verify_card;
    BlurLayout blurLayout1;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference,ref;
    private ProgressDialog loadingBar;
    String userID;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verifyotp_user);
        User user = (User) getIntent().getSerializableExtra("User");
        final Button verifybuttononclick = findViewById(R.id.buttongetotp);

        verify_card = (CardView) findViewById(R.id.verify_card);
        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2 = findViewById(R.id.inputotp2);
        inputnumber3 = findViewById(R.id.inputotp3);
        inputnumber4 = findViewById(R.id.inputotp4);
        inputnumber5 = findViewById(R.id.inputotp5);
        inputnumber6 = findViewById(R.id.inputotp6);
        blurLayout1 = findViewById(R.id.blurLayout);
        loadingBar = new ProgressDialog(this);
        String from_intent = getIntent().getStringExtra("from");

        TextView textView = findViewById(R.id.textshowmobilenumber);
        textView.setText(String.format(
                "+91 %s", getIntent().getStringExtra("mobile")
        ));

        getotpbackend = getIntent().getStringExtra("backendotp");


        verify_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty() && !inputnumber3.getText().toString().trim().isEmpty() && !inputnumber4.getText().toString().trim().isEmpty() && !inputnumber5.getText().toString().trim().isEmpty() && !inputnumber6.getText().toString().trim().isEmpty()) {
                    String entercodeotp = inputnumber1.getText().toString() +
                            inputnumber2.getText().toString() +
                            inputnumber3.getText().toString() +
                            inputnumber4.getText().toString() +
                            inputnumber5.getText().toString() +
                            inputnumber6.getText().toString();

                    if (getotpbackend != null) {
//                        loadingBar.setTitle("Logging in");
                        loadingBar.setMessage("Verifying OTP");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getotpbackend, entercodeotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            rootNode = FirebaseDatabase.getInstance();
                                            FirebaseUser mauth = FirebaseAuth.getInstance().getCurrentUser();
                                            userID = mauth.getUid();
                                            ref=rootNode.getReference("allusers");
                                            if(from_intent.equals("SignUp_User"))
                                            {

                                                reference = rootNode.getReference("users");

                                                String Bloodgrp = user.getBloodgrp();
                                                String pin_code = user.getPinCode();
                                                reference.child(Bloodgrp).child(pin_code).child(userID).setValue(user);
                                                ref.child(userID).setValue(user);
//                                                Intent intent = new Intent(getApplicationContext(), Homepage_user.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                loadingBar.dismiss();
//                                                Toast.makeText(verifyotp_User.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//                                                startActivity(intent);
                                                Intent intent = new Intent(getApplicationContext(), Homepage_user.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                loadingBar.dismiss();
                                                Toast.makeText(verifyotp_User.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot snapshot1:snapshot.getChildren())
                                                        {
                                                            if(snapshot1.getKey().equals(userID))
                                                            {
                                                                i=1;
                                                            }
                                                        }
                                                        if(i==0) {
                                                            Toast.makeText(verifyotp_User.this, "User not registered", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), SignUp_User.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            loadingBar.dismiss();
                                                            startActivity(intent);
                                                        }
                                                        else
                                                        {
                                                            Intent intent = new Intent(getApplicationContext(), Homepage_user.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            loadingBar.dismiss();
                                                            Toast.makeText(verifyotp_User.this, "Logged in successfully+ 11", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }


                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(verifyotp_User.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(verifyotp_User.this, "Network error:(", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(verifyotp.this, "OTP verify...",Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(verifyotp_User.this, "Please enter all numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();

        TextView resendlabel = findViewById(R.id.textresendotp);

        resendlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(verifyotp_User.this, "Resending the OTP... Plesse wait...", Toast.LENGTH_SHORT).show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        90,
                        TimeUnit.SECONDS,
                        verifyotp_User.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(verifyotp_User.this, "Network Error:(", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getotpbackend = newbackendotp;
                                Toast.makeText(verifyotp_User.this, "OTP sent successfully:)", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    private void numberotpmove() {
        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}