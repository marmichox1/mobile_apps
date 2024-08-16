package com.example.VoyaGo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.VoyaGo.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email, password;
    Button signin;
    TextView signup;
    DBHelper DB;
    Spinner spinner;
    public static final String[] languages = {"SELECT A LANGUAGE", "ENGLISH", "FRANCAIS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.btnsignin);
        signup = (TextView) findViewById(R.id.signup);
        DB = new DBHelper(this);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();

                if (selectedLang.equals("ENGLISH")){
                    LocaleHelper.setLocal(MainActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                }else if (selectedLang.equals("FRANCAIS")){
                    LocaleHelper.setLocal(MainActivity.this, "fr");
                    finish();
                    startActivity(getIntent());                }
            }

            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LocaleHelper.setLocal(MainActivity.this, "fr");
                finish();
                startActivity(getIntent());
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {


                String ad_email = Objects.requireNonNull(email.getText()).toString();
                String pass = Objects.requireNonNull(password.getText()).toString();

                if(ad_email.isEmpty() || pass.isEmpty())
                    Toast.makeText(MainActivity.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                else{

                    Boolean checkuserpass = DB.checkEmailPassword(ad_email , pass);
                    if(checkuserpass){
                        Toast.makeText(MainActivity.this, "La connexion est réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("email", ad_email);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "les informations d'identification invalides", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}