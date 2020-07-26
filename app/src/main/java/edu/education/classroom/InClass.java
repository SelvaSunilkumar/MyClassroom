package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InClass extends AppCompatActivity {

    private String USER_JSON_URL = "http://192.168.43.89/Classroom/php%20Codes/login.php";

    private Bundle bundle;

    private TextView className;
    private TextView classSection;
    private ImageView userImage;
    private LinearLayout shareContent;

    private String ClassName;
    private String ClassSection;
    private String ClassId;
    private String userId;
    private String userProfileImage;

    private RequestQueue queue;
    private JsonObjectRequest userRequest;
    private JSONArray userArray;
    private JSONObject userObject;

    @Override
    protected void onStart() {
        super.onStart();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        Toast.makeText(getApplicationContext(),"Date : " + date,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class);

        bundle = getIntent().getExtras();

        ClassName = bundle.getString("name");
        ClassSection = bundle.getString("section");
        ClassId = bundle.getString("code");
        userId = bundle.getString("email");

        className = findViewById(R.id.className);
        classSection = findViewById(R.id.classSection);
        userImage = findViewById(R.id.userImage);
        shareContent = findViewById(R.id.announcements);

        classSection.setText(ClassSection);
        className.setText(ClassName);

        new LoadImage().execute();
        //Toast.makeText(getApplicationContext(),"Back to Main",Toast.LENGTH_LONG).show();

        shareContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InClass.this,NewAnnouncement.class);
                Bundle bundle = new Bundle();
                bundle.putString("classId",ClassId);
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    class LoadImage extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            queue = Volley.newRequestQueue(InClass.this);
            userRequest = new JsonObjectRequest(Request.Method.POST, USER_JSON_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                userArray = response.getJSONArray("users");
                                for (int i = 0; i < userArray.length(); i++) {
                                    userObject = userArray.getJSONObject(i);

                                    System.out.print(userObject.getString("email") + userObject.getString("picurl"));
                                    if (userObject.getString("email").equals(userId)) {
                                        userProfileImage = userObject.getString("picurl");
                                        //Toast.makeText(getApplicationContext(), userProfileImage, Toast.LENGTH_LONG).show();
                                        loadImage(userProfileImage);
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load User Image", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(userRequest);
            return null;
        }
    }
    private void loadImage(String userProfileImage) {
        //Toast.makeText(getApplicationContext(),"Inside function : " + userProfileImage,Toast.LENGTH_LONG).show();
        Picasso.get().load(userProfileImage).into(userImage);
    }

    class FetchData extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
