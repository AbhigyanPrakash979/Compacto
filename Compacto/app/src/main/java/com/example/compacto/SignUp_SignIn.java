package com.example.compacto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp_SignIn extends AppCompatActivity {
    ImageView user_logo;
    TextView sign_up, or, sign_in, already_an_user;
    View divider, divider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_sign_in);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        user_logo=findViewById(R.id.user_logo);
        sign_up=findViewById(R.id.resetwithEmail);
        or=findViewById(R.id.or);
        sign_in=findViewById(R.id.sign_in);
        already_an_user=findViewById(R.id.already_an_user);
        divider=findViewById(R.id.divider);
        divider2=findViewById(R.id.divider2);


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_up=new Intent(SignUp_SignIn.this,SignUp.class);
                startActivity(sign_up);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_signIn =new Intent(SignUp_SignIn.this,SignIn.class);
                startActivity(sign_signIn);
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}