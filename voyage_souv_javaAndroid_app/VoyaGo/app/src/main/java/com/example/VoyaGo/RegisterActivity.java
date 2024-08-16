package com.example.VoyaGo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.VoyaGo.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText name, email, password;
    Button signup;
    TextView signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        name = (TextInputEditText) findViewById(R.id.nom);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (TextView) findViewById(R.id.signin);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nom = Objects.requireNonNull(name.getText()).toString();
                String ad_email = Objects.requireNonNull(email.getText()).toString();
                String pass = Objects.requireNonNull(password.getText()).toString();

                if(ad_email.isEmpty() || pass.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkemail = DB.checkEmail(ad_email);
                    if(!checkemail){
                        Boolean insert = DB.insertData(nom, ad_email, pass);
                        if(insert){
                            Toast.makeText(RegisterActivity.this, "L'inscription est réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "l'inscription ne réussie pas'", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}