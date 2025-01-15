package com.example.compacto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainHomeScreen extends AppCompatActivity {
    ImageView speechrecognise_image,textextract_image, texttranslate_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_home_screen);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        speechrecognise_image=findViewById(R.id.speechrecognise_image);
        speechrecognise_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainHomeScreen.this,SpeechRecognition.class));
            }
        });


        textextract_image=findViewById(R.id.textextract_image);
        textextract_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainHomeScreen.this, TextExtraction.class));
            }
        });


        texttranslate_image=findViewById(R.id.texttranslate_image);
        texttranslate_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent launchIntent=getPackageManager().getLaunchIntentForPackage("com.example.texttranslation");
//                startActivity(launchIntent);
                startActivity(new Intent(MainHomeScreen.this, TextTranslation.class));

            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}