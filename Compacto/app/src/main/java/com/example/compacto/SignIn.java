package com.example.compacto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    EditText email_signIn, pass_signIn;
    Button signin;
    TextView forgotpass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth=FirebaseAuth.getInstance();


        email_signIn=findViewById(R.id.email_signIn);;
        pass_signIn=findViewById(R.id.pass_signIn);
        signin=findViewById(R.id.signin);

        forgotpass=findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,ForgotPassword.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                userLogin();
            }
        });



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
    private void userLogin()
    {
        String email=email_signIn.getText().toString().trim();
        String password=pass_signIn.getText().toString().trim();

        if (email.isEmpty())
        {
            email_signIn.setError("Email is required!");
            email_signIn.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_signIn.setError("Please enter valid email!");
            email_signIn.requestFocus();
            return;

        }
        if(password.isEmpty())
        {
            pass_signIn.setError("Password is required!");
            pass_signIn.requestFocus();
            return;
        }
        if (password.length()<6)
        {
            pass_signIn.setError("Minimum 6 characters required!");
            pass_signIn.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified())
                    {
                        //redirect to user profile
                        startActivity(new Intent(SignIn.this,MainHomeScreen.class));
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(SignIn.this,"Check your email to verify your account!",Toast.LENGTH_LONG).show();
                    }


                }
                else
                {
                    Toast.makeText(SignIn.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}