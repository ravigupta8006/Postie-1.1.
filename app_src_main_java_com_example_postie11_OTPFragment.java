package com.example.postie11;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class OTPFragment extends Fragment {


    public OTPFragment() {
        // Required empty public constructor
    }

    public OTPFragment(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    private EditText otp;
    private TextView tvphone;
    private Button resendBtn;
    private Button verifyBtn;
    private String email, phone, password;
    private Timer timer;
    private int count = 60;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        firebaseAuth = FirebaseAuth.getInstance();

        tvphone.setText("verfication code has sent to +91"+phone);

        sendOTP();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count==0){
                            resendBtn.setText("RESEND");
                            resendBtn.setEnabled(true);
                            resendBtn.setAlpha(1f);
                        }else{
                            //resendBtn.setText("Resend in "+count);
                            count--;
                        }
                    }
                });
            }
        },0,1000);

        //resendBtn.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View v) {
                //resendOTP();

                //resendBtn.setEnabled(false);
                //resendBtn.setAlpha(0.5f);
               // count=60;
           // }
        //});
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText()==null||otp.toString().isEmpty()){
                    return;
                }
                otp.setError(null);
                String code = otp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);

            }
        });

    }
    private void init(View view) {
        otp = view.findViewById(R.id.otp);
        verifyBtn = view.findViewById(R.id.verify_btn);
        resendBtn = view.findViewById(R.id.reset_btn);
        tvphone = view.findViewById(R.id.tv_phone);
    }

    private void sendOTP(){
        mCallback =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    otp.setError(e.getMessage());
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    otp.setError(e.getMessage());
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        } ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallback,mResendToken);        // OnVerificationStateChangedCallbacks
    }
    private void resendOTP(){

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            user.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        Map<String,Object> map =new HashMap<>();
                                        map.put("email",email);
                                        map.put("phone",phone);
                                        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    Intent mainIntent = new Intent(getContext(),UsernameActivity.class);
                                                    startActivity(mainIntent);
                                                    getActivity().finish();

                                                }else{
                                                    String error= task.getException().getMessage();
                                                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });



                                       //Intent mainIntent = new Intent(getContext(),MainActivity.class);
                                       // startActivity(mainIntent);
                                        //getActivity().finish();

                                    }else{
                                        String error= task.getException().getMessage();
                                        Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                otp.setError("Invalid OTP");
                            }
                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


}
