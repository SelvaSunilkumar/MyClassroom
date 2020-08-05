package edu.education.classroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CreateClass extends AppCompatActivity {

    private EditText className;
    private EditText classRoom;
    private EditText classSection;
    private EditText classSubject;
    private Button createClass;

    private String JSON_URL = "http://192.168.43.89/Classroom/php%20Codes/create_class.php";
    private String Name;
    private String Room;
    private String Section;
    private String Subject;
    private String userId;
    private boolean flag = true;
    private String classId;
    private String classDate;
    private UUID uuid;

    private RequestQueue queue;
    private StringRequest request;
    private JSONObject object;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        className = findViewById(R.id.className);
        classRoom = findViewById(R.id.classRoom);
        classSection = findViewById(R.id.classSection);
        classSubject = findViewById(R.id.classSubject);
        createClass = findViewById(R.id.createClass);

        bundle = getIntent().getExtras();
        userId = bundle.getString("userId");

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = className.getText().toString();
                Room = classRoom.getText().toString();
                Section = classSection.getText().toString();
                Subject = classSubject.getText().toString();

                if (Name.isEmpty())
                    flag = false;

                if (flag)
                {
                    queue = Volley.newRequestQueue(CreateClass.this);
                    request = new StringRequest(Request.Method.POST, JSON_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        object = new JSONObject(response);
                                        String result = object.getString("status");

                                        if (result.equals("success"))
                                        {
                                            Intent intent = new Intent(CreateClass.this,Home.class);
                                            bundle = new Bundle();
                                            bundle.putString("userId",userId);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"Failed to create class, Please try again",Toast.LENGTH_SHORT).show();
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
                    }) {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            uuid = UUID.randomUUID();
                            classId = uuid.toString();

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            classDate = dateFormat.format(date);

                            Random random = new Random();
                            int backgroundNumber = random.nextInt(4);

                            params.put("classId", classId);
                            params.put("className", Name);
                            params.put("classRoom", Room);
                            params.put("classSection", Section);
                            params.put("classSubject", Subject);
                            params.put("classCreated", userId);
                            params.put("classStudents", userId);
                            params.put("classDate", classDate);
                            params.put("bgNo", String.valueOf(backgroundNumber));
                            return params;
                        }
                    };

                    queue.add(request);
                }

            }
        });
    }
}
