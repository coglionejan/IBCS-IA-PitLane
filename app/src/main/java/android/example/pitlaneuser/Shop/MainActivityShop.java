package android.example.pitlaneuser.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.MainActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivityShop extends AppCompatActivity {
    View fragment1;
    View fragment2;
    Spinner sortBy;
    Button setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);

        Toolbar toolbar = findViewById(R.id.toolbar_shopmain);
        getSupportActionBar().setTitle("Pit Lane (Shop Version)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sortBy = findViewById(R.id.spinner_shopmain);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shopSortBy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(adapter);
        fragment1 = findViewById(R.id.shopmainFragment);
        fragment2 = findViewById(R.id.shopmainFragment2);
        setting = findViewById(R.id.btn_shopsetting);

        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sortBy.getSelectedItem().toString().equals("Date Added (newest)")){
                    fragment1.setVisibility(View.VISIBLE);
                    fragment2.setVisibility(View.GONE);
                }
                else {
                    fragment2.setVisibility(View.VISIBLE);
                    fragment1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityShop.this, SettingActivityShop.class));
            }
        });
    }
}