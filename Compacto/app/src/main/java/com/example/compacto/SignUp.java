package com.example.compacto;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText name, email_adr,mobnum,password;
    ImageView user_logo;
    Button register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        name=findViewById(R.id.name);
        email_adr=findViewById(R.id.email_adr);
        mobnum=findViewById(R.id.mobnum);
        password=findViewById(R.id.password);
        user_logo=findViewById(R.id.user_logo);
        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void registerUser()
    {
        String email=email_adr.getText().toString().trim();
        String pass=password.getText().toString().trim();
        String fullname=name.getText().toString().trim();
        String mobnumber=mobnum.getText().toString().trim();

        if(fullname.isEmpty())
        {
            name.setError("Full name is required");
            name.requestFocus();
            return;
        }
        if(mobnumber.isEmpty())
        {
            mobnum.setError("Mobile Number is required");
            mobnum.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            email_adr.setError("Email is required");
            email_adr.requestFocus();
            return;
        }
        if(! Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_adr.setError("Please provide valid email");
            email_adr.requestFocus();
            return;

        }
        if(pass.isEmpty())
        {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(pass.length() < 6)
        {
            password.setError("Minimum 6 characters required");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    User user=new User(fullname,mobnumber,email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUp.this,"User registration successful",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUp.this,"User registration failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(SignUp.this,"User registration failed",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}