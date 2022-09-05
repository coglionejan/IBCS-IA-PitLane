package android.example.pitlaneuser.Shop;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.LoginActivity;
import android.example.pitlaneuser.User.MainActivity;
import android.example.pitlaneuser.User.UserInfo;
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

public class LoginActivityShop extends AppCompatActivity {
    private EditText inputShopEmail;
    private EditText inputShopPassword;
    private Button shopLogin;
    private int counter = 5;
    private FirebaseAuth mAuth;
    final Progressbar progressbar = new Progressbar(LoginActivityShop.this);
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");
    ShopInfo shopInfo;
    FirebaseUser shop;
    public static String finalShopname;
    public static String finalShopEmail;
    public static String fianlshopregion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);
        inputShopEmail = (EditText)findViewById(R.id.inputShopEmail);
        inputShopPassword = (EditText)findViewById(R.id.inputShopPassword);

        mAuth = FirebaseAuth.getInstance();
        shop = mAuth.getCurrentUser();

        Button login = findViewById(R.id.btn_shoplogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopEmailLogin = inputShopEmail.getText().toString();
                String shopPasswordLogin = inputShopPassword.getText().toString();
                if (shopEmailLogin.equals("")){
                    Toast.makeText(LoginActivityShop.this, "Fill in your email address", Toast.LENGTH_SHORT).show();
                }
                else if (shopPasswordLogin.equals("")){
                    Toast.makeText(LoginActivityShop.this, "Fill in your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    shopLogin(shopEmailLogin, shopPasswordLogin);
                }
            }
        });

        TextView register = findViewById(R.id.textViewSignUp);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivityShop.this, RegisterActivityShop.class));
            }
        });
    }

    public void shopLogin (String email, String password){
        String encodeEmail = EncodeString(email);
        setShopInfo(encodeEmail);
        progressbar.showDialog();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressbar.dismissBar();
                    Toast.makeText(LoginActivityShop.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivityShop.this, MainActivityShop.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivityShop.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setShopInfo (String e){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(e)){
                    finalShopname = snapshot.child(e).child("shopName").getValue(String.class);
                    finalShopEmail = snapshot.child(e).child("email").getValue(String.class);
                    fianlshopregion = snapshot.child(e).child("region").getValue(String.class);
                }
                else{
                    Toast.makeText(LoginActivityShop.this, "F", Toast.LENGTH_SHORT).show();
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