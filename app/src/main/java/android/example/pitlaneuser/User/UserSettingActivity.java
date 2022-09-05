package android.example.pitlaneuser.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class UserSettingActivity extends RegisterActivity {
    EditText carModel, username;
    TextView name;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        user = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_usersetting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button changeInfo = findViewById(R.id.btn_changeInfo);
        Button history = findViewById(R.id.btn_history_setting);
        Button home = findViewById(R.id.btn_home_setting);
        Button logout = findViewById(R.id.btn_userlogout);
        carModel = findViewById(R.id.set_carModel);
        username = findViewById(R.id.set_username);
        name = findViewById(R.id.text_username);

        name.setText(LoginActivity.finalUsername);
        carModel.setHint(LoginActivity.finalUsercar);
        username.setHint(LoginActivity.finalUsername);

        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = EncodeString(user.getEmail());
                DatabaseReference userRef = ref.child(userEmail);
                String carModelInput = carModel.getText().toString();
                String usernameInput = username.getText().toString();
                if (carModelInput.equals(carModel.getHint()) && usernameInput.equals(username.getHint())){
                    Toast.makeText(UserSettingActivity.this, "Type in the information if you want to change", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UserSettingActivity.this, "User information has changed. Login again", Toast.LENGTH_SHORT).show();
                    if (carModelInput.equals("")){
                        ref.child(EncodeString(user.getEmail())).child("username").setValue(usernameInput);
                        ref.child(EncodeString(user.getEmail())).child("car").setValue(LoginActivity.finalUsercar);
                    }
                    else if (usernameInput.equals("")){
                        ref.child(EncodeString(user.getEmail())).child("username").setValue(LoginActivity.finalUsername);
                        ref.child(EncodeString(user.getEmail())).child("car").setValue(carModelInput);
                    }
                    else{
                        ref.child(EncodeString(user.getEmail())).child("car").setValue(carModelInput);
                        ref.child(EncodeString(user.getEmail())).child("username").setValue(usernameInput);
                    }
                    startActivity(new Intent(UserSettingActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(UserSettingActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, MyHistoryActivity.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, MainActivity.class));
            }
        });
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}