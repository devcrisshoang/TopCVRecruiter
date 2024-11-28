package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String phoneNumber;
    private String verificationCode;
    private String rawPhoneNumber;

    Long timeoutSeconds = 60L;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private EditText editTextCode;

    private Button buttonVerify;

    private TextView textViewTitle;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        setWidget();

        setClick();

    }

    private void verifyButton(){
        String enteredOtp = editTextCode.getText().toString();
        if (!TextUtils.isEmpty(enteredOtp)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
        } else {
            Toast.makeText(VerifyPhoneActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClick(){
        buttonVerify.setOnClickListener(v -> verifyButton());
    }

    private void setWidget(){

        editTextCode = findViewById(R.id.editTextCode);
        buttonVerify = findViewById(R.id.buttonVerify);
        textViewTitle = findViewById(R.id.textViewTitle);

        rawPhoneNumber = getIntent().getStringExtra("phone");

        phoneNumber = formatPhoneNumber(rawPhoneNumber);

        if (TextUtils.isEmpty(rawPhoneNumber) || TextUtils.isEmpty(phoneNumber)) {
            Log.e("VerifyPhoneActivity","Invalid phone number");
            finish();
            return;
        }

        sendOtp(phoneNumber, false);
    }

    void sendOtp(String phoneNumber, boolean isResend) {
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "OTP verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Toast.makeText(getApplicationContext(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                            }
                        });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            buttonVerify.setEnabled(false);
            buttonVerify.setText("Verifying...");
        } else {
            buttonVerify.setEnabled(true);
            buttonVerify.setText("Verify");
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatPhoneNumber(String rawPhoneNumber) {

        if (rawPhoneNumber != null) {
            rawPhoneNumber = rawPhoneNumber.trim();
            if (rawPhoneNumber.startsWith("0")) {
                return "+84" + rawPhoneNumber.substring(1);
            } else if (rawPhoneNumber.startsWith("+84")) {
                return rawPhoneNumber;
            }
        }
        return rawPhoneNumber;
    }
}
