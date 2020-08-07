package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.education.classroom.Classes.CommentDetails;
import edu.education.classroom.adapter.InflateCommentLister;

public class ViewAnnouncement extends AppCompatActivity {

    private final String GET_USER_NAME_URL = "http://192.168.43.90/Classroom/php%20Codes/get_user_name.php";
    private final String GET_COMMENT_URL = "http://192.168.43.90/Classroom/php%20Codes/get_comment.php";
    private final String ADD_COMMENT_URL = "http://192.168.43.90/Classroom/php%20Codes/add_comment.php";

    private Bundle bundle;

    private String userName;
    private String userDate;
    private String userProfile;
    private String message;
    private String announcementId;
    private String commentDate;
    private String profilePic;
    private String userId;
    private String name;

    private TextView announcerName;
    private TextView classComment;
    private TextView announcementDate;
    private TextView announcementMessage;
    private EditText userComment;
    private LinearLayout sendComment;
    private RecyclerView recyclerView;
    private LinearLayout goBackButton;
    private ImageView announcerProfilePic;

    private RequestQueue addCommentQueue;
    private StringRequest addCommentRequest;
    private JSONObject addCommentObject;

    private RequestQueue nameQueue;
    private StringRequest nameRequest;
    private JSONArray nameArray;
    private JSONObject nameObject;

    private RequestQueue getCommentQueue;
    private StringRequest getCommentRequest;
    private JSONArray getCommentArray;
    private JSONObject getCommentObject;
    private String getCommentUserName;
    private String getCommentDate;
    private String getCommentMessage;
    private String getCommentUserProfile;
    private InflateCommentLister adapter;
    private ArrayList<CommentDetails> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement);

        bundle = getIntent().getExtras();
        userName = bundle.getString("name");
        userDate = bundle.getString("date");
        userProfile = bundle.getString("profileurl");
        message = bundle.getString("message");
        announcementId = bundle.getString("announcementId");
        profilePic = bundle.getString("userProfile");
        userId = bundle.getString("userId");

        classComment = findViewById(R.id.ClassComment);
        announcerName = findViewById(R.id.name);
        announcementDate = findViewById(R.id.date);
        goBackButton = findViewById(R.id.back);
        announcerProfilePic = findViewById(R.id.profilePic);
        announcementMessage = findViewById(R.id.message);
        userComment = findViewById(R.id.userComment);
        sendComment = findViewById(R.id.sendComment);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initializeTopLayout(userName,userDate,userProfile,message);
        classComment.setVisibility(View.GONE);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnnouncement.super.onBackPressed();
            }
        });
        
        name = getCurrentUserName(userId);

        userComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    sendComment.setEnabled(false);
                }
                else {
                    sendComment.setEnabled(true);
                    sendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addComment();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        details = new ArrayList<>();
        adapter = new InflateCommentLister(ViewAnnouncement.this,details);
        recyclerView.setAdapter(adapter);
        fetchComment(announcementId);
    }

    private String getCurrentUserName(final String userId) {

        nameQueue = Volley.newRequestQueue(ViewAnnouncement.this);
        nameRequest = new StringRequest(Request.Method.POST, GET_USER_NAME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            nameArray = object.getJSONArray("user");
                            nameObject = nameArray.getJSONObject(0);
                            name = nameObject.getString("name");
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
                Map<String,String> params =  new HashMap<>();
                params.put("userId",userId);
                return params;
            }
        };

        nameQueue.add(nameRequest);

        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

        return name;
    }

    private void fetchComment(final String announcementId) {

        getCommentQueue = Volley.newRequestQueue(ViewAnnouncement.this);
        getCommentRequest = new StringRequest(Request.Method.POST, GET_COMMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            getCommentArray = jsonObject.getJSONArray("comments");

                            if (getCommentArray.length() > 0)
                            {
                                classComment.setVisibility(View.VISIBLE);
                            }
                            for (int i=0;i<getCommentArray.length();i++) {
                                getCommentObject = getCommentArray.getJSONObject(i);
                                getCommentUserName = getCommentObject.getString("name");
                                getCommentUserProfile = getCommentObject.getString("profile");
                                getCommentDate = getCommentObject.getString("date");
                                getCommentMessage = getCommentObject.getString("comment");

                                details.add(new CommentDetails(getCommentMessage,getCommentUserName,getCommentUserProfile,getCommentDate));
                            }

                            adapter.notifyDataSetChanged();

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
                params.put("announcementId",announcementId);
                return params;
            }
        };

        getCommentQueue.add(getCommentRequest);
    }

    private void addComment() {
        final String commentMessage = userComment.getText().toString();

        addCommentQueue = Volley.newRequestQueue(ViewAnnouncement.this);
        addCommentRequest = new StringRequest(Request.Method.POST, ADD_COMMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            addCommentObject = new JSONObject(response);
                            if (addCommentObject.getString("status").equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Comment Added Successfully",Toast.LENGTH_SHORT).show();
                                details.add(new CommentDetails(commentMessage,name,profilePic,commentDate));
                                adapter.notifyDataSetChanged();
                                userComment.setText("");
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Failed to add Comment",Toast.LENGTH_SHORT).show();
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

                commentDate = generateCommentDate();

                params.put("announcementId",announcementId);
                params.put("message",commentMessage);
                params.put("userName",name);
                params.put("userProfile",profilePic);
                params.put("date",commentDate);
                return params;
            }
        };
        addCommentQueue.add(addCommentRequest);
    }

    private String generateCommentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    private void initializeTopLayout(String userName, String userDate, String userProfile,String message) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.ENGLISH);
        try {
            Date date = format.parse(userDate);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM");
            userDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        announcerName.setText(userName);
        announcementDate.setText(userDate);
        Picasso.get().load(userProfile).into(announcerProfilePic);
        announcementMessage.setText(message);
    }
}
