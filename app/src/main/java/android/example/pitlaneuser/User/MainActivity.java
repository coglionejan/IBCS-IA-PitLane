package android.example.pitlaneuser.User;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_usermain);
        getSupportActionBar().setTitle("Pit Lane");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button history = findViewById(R.id.btn_history_main);
        Button setting = findViewById(R.id.btn_setting_main);
        Button request = findViewById(R.id.btn_gotoRequest);
        TextView username = findViewById(R.id.main_displayUsername);
        TextView usercar = findViewById(R.id.main_displayCarModel);
        username.setText("Username: " + LoginActivity.finalUsername);
        usercar.setText("Car model: " + LoginActivity.finalUsercar);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyHistoryActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserSettingActivity.class));
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RequestEstimateActivity.class));
            }
        });
    }
}