package android.example.pitlaneuser.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyHistoryActivity extends AppCompatActivity {
    private Spinner sortBy;
    View fragment1;
    View fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        getSupportActionBar().setTitle("My History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button home = findViewById(R.id.btn_home_history);
        Button setting = findViewById(R.id.btn_setting_history);
        fragment1 = findViewById(R.id.UserPostFragment);
        fragment2 = findViewById(R.id.UserPostFragment2);
        sortBy = findViewById(R.id.spinner_myhistory);
        sortBy.setPrompt("Sort by");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortBy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(adapter);

        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sortBy.getSelectedItem().toString().equals("Date Added (newest)")){
                    fragment2.setVisibility(View.VISIBLE);
                    fragment1.setVisibility(View.GONE);
                }
                else if (sortBy.getSelectedItem().toString().equals("Date Added (oldest)")) {
                    fragment1.setVisibility(View.VISIBLE);
                    fragment2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyHistoryActivity.this, MainActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyHistoryActivity.this, UserSettingActivity.class));
            }
        });
    }

}