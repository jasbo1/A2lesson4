package com.e.lesson_4.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.e.lesson_4.MainActivity;
import com.e.lesson_4.R;
import com.e.lesson_4.Toaster;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.e.lesson_4.Toaster.show;

public class PhoneActivity extends AppCompatActivity {
    EditText editCode;
    EditText editPhone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editCode = findViewById(R.id.editCode);
        editPhone = findViewById(R.id.editPhone);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                Log.e("TAG", "onVerificationCompleted");

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("TAG", "onVerificationFailed");

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                            Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PhoneActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public void onClick(View view) {
        String countryCode = editCode.getText().toString().trim();
        String phone =countryCode + editPhone.getText().toString().trim();
        Log.d("TAG", "onClick: " + view);
        // Toast.makeText(PhoneActivity.this, "Back to button", Toast.LENGTH_SHORT);
        if (TextUtils.isEmpty(phone)) {
            editPhone.setError("Укажите номер телефона");
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);


    }
}
