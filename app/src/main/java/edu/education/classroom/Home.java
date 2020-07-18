package edu.education.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private LinearLayout addClass;

    private Bundle bundle;

    private String userId;

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
                //Toast.makeText(getApplicationContext(),"Add Class",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home.this,CreateClass.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
