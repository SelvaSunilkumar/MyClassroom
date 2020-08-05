package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JoinClass extends AppCompatActivity {

    private String CLASS_AVAILABLE_URL = "http://192.168.43.89/Classroom/php%20Codes/check_class.php";
    private String CLASS_JOIN_URL = "http://192.168.43.89/Classroom/php%20Codes/join_class.php";

    private LinearLayout closeWindow;
    private Button joinClass;
    private EditText classCode;

    private String userClassCode;
    private String userId;

    private Bundle bundle;

    private RequestQueue classQueue;
    private StringRequest classRequest;
    private JSONArray classArray;
    private JSONObject classDetails;
    private String classResponse;
    private String CLASSCODE;
    private String CLASSNAME;
    private String CLASSSECTION;
    private String CLASSROOM;
    private String CLASSCREATED;
    private String CLASSSUBJECT;
    private String date;
    private int backgroundNumber;

    private RequestQueue joinQueue;
    private StringRequest joinRequest;
    private JSONObject classObject;
    private JSONObject object;
    private String joinResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        bundle = getIntent().getExtras();
        userId = bundle.getString("userId");

        closeWindow = findViewById(R.id.close);
        joinClass = findViewById(R.id.joinClass);
        classCode = findViewById(R.id.classCode);

        joinClass.setEnabled(false);

        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinClass.super.onBackPressed();
            }
        });

        classCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    joinClass.setEnabled(false);
                    joinClass.setTextColor(getResources().getColor(R.color.black));
                    joinClass.setBackgroundColor(getResources().getColor(R.color.button));
                }
                else {
                    joinClass.setEnabled(true);
                    joinClass.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    joinClass.setTextColor(getResources().getColor(R.color.white));

                    joinClass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //code here
                            userClassCode = classCode.getText().toString();
                            new CheckClass().execute(userClassCode,userId);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class CheckClass extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            userClassCode = strings[0];
            userId = strings[1];
            classQueue = Volley.newRequestQueue(JoinClass.this);
            classRequest = new StringRequest(Request.Method.POST, CLASS_AVAILABLE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                classObject = new JSONObject(response);
                                classArray = classObject.getJSONArray("class");
                                if (classArray.length() > 0)
                                {
                                    classDetails = classArray.getJSONObject(0);
                                    System.out.print("Data from JSON" + classDetails.getString("classId") + classDetails.getString("className"));
                                    CLASSCODE = classDetails.getString("classId");
                                    CLASSNAME = classDetails.getString("className");
                                    CLASSSECTION = classDetails.getString("classSection");
                                    CLASSROOM = classDetails.getString("room");
                                    CLASSSUBJECT = classDetails.getString("subject");
                                    CLASSCREATED = classDetails.getString("created");
                                    backgroundNumber = Integer.parseInt(classDetails.getString("bgNo"));
                                    Toast.makeText(getApplicationContext(),CLASSCODE + " " + CLASSNAME + " " + CLASSSECTION + " " + CLASSROOM + " " + CLASSSUBJECT + " " + CLASSCREATED,Toast.LENGTH_SHORT).show();
                                    new EnterClass().execute(CLASSCODE,CLASSNAME,CLASSSECTION,CLASSROOM,CLASSSUBJECT,CLASSCREATED,userId);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"No Class Found",Toast.LENGTH_SHORT).show();
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
                    params.put("classId",userClassCode);
                    return params;
                }
            };
            classQueue.add(classRequest);
            return null;
        }
    }

    class EnterClass extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            CLASSCODE = strings[0];
            CLASSNAME = strings[1];
            CLASSSECTION = strings[2];
            CLASSROOM = strings[3];
            CLASSSUBJECT = strings[4];
            CLASSCREATED = strings[5];
            userId = strings[6];

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = dateFormat.format(Calendar.getInstance().getTime());
            //Toast.makeText(getApplicationContext(),CLASSCODE + " " + CLASSNAME + " " + CLASSSECTION + " " + CLASSROOM + " " + CLASSSUBJECT + " " + CLASSCREATED,Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
            joinQueue = Volley.newRequestQueue(JoinClass.this);
            joinRequest = new StringRequest(Request.Method.POST, CLASS_JOIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                object = new JSONObject(response);
                                String result = object.getString("status");

                                Toast.makeText(getApplicationContext(),CLASSCODE + " " + CLASSNAME + " " + CLASSSECTION + " " + CLASSROOM + " " + CLASSSUBJECT + " " + CLASSCREATED,Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
                                if (result.equals("success")) {
                                    Intent intent = new Intent(JoinClass.this,Home.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId",userId);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Class Not Found",Toast.LENGTH_SHORT).show();
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

                    params.put("classId",CLASSCODE);
                    params.put("className",CLASSNAME);
                    params.put("classSection",CLASSSECTION);
                    params.put("classRoom",CLASSROOM);
                    params.put("classSubject",CLASSSUBJECT);
                    params.put("classCreated",CLASSCREATED);
                    params.put("classStudents",userId);
                    params.put("classDate",date);
                    params.put("bgNo",String.valueOf(backgroundNumber));
                    return params;
                }
            };
            joinQueue.add(joinRequest);
            return null;
        }
    }
}
