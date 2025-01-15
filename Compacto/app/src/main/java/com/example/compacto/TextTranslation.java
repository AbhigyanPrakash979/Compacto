package com.example.compacto;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;

import java.util.ArrayList;
import java.util.Locale;

public class TextTranslation extends AppCompatActivity {

    private Spinner idFromSpinner, idToSpinner;
    private ImageView arrow, mic;
    private TextInputEditText sourceEdt;
    private TextView or, speak, havepatience;
    private Button go;
    private ImageView imageView6;
    private TextView textView3;

    private static final int REQUEST_PERMISSION_CODE = 1;
    private String fromlanguageCode, tolanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_translation);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView6 = findViewById(R.id.imageView6);
        textView3 = findViewById(R.id.textView3);
        idFromSpinner = findViewById(R.id.idFromSpinner);
        idToSpinner = findViewById(R.id.idToSpinner);
        arrow = findViewById(R.id.arrow);
        mic = findViewById(R.id.mic);
        sourceEdt = findViewById(R.id.idEditSource);
        or = findViewById(R.id.or);
        speak = findViewById(R.id.speak);
        havepatience = findViewById(R.id.havepatience);
        go = findViewById(R.id.go);

        setupSpinners();
        setupGoButton();
        setupMicButton();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void setupSpinners() {
        String[] fromLanguages = {"From", "English", "Afrikaans", "Arabic", "Belarusian", "Bengali", "Catalan", "Czech", "Welsh", "Hindi", "Urdu"};
        String[] toLanguages = {"To", "English", "Afrikaans", "Arabic", "Belarusian", "Bengali", "Catalan", "Czech", "Welsh", "Hindi", "Urdu"};

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idFromSpinner.setAdapter(fromAdapter);
        idFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromlanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idToSpinner.setAdapter(toAdapter);
        idToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tolanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupGoButton() {
        go.setOnClickListener(v -> {
            havepatience.setText("");
            if (sourceEdt.getText().toString().isEmpty()) {
                Toast.makeText(TextTranslation.this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
            } else if (fromlanguageCode == null) {
                Toast.makeText(TextTranslation.this, "Please select a source language", Toast.LENGTH_SHORT).show();
            } else if (tolanguageCode == null) {
                Toast.makeText(TextTranslation.this, "Please select a target language", Toast.LENGTH_SHORT).show();
            } else {
                translateText(fromlanguageCode, tolanguageCode, sourceEdt.getText().toString());
            }
        });
    }

    private void setupMicButton() {
        mic.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into Text");
            try {
                startActivityForResult(i, REQUEST_PERMISSION_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TextTranslation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            sourceEdt.setText(result.get(0));
        }
    }

    private void translateText(String fromLanguageCode, String toLanguageCode, String source) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    translator.translate(source)
                            .addOnSuccessListener(translatedText -> havepatience.setText(translatedText))
                            .addOnFailureListener(e -> Toast.makeText(TextTranslation.this, "Translation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(TextTranslation.this, "Model download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getLanguageCode(String language) {
        switch (language) {
            case "English": return TranslateLanguage.ENGLISH;
            case "Afrikaans": return TranslateLanguage.AFRIKAANS;
            case "Arabic": return TranslateLanguage.ARABIC;
            case "Belarusian": return TranslateLanguage.BELARUSIAN;
            case "Bengali": return TranslateLanguage.BENGALI;
            case "Catalan": return TranslateLanguage.CATALAN;
            case "Czech": return TranslateLanguage.CZECH;
            case "Welsh": return TranslateLanguage.WELSH;
            case "Hindi": return TranslateLanguage.HINDI;
            case "Urdu": return TranslateLanguage.URDU;
            default: return null;
        }
    }
}
