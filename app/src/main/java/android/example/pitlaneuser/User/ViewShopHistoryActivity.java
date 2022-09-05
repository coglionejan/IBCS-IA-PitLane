package android.example.pitlaneuser.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ViewShopHistoryActivity extends AppCompatActivity {
    Button home, history, setting;
    static String shopname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_history);

        home = findViewById(R.id.btn_home_shopHistory);
        history = findViewById(R.id.btn_history_shopHistory);
        setting = findViewById(R.id.btn_setting_shopHistory);

        Intent intent = getIntent();
        shopname = intent.getStringExtra("shopname1");

        Toolbar toolbar = findViewById(R.id.toolbar_shophistory);
        getSupportActionBar().setTitle("View History of " + shopname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewShopHistoryActivity.this, MainActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewShopHistoryActivity.this, MyHistoryActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewShopHistoryActivity.this, UserSettingActivity.class));
            }
        });
    }
}