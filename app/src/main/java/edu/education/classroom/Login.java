package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button LoginButton;
    private Button RegisterButton;
    private CircleImageView imageView;
    private Dialog dialog;

    private String JSON_URL = "http://192.168.43.89/Classroom/php%20Codes/login.php";
    private String actualPassword;
    private String uUsername;
    private String uPassword;
    private String imageUrl;
    private String userEmail;
    private boolean flag = false;

    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray array;
    private JSONObject object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.profilePic);
        userName = findViewById(R.id.usernameId);
        password = findViewById(R.id.passwordId);
        LoginButton = findViewById(R.id.loginId);
        RegisterButton = findViewById(R.id.registerId);

        password.setVisibility(View.GONE);

        flag = false;

        dialog = new Dialog(Login.this);
        dialog.setContentView(R.layout.progressdialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag)
                {
                    Intent intent = new Intent(Login.this,Login.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(Login.this,Register.class);
                    startActivity(intent);
                }
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag)
                {
                    uPassword = password.getText().toString();
                    if (uPassword.equals(actualPassword))
                    {
                        Intent intent = new Intent(Login.this,Home.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId",userEmail);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        password.setError("Please enter a valid password");
                        password.requestFocus();
                    }
                }
                else {

                    uUsername = userName.getText().toString();
                    queue = Volley.newRequestQueue(Login.this);
                    request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        array = response.getJSONArray("users");

                                        for (int i=0;i<array.length();i++)
                                        {
                                            object = array.getJSONObject(i);

                                            dialog.show();

                                            if (object.getString("username").equals(uUsername) || object.getString("email").equals(uUsername))
                                            {
                                                actualPassword = object.getString("password");
                                                imageUrl = object.getString("picurl");
                                                Picasso.get().load(imageUrl).into(imageView);
                                                userName.setEnabled(false);
                                                password.setVisibility(View.VISIBLE);
                                                LoginButton.setText("Login");
                                                RegisterButton.setText("Go Back");
                                                RegisterButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.back_icon,0,0,0);
                                                userEmail = object.getString("email");
                                                flag = true;
                                            }
                                        }
                                        dialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    queue.add(request);
                }
            }
        });
    }

}
