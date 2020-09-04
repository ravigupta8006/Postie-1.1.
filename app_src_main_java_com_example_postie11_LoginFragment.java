package com.example.postie11;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.postie11.CreateAccountFragment.VALID_EMAIL_ADDRESS_REGEX;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    private EditText emailORphone, password;
    private Button loginBtn;
    private TextView createAccountTV, forgotPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailORphone.setError(null);
                password.setError(null);
                if (emailORphone.getText().toString().isEmpty()) {
                    emailORphone.setError("Required");
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Required");
                    return;
                }

                if (VALID_EMAIL_ADDRESS_REGEX.matcher(emailORphone.getText().toString()).find()) {

                    login(emailORphone.getText().toString());

                }else{

                    emailORphone.setError("Please enter a valid Email");

                }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new ForgotPasswordFragment());
            }
        });

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new CreateAccountFragment());
            }
        });
    }

    private void init(View view) {
        emailORphone = view.findViewById(R.id.email_or_phone);
        password = view.findViewById(R.id.password);
        loginBtn = view.findViewById(R.id.login_btn);
        createAccountTV = view.findViewById(R.id.create_account_text);

        forgotPassword = view.findViewById(R.id.forgot_password);


    }

    private void login(final String email){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(getContext(),MainActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}