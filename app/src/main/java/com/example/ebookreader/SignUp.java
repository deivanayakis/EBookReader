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

import com.example.ebookreader.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    DatabaseHelper dbhelper;
    private TextView loginRedirectText;
    private Button signupbutton;
    private EditText et1,et2,et3,et4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });

        dbhelper = new DatabaseHelper(this);

        signupbutton = findViewById(R.id.signup_button);
        et1 = findViewById(R.id.signup_name);
        et2 = findViewById(R.id.signup_mail);
        et3 = findViewById(R.id.signup_pwd);
        et4 = findViewById(R.id.signup_cpwd);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = et1.getText().toString();
                String email = et2.getText().toString();
                String password = et3.getText().toString();
                String cpwd = et4.getText().toString();

                if(name.equals("") || password.equals("") || email.equals("") || cpwd.equals(""))
                {
                    Toast.makeText(SignUp.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.equals(cpwd))
                    {
                        Boolean checkUserEmail = dbhelper.checkEmail(email);

                        if(checkUserEmail==false)
                        {
                            Boolean insert = dbhelper.insertData(name,email,password);

                            if(insert==true)
                            {
                                Toast.makeText(SignUp.this,"Registered Successfully!!!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(SignUp.this,"Register Failed!!!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUp.this,"User already exists!! Please login",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignUp.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}