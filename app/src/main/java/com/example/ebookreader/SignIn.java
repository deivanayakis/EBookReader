package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignIn extends AppCompatActivity {

    private TextView registerRedirectText;
    private EditText et1,et2;
    Button signinbutton;
    DatabaseHelper dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        et1 = findViewById(R.id.signin_mail);
        et2 = findViewById(R.id.signin_pwd);
        signinbutton = findViewById(R.id.signin_button);

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });

        dbhelper = new DatabaseHelper(this);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et1.getText().toString();
                String password = et2.getText().toString();

                if (email.equals("") || password.equals(""))
                {
                    Toast.makeText(SignIn.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkCredentials = dbhelper.checkEmailPassword(email,password);

                    if(checkCredentials==true)
                    {
                        Toast.makeText(SignIn.this,"Login Successfully!!!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, DashBoard.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(SignIn.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    }
}