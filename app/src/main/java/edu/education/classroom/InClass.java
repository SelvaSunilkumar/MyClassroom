package edu.education.classroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.education.classroom.Classes.AnnouncementDetails;
import edu.education.classroom.Classes.ClassDetails;
import edu.education.classroom.adapter.InflateAnnouncementLister;

public class InClass extends AppCompatActivity implements InflateAnnouncementLister.OnItemClickListener {

    private String USER_JSON_URL = "http://192.168.43.90/Classroom/php%20Codes/login.php";
    private String ANNOUNCEMENT_JSON_URL = "http://192.168.43.90/Classroom/php%20Codes/get_announcement.php";

    private Bundle bundle;

    private TextView className;
    private TextView classSection;
    private ImageView userImage;
    private LinearLayout linearLayout;
    private LinearLayout shareContent;
    private RecyclerView recyclerView;

    private String ClassName;
    private String ClassSection;
    private String ClassId;
    private String userId;
    private String userProfileImage;
    private int backgroundNumber;

    private RequestQueue queue;
    private JsonObjectRequest userRequest;
    private JSONArray userArray;
    private JSONObject userObject;

    private RequestQueue announcementRequestQueue;
    private StringRequest announcementRequest;
    private JSONObject announcementObject;
    private JSONArray announcementArray;

    private InflateAnnouncementLister adapter;
    private ArrayList<AnnouncementDetails> details;
    private String announcementId;
    private String classId;
    private String announcementUserId;
    private String announcementMessage;
    private String announcementDate;
    private String announcementPhotoUrl;
    private String posterName;
    int backColor[] = {R.drawable.classbackground1,R.drawable.classbackground2,R.drawable.classbackground3,R.drawable.classbackground4,R.drawable.classbackground5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class);

        bundle = getIntent().getExtras();

        ClassName = bundle.getString("name");
        ClassSection = bundle.getString("section");
        ClassId = bundle.getString("code");
        userId = bundle.getString("email");
        backgroundNumber = bundle.getInt("backgroundNumber");

        recyclerView = findViewById(R.id.recyclerView);
        className = findViewById(R.id.className);
        classSection = findViewById(R.id.classSection);
        userImage = findViewById(R.id.userImage);
        shareContent = findViewById(R.id.announcements);
        linearLayout = findViewById(R.id.linearLayout);

        classSection.setText(ClassSection);
        className.setText(ClassName);
        linearLayout.setBackground(getApplicationContext().getDrawable(backColor[backgroundNumber]));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        details = new ArrayList<>();
        new FetchData().execute();
    }

    @Override
    public void onItemClick(int position) {
        AnnouncementDetails announcementDetails = details.get(position);
        Intent intent = new Intent(InClass.this,ViewAnnouncement.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",announcementDetails.getName());
        bundle.putString("date",announcementDetails.getDate());
        bundle.putString("profileurl",announcementDetails.getProfilePic());
        bundle.putString("message",announcementDetails.getMessage());
        bundle.putString("announcementId",announcementDetails.getAnnouncementId());
        bundle.putString("userProfile",userProfileImage);
        bundle.putString("userId",userId);
        intent.putExtras(bundle);
        startActivity(intent);
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

            announcementRequestQueue = Volley.newRequestQueue(InClass.this);
            announcementRequest = new StringRequest(Request.Method.POST, ANNOUNCEMENT_JSON_URL,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                announcementArray = jsonObject.getJSONArray("announcement");

                                for (int i=0;i<announcementArray.length();i++) {
                                    announcementObject = announcementArray.getJSONObject(i);
                                    //code here
                                    announcementId = announcementObject.getString("announcementId");
                                    classId = announcementObject.getString("classId");
                                    announcementUserId = announcementObject.getString("username");
                                    announcementMessage = announcementObject.getString("announcement");
                                    announcementDate = announcementObject.getString("date");
                                    announcementPhotoUrl = announcementObject.getString("url");
                                    posterName = announcementObject.getString("name");
                                    details.add(new AnnouncementDetails(announcementId,classId,announcementUserId,announcementMessage,announcementDate,announcementPhotoUrl,posterName));
                                }
                                //Collections.sort(details);
                                //Collections.reverse(details);
                                details.sort(new DataSorter());
                                adapter = new InflateAnnouncementLister(InClass.this,details);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(InClass.this);

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
                    params.put("classId",ClassId);
                    return params;
                }
            };
            announcementRequestQueue.add(announcementRequest);
            return null;
        }
    }

    class DataSorter implements Comparator<AnnouncementDetails> {

        @Override
        public int compare(AnnouncementDetails o1, AnnouncementDetails o2) {
            //return o1.getDate().compareTo(o2.getDate());
            return o2.getDate().compareTo(o1.getDate());
        }
    }
}
