package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.education.classroom.Classes.ClassDetails;
import edu.education.classroom.adapter.InflateClassLister;

public class Home extends AppCompatActivity implements InflateClassLister.OnItemClickListener {

    private LinearLayout addClass;
    private RecyclerView recyclerView;
    private InflateClassLister adapter;
    private SwipeRefreshLayout refreshLayout;

    private Bundle bundle;

    private String ClassId;
    private String ClassName;
    private String ClassSection;
    private String ClassRoom;
    private String ClassSubject;
    private String ClassCreated;
    private String ClassStudent;
    private String ClassNumber = null;
    private String userId;
    private int ClassBackground;
    private String JSON_URL = "http://192.168.43.90/Classroom/php%20Codes/get_class.php";
    private String COUNT_URL = "http://192.168.43.90/Classroom/php%20Codes/get_class_count.php";
    private ArrayList<ClassDetails> details;

    private RequestQueue queue;
    private StringRequest request;
    private StringRequest stringRequest;
    private JSONArray array;
    private JSONObject object;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bundle = getIntent().getExtras();
        userId = bundle.getString("userId");

        addClass = findViewById(R.id.addClassId);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Home.this,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.joinClass:
                                Intent joinIntent = new Intent(Home.this,JoinClass.class);
                                Bundle joinBundle = new Bundle();
                                bundle.putString("userId",userId);
                                joinIntent.putExtras(bundle);
                                startActivity(joinIntent);
                                return true;
                            case R.id.newClass:
                                Intent intent = new Intent(Home.this,CreateClass.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userId",userId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.home_menu);
                popupMenu.show();
                //Toast.makeText(getApplicationContext(),"Add Class",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(Home.this,CreateClass.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        ClassCount();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ClassCount();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        ClassDetails classDetails = details.get(position);
        //Toast.makeText(getApplicationContext(),classDetails.getClassName(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Home.this,InClass.class);
        Bundle bundle = new Bundle();
        bundle.putString("code",classDetails.getClassId());
        bundle.putString("name",classDetails.getClassName());
        bundle.putString("section",classDetails.getClassSection());
        bundle.putString("email",userId);
        bundle.putInt("backgroundNumber",classDetails.getBackgroundNumber());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void ClassCount()
    {
        refreshLayout.setRefreshing(true);
        queue = Volley.newRequestQueue(Home.this);
        stringRequest = new StringRequest(Request.Method.POST, COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            jsonObject = new JSONObject(response);
                            System.out.print("\n\n\n\nResult " + jsonObject.getString("result"));
                            ClassNumber = jsonObject.getString("result");

                            if (ClassNumber.equals("true"))
                            {
                                GetClass();
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
                params.put("userId",userId);
                return params;
            }

        };

        queue.add(stringRequest);
    }

    void GetClass()
    {
        details = new ArrayList<>();
        queue = Volley.newRequestQueue(Home.this);
        request = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject objectJson = new JSONObject(response);
                            array = objectJson.getJSONArray("classes");
                            for (int i=0;i<array.length();i++) {
                                object = array.getJSONObject(i);
                                ClassId = object.getString("id");
                                ClassName = object.getString("name");
                                ClassSection = object.getString("section");
                                ClassRoom = object.getString("room");
                                ClassSubject = object.getString("subject");
                                ClassCreated = object.getString("created");
                                ClassBackground = Integer.parseInt(object.getString("background"));
                                details.add(new ClassDetails(ClassId, ClassName, ClassRoom, ClassSection, ClassCreated,ClassBackground));
                            }

                            refreshLayout.setRefreshing(false);
                            adapter = new InflateClassLister(Home.this,details);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(Home.this);

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
                params.put("userId",userId);
                return params;
            }

        };

        queue.add(request);
    }
}
