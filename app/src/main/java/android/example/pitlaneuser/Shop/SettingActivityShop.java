package android.example.pitlaneuser.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.LoginActivity;
import android.example.pitlaneuser.User.UserSettingActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivityShop extends AppCompatActivity {
    EditText inputStreet, inputPassword, inputContact;
    CheckBox checkBox;
    Button changeInfo, logout, home;
    FirebaseUser user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_shop);

        Toolbar toolbar = findViewById(R.id.toolbar_shopsetting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        inputStreet = findViewById(R.id.shopset_street);
        inputPassword = findViewById(R.id.shopset_password);
        inputContact = findViewById(R.id.shopset_contact);
        changeInfo = findViewById(R.id.btn_shopchangeInfo);
        logout = findViewById(R.id.btn_shoplogout);
        home = findViewById(R.id.btn_home_setting);
        checkBox = findViewById(R.id.shopSetting_showpw);


        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(EncodeString(user.getEmail()))){
                    inputStreet.setHint(snapshot.child(EncodeString(LoginActivityShop.finalShopEmail))
                            .child("streetAddress").getValue(String.class));
                    inputPassword.setHint(snapshot.child(EncodeString(LoginActivityShop.finalShopEmail))
                            .child("password").getValue(String.class));
                    inputContact.setHint(snapshot.child(EncodeString(LoginActivityShop.finalShopEmail))
                            .child("contact").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    inputPassword.setInputType(61);
                }
                else{
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStreet = inputStreet.getText().toString();
                String newPassword = inputPassword.getText().toString();
                String newContact = inputContact.getText().toString();
                String ogStreet = inputStreet.getHint().toString();
                String ogPassword = inputPassword.getHint().toString();
                String ogContact = inputContact.getHint().toString();
                if (newStreet.equals("") && newPassword.equals("")
                        && newContact.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Type in the information if you want to change", Toast.LENGTH_SHORT).show();
                }
                else if (newStreet.equals("") && newPassword.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("contact").setValue(newContact);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else if (newStreet.equals("") && newContact.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("password").setValue(newPassword);
                    user.updatePassword(newPassword);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else if (newPassword.equals("") && newContact.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("streetAddress").setValue(newStreet);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else if (newStreet.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("contact").setValue(newContact);
                    shopRef.child(EncodeString(user.getEmail())).child("password").setValue(newPassword);
                    user.updatePassword(newPassword);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else if (newPassword.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("contact").setValue(newContact);
                    shopRef.child(EncodeString(user.getEmail())).child("streetAddress").setValue(newStreet);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else if (newContact.equals("")){
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("streetAddress").setValue(newStreet);
                    shopRef.child(EncodeString(user.getEmail())).child("password").setValue(newPassword);
                    user.updatePassword(newPassword);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(SettingActivityShop.this, "Shop information has changed. Login again", Toast.LENGTH_SHORT).show();
                    shopRef.child(EncodeString(user.getEmail())).child("streetAddress").setValue(newStreet);
                    shopRef.child(EncodeString(user.getEmail())).child("password").setValue(newPassword);
                    shopRef.child(EncodeString(user.getEmail())).child("contact").setValue(newContact);
                    user.updatePassword(newPassword);
                    startActivity(new Intent(SettingActivityShop.this, LoginActivity.class));
                    finish();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(SettingActivityShop.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivityShop.this, MainActivityShop.class));
            }
        });
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}