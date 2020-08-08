package edu.education.classroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    private EditText UserName;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText UserMail;
    private Button signUp;
    private Button UploadImage;
    private CircleImageView imageView;
    private Bitmap bitmap;

    private String UPLOAD_URl = "http://192.168.43.90/Classroom/php%20Codes/register.php";
    private String username;
    private String password;
    private String confirmPassword;
    private String usermail;
    private int REQUEST_CODE = 1;
    private boolean isPicUploaded = false;

    private RequestQueue queue;
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = findViewById(R.id.usernameId);
        UserMail = findViewById(R.id.userMailId);
        Password = findViewById(R.id.passwordId);
        ConfirmPassword = findViewById(R.id.confirmPaswordId);
        signUp = findViewById(R.id.signupId);
        UploadImage = findViewById(R.id.uploader);
        imageView = findViewById(R.id.profilePic);

        imageView.setVisibility(View.GONE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermail = UserMail.getText().toString();
                username = UserName.getText().toString();
                password = Password.getText().toString();
                confirmPassword = ConfirmPassword.getText().toString();

                if (usermail.isEmpty())
                {
                    UserMail.setError("Please enter a valid Email Id");
                }
                if (username.isEmpty()) {
                    UserName.setError("Please enter a UserName");
                }
                if (password.isEmpty())
                {
                    Password.setError("PLease enter a valid Password");
                }
                if (confirmPassword.isEmpty()) {
                    ConfirmPassword.setError("Please confirm your password");
                }
                if (!isPicUploaded)
                {
                    Toast.makeText(Register.this,"Please upload a Picture",Toast.LENGTH_SHORT).show();
                }

                if (!usermail.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && isPicUploaded)
                {
                    if (password.equals(confirmPassword))
                    {
                        UploadCredentials(usermail,username,password,bitmap);
                    }
                    else {
                        Password.setError("Password Mismatch");
                        Password.requestFocus();
                    }
                }
            }
        });

        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void UploadCredentials(final String usermail, final String username, final String password, final Bitmap bitmap) {

        queue = Volley.newRequestQueue(Register.this);

        request = new StringRequest(Request.Method.POST, UPLOAD_URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("status");

                            if (result.equals("success"))
                            {
                                Intent intent = new Intent(Register.this,Login.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(Register.this,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("mailid",usermail);
                params.put("password",password);
                params.put("image",encodeImage(bitmap));
                return params;
            }
            
        };

        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                isPicUploaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imgByted = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByted,Base64.DEFAULT);
    }
}
