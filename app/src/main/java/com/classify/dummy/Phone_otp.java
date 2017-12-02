package com.classify.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Phone_otp extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1,e2;
    Button btn1,btn2;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code,email;
    DatabaseReference mDatabaseUsers;
    int no;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp);
        e1 = (EditText)findViewById(R.id.otpNO);
        e2 = (EditText)findViewById(R.id.code);
        btn1 = (Button)findViewById(R.id.send);
        btn2 = (Button)findViewById(R.id.verify);
        auth = FirebaseAuth.getInstance();

        no = getIntent().getExtras().getInt("total");
//        email = getIntent().getExtras().getString("email");
//        Log.d("phonee",email);

       /* num = getIntent().getExtras().getInt("phone");
        if(num!=0)
        {
            e1.setText(num);

        }
*/
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUsers.keepSynced(true);

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                Toast.makeText(Phone_otp.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void sendSms(View v)
    {
        String number = e1.getText().toString();
        mDatabaseUsers.child(no + "").child("Phone").setValue(number);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback
        );
    }
    public void signInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("123321","hio");
                if (task.isSuccessful()) {
//                    Log.d("123321",email);
                    Intent i = new Intent(Phone_otp.this,MainActivity.class);
//                    i.putExtra("emails", email);
                    startActivity(i);
                } else
                {
                    Log.d("123321","hi1");
                    Toast.makeText(Phone_otp.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verify_num(View v)
    {
        String code = e2.getText().toString();
        //if(verification_code!=null)
        ///{
            verifyPhoneNumber(verification_code,code);
        //}
    }

    private void verifyPhoneNumber(String verification_code, String code)
    {
        Log.d("123321","ver");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code,code);
        signInWithPhone(credential);
    }

}
