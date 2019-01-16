package com.app.barber.ui.preauth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.app.barber.R;
import com.app.barber.core.BaseActivity;
import com.app.barber.util.CommonUtils;
import com.app.barber.util.GlobalValues;
import com.app.barber.views.CustomEditText;
import com.app.barber.views.CustomTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 29/10/18.
 */

public class VerifyMobileActivity extends BaseActivity {

    @BindView(R.id.otp_edtxt)
    CustomEditText otpEdtxt;
    @BindView(R.id.resend_otp)
    CustomTextView resendOtp;
    @BindView(R.id.btn_verify)
    CustomTextView btnVerify;
    private String enteredNumber;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String countryCode;
    private String fromActivity;

    @Override
    public int getLayoutId() {
        return R.layout.layout_verify_mobile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing objects
        getIntentData(getIntent());

    }


    private void getIntentData(Intent intent) {
        fromActivity = intent.getStringExtra(GlobalValues.Extras.FROM);
        enteredNumber = intent.getStringExtra(GlobalValues.Extras.ADD_MOBILE);
        countryCode = intent.getStringExtra(GlobalValues.Extras.COUNTRY_CODE);
//        atNumber.setText("at " + countryCode + enteredNumber);
        otprequest();
    }

    private void otprequest() {
        mPref.setPrefrencesBoolean(GlobalValues.Extras.VERIFIED, false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" + countryCode + enteredNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        Toast.makeText(VerifyMobileActivity.this, R.string.error_please_wait, Toast.LENGTH_SHORT).show();
    }

    private String mVerificationId;
    private String codeReceived;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code 
            if (code != null) {
//                editTextCode.setText(code);
                //verifying the code
                codeReceived = code;
//                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyMobileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String otp) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            //signing the user
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            goNext();
            new CommonUtils().displayMessage(VerifyMobileActivity.this, "Error");
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(VerifyMobileActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(VerifyMobileActivity.this, ProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                            new CommonUtils().displayMessage(VerifyMobileActivity.this, "Verified successfully");
                            goNext();
                        } else {
                            goNext();
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                new CommonUtils().displayMessage(VerifyMobileActivity.this, message);
                            }
                            /*new CommonUtils().displayMessage(VerifyMobileActivity.this, "Verified successfully");
                            Intent intent = new Intent();
                            intent.putExtra(GlobalValues.Extras.ADD_MOBILE, enteredNumber);git add -A
                            mPref.setPrefrencesBoolean(GlobalValues.Extras.VERIFIED, true);
                            setResult(RESULT_OK, intent);
                            finish();*/
                        }
                    }
                });
    }

    private void goNext() {
       /* if (fromActivity.equals(LoginSignupActivity.class.getName())) {
            Intent intent = new Intent();
            intent.putExtra(GlobalValues.Extras.ADD_MOBILE, enteredNumber);
            intent.putExtra(GlobalValues.Extras.VERIFIED, true);
            mPref.setPrefrencesBoolean(GlobalValues.Extras.VERIFIED, true);
            setResult(RESULT_OK, intent);
        } else {
            activitySwitcher(VerifyMobileActivity.this, ChangePasswordActivity.class, null);
        }
        finish();*/
    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(GlobalValues.Extras.ADD_MOBILE, "");
        setResult(RESULT_OK, intent);
        finish();
    }*/

    private void verifyOtp() {
        if (validatefields()) {
            String code = otpEdtxt.getText().toString();
            verifyVerificationCode(code);
        }
    }

    private boolean validatefields() {
        if (CommonUtils.isEmpty(otpEdtxt)) {
            CommonUtils.getInstance(VerifyMobileActivity.this).
                    displayMessage(VerifyMobileActivity.this,
                            VerifyMobileActivity.this.getString(R.string.error_otp));
            return false;
        } else
            return true;
    }

    @OnClick({R.id.resend_otp, R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resend_otp:
//              activitySwitcherResult(VerifyMobileActivity.this, AddMobileNumberActivity.class, null, GlobalValues.RequestCodes.ADD_MOBILE);
                break;
            case R.id.btn_verify:
                verifyOtp();
                break;
        }
    }
}
