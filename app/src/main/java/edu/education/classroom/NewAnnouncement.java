package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.education.classroom.customLayout.BottomAttachmentSheet;

public class NewAnnouncement extends AppCompatActivity implements BottomAttachmentSheet.BottomAttachmentListener {

    private String ANNOUNCEMENT_URL = "http://192.168.43.90/Classroom/php%20Codes/create_announcement.php";

    private LinearLayout closeActivity;
    private LinearLayout addAttachment;
    private LinearLayout AddAnnouncement;
    private EditText getAnnouncement;
    private ImageView imageView;
    private VideoView videoView;
    private boolean flag = false;

    private String announcement;
    private String userId;
    private String classId;
    private String announcementId;
    private String announcementTime;
    private UUID uuid;

    private Bundle bundle;
    private RequestQueue queue;
    private StringRequest request;
    private JSONObject object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_announcement);

        bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        classId = bundle.getString("classId");

        closeActivity = findViewById(R.id.closeAnnouncement);
        addAttachment = findViewById(R.id.addAttachment);
        AddAnnouncement = findViewById(R.id.postAnnouncement);
        getAnnouncement = findViewById(R.id.getAnnouncement);
        imageView = findViewById(R.id.tempImageView);
        videoView = findViewById(R.id.tempVideoView);

        AddAnnouncement.setClickable(false);

        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAnnouncement.super.onBackPressed();
            }
        });

        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomAttachmentSheet attachmentSheet = new BottomAttachmentSheet();
                attachmentSheet.show(getSupportFragmentManager(),"bottom Sheet");
            }
        });

        getAnnouncement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    AddAnnouncement.setClickable(false);
                }
                else {
                    AddAnnouncement.setClickable(true);
                    AddAnnouncement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                            announcement = getAnnouncement.getText().toString();
                            announcementId = generateAnnouncementID();
                            announcementTime = generateAnnouncementTime();

                            queue = Volley.newRequestQueue(NewAnnouncement.this);
                            request = new StringRequest(Request.Method.POST, ANNOUNCEMENT_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                object = new JSONObject(response);
                                                String result = object.getString("status");
                                                if (result.equals("success"))
                                                {
                                                    flag = true;
                                                    NewAnnouncement.super.onBackPressed();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"Please Try again Later",Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    params.put("classId",classId);
                                    params.put("userId",userId);
                                    params.put("message",announcement);
                                    params.put("date",announcementTime);
                                    params.put("announcementId",announcementId);

                                    return params;
                                }
                            };
                            queue.add(request);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public boolean createAnnouncement(final String ClassId, final String UserId, final String message) {

        return false;
    }

    private String generateAnnouncementTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    private String generateAnnouncementID() {
        uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public void onCameraAttachment(Bitmap imageMap,int accessCode) {
        imageView.setImageBitmap(imageMap);
    }

    @Override
    public void onVideoAttachment(Uri videoUrl, int video_camera_access_request_code) {
        videoView.setVideoURI(videoUrl);
        videoView.start();
    }

    @Override
    public void onFileAttachment(String filePath, String fileExtension, int file_upload_access_request_code) {
        Toast.makeText(getApplicationContext(),filePath,Toast.LENGTH_SHORT).show();
        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();
    }
}
