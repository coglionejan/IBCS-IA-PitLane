package android.example.pitlaneuser.User;

import android.content.ContentValues;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.Shop.LoginActivityShop;
import android.example.pitlaneuser.Shop.Progressbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;
    private Button login;
    private int counter = 5;
    final Progressbar progressbar = new Progressbar(LoginActivity.this);
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    UserInfo userinfo;
    FirebaseAuth mAuth;
    FirebaseUser user;
    static String finalUsername;
    static String finalUsercar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailLogin = (EditText)findViewById(R.id.inputUsername);
        passwordLogin = (EditText)findViewById(R.id.inputPasswordLogin);
        login = (Button)findViewById(R.id.btn_login);
        ContentValues values = new ContentValues();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();
                if (email.equals("")){
                    Toast.makeText(LoginActivity.this, "Fill in your email", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(LoginActivity.this, "Fill in your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    setUserinfo(email, password);
                }
            }
        });

        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        TextView goToShopActivity = findViewById(R.id.textView_goToShopLogin);
        goToShopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginActivityShop.class));
                finish();
            }
        });
    }

    public void login (String email, String password){
        progressbar.showDialog();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressbar.dismissBar();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUserinfo(String e, String p){
        String encodeEmail = EncodeString(e);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(encodeEmail)){
                    finalUsername = snapshot.child(encodeEmail).child("username").getValue(String.class);
                    finalUsercar = snapshot.child(encodeEmail).child("car").getValue(String.class);
                    login(e, p);
                }
                else{
                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}