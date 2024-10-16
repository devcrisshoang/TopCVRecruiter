package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    String phoneNumber;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText editTextCode;
    Button buttonVerify;
    TextView textViewTitle;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        editTextCode = findViewById(R.id.editTextCode);
        buttonVerify = findViewById(R.id.buttonVerify);
        textViewTitle = findViewById(R.id.textViewTitle);

        // Nhận số điện thoại từ Intent
        String rawPhoneNumber = getIntent().getStringExtra("phone"); // Đảm bảo tên khóa đúng

        // Định dạng số điện thoại
        phoneNumber = formatPhoneNumber(rawPhoneNumber);

        // Kiểm tra hợp lệ của số điện thoại
        if (TextUtils.isEmpty(rawPhoneNumber) || TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu số điện thoại không hợp lệ
            return;
        }

        // Gửi OTP
        sendOtp(phoneNumber, false);

        buttonVerify.setOnClickListener(v -> {
            String enteredOtp = editTextCode.getText().toString();
            if (!TextUtils.isEmpty(enteredOtp)) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                signIn(credential);
            } else {
                Toast.makeText(VerifyPhoneActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
            }
        });
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
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String formatPhoneNumber(String rawPhoneNumber) {
        // Kiểm tra và định dạng số điện thoại
        if (rawPhoneNumber != null) {
            rawPhoneNumber = rawPhoneNumber.trim();
            if (rawPhoneNumber.startsWith("0")) {
                return "+84" + rawPhoneNumber.substring(1); // Chuyển đổi từ 0 sang +84
            } else if (rawPhoneNumber.startsWith("+84")) {
                return rawPhoneNumber; // Nếu đã có mã quốc gia
            }
        }
        return rawPhoneNumber; // Trả về số gốc nếu không hợp lệ
    }
}
