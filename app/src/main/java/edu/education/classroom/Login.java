package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button LoginButton;
    private Button RegisterButton;
    private CircleImageView imageView;

    private String actualPassword;
    private String uUsername;
    private String uPassword;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.usernameId);
        password = findViewById(R.id.passwordId);
        LoginButton = findViewById(R.id.loginId);
        RegisterButton = findViewById(R.id.registerId);
        imageView = findViewById(R.id.profilePic);

        password.setVisibility(View.GONE);
        //imageView.setVisibility(View.GONE);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {

                    uUsername = userName.getText().toString();

                    if (uUsername.isEmpty())
                    {
                        userName.setError("Please enter Username/Email");
                        userName.requestFocus();
                    }
                    else {
                        if (isValidUser(uUsername))
                        {
                            flag = true;
                            password.setVisibility(View.VISIBLE);
                            LoginButton.setText("Login");
                            RegisterButton.setText("Go Back");
                            imageView.setVisibility(View.VISIBLE);
                            Picasso.get().load("http://192.168.43.89/Classroom/php%20Codes/data/sunil@student.tce.edu.jpg").into(imageView);
                            //userName.setEnabled(false);
                        }
                    }
                }
                else {
                    uPassword = password.getText().toString();

                    if (uPassword.isEmpty())
                    {
                        password.setError("Please enter a valid password");
                        password.requestFocus();
                    }
                    else {
                        if (uPassword.equals(actualPassword))
                        {
                            Toast.makeText(Login.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                            password.setError("Invalid Password");
                            password.requestFocus();
                        }
                    }
                }
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag)
                {
                    Toast.makeText(Login.this,"Registration",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,Register.class);
                    startActivity(intent);
                }
                else {
                    flag = false;
                    password.setVisibility(View.GONE);
                    //imageView.setVisibility(View.GONE);
                    RegisterButton.setText("Register Me");
                    Toast.makeText(Login.this,"Go back",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidUser(String uUsername) {

        if (uUsername.equals("Sunil Kumar"))
        {
            actualPassword = "Sunil";
            return true;
        }
        return false;
    }
}
