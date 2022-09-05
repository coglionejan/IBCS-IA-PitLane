package android.example.pitlaneuser.User;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.Shop.Progressbar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextInputEditText inputUsername, inputEmailRegister,
            inputPasswordRegister, inputConfirmPassword, inputCar;
    private CheckBox showPW;
    private FirebaseAuth mAuth;
    final Progressbar progressbar = new Progressbar(RegisterActivity.this);
    DatabaseReference addRef = FirebaseDatabase.getInstance().getReference().child("User");
    UserInfo userinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        TextView btn = findViewById(R.id.alreadyHaveAccount);
        inputUsername = findViewById(R.id.inputUserName);
        inputEmailRegister = findViewById(R.id.inputEmailRegister);
        inputPasswordRegister = findViewById(R.id.inputPasswordRegister);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputCar = findViewById(R.id.inputCar);
        userinfo = new UserInfo();
        showPW = findViewById(R.id.checkbox_showPW1);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = inputPasswordRegister.getText().toString();
                String confirmPW = inputConfirmPassword.getText().toString();
                String username = inputUsername.getText().toString();
                String email = inputEmailRegister.getText().toString();
                String car = inputCar.getText().toString();

                if (password.equals("") || confirmPW.equals("") || username.equals("")
                        || email.equals("") || car.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Fill in all the required information", Toast.LENGTH_SHORT).show();

                }
                else{
                    if (password.equals(confirmPW)) {
                        String encodeEmail = EncodeString(email);
                        userinfo.setUsername(username);
                        userinfo.setPassword(password);
                        userinfo.setCar(car);
                        userinfo.setEmail(encodeEmail);
                        addUsertoDatabase(username, encodeEmail, password, car, userinfo);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        showPW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    inputPasswordRegister.setInputType(61);
                    inputConfirmPassword.setInputType(61);
                }
                else{
                    inputPasswordRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inputConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    public void addUsertoDatabase(String u, String e, String p, String c, UserInfo userData) {
        progressbar.showDialog();
        addRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userData.getEmail().toString())) {
                    progressbar.dismissBar();
                    Toast.makeText(RegisterActivity.this, "This email is already registered",
                            Toast.LENGTH_SHORT).show();
                } else if (snapshot.hasChild(u)) {
                    progressbar.dismissBar();
                    Toast.makeText(RegisterActivity.this, "This username is already registered",
                            Toast.LENGTH_SHORT).show();
                } else {
                    addRef.child(e).setValue(userData);
                    String decodeEmail = DecodeString(e);
                    registerUser(decodeEmail, p);
                    progressbar.dismissBar();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "Registration successful",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration failed",
                            Toast.LENGTH_SHORT).show();
                }
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