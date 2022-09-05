package android.example.pitlaneuser.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.LoginActivity;
import android.example.pitlaneuser.User.RegisterActivity;
import android.example.pitlaneuser.User.RequestEstimateActivity;
import android.example.pitlaneuser.User.UserInfo;
import android.example.pitlaneuser.User.UserRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class RegisterActivityShop extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btn_register;
    private EditText inputShopName, inputEmail, inputPassword, inputContact, inputStreet;
    private Spinner inputRegion;
    private CheckBox showPW;
    private TextView choosePic;
    private FirebaseAuth mAuth;
    private Uri picURI;
    private String picURItoString;
    final Progressbar progressbar = new Progressbar(RegisterActivityShop.this);
    int SELECT_IMAGE_CODE = 1;

    DatabaseReference addRef = FirebaseDatabase.getInstance().getReference().child("Shop");
    StorageReference mStorageRef;
    ActivityResultLauncher<Intent> activityResultLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);
        mAuth = FirebaseAuth.getInstance();

        Spinner selectRegion = findViewById(R.id.spinner_shopRegion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions, R.layout.region_spinner_item);
        selectRegion.setAdapter(adapter);
        selectRegion.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        btn_register = findViewById(R.id.btnShopRegister);
        inputShopName = findViewById(R.id.inputShopName);
        inputEmail = findViewById(R.id.inputEmailRegister);
        inputPassword = findViewById(R.id.inputPwRegister);
        inputRegion = findViewById(R.id.spinner_shopRegion);
        inputStreet = findViewById(R.id.inputStreetNum);
        inputContact = findViewById(R.id.inputContact);
        choosePic = findViewById(R.id.chooseShopPicture);
        showPW = findViewById(R.id.checkbox_showPWSHOP);

        TextView backLogin = findViewById(R.id.alreadyHaveAccount);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivityShop.this, android.example.pitlaneuser.Shop.LoginActivityShop.class));
            }
        });

        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputShopName.equals("")){
                    Toast.makeText(RegisterActivityShop.this, "Please type your shop name first", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_IMAGE_CODE);
                }

            }
        });

        showPW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopname = inputShopName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String region = inputRegion.getSelectedItem().toString();
                String contact = inputContact.getText().toString();
                String street = inputStreet.getText().toString();
                double rating = 0.0;
                String encodeEmail;
                encodeEmail = EncodeString(email);
                if (shopname.equals("") || email.equals("") ||password.equals("") ||region.equals("")
                        ||contact.equals("") || street.equals("")){
                    Toast.makeText(RegisterActivityShop.this, "Fill in everything before you register",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    addPicture(encodeEmail, shopname, password, region, street, contact, rating);
                }
            }
        });


    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE){
            picURI = data.getData();
            choosePic.setText(picURI.toString());
        }
    }

    public void addPicture(String email, String shopname, String pw, String region, String street, String contact, double rating){
        final String randomKey = UUID.randomUUID().toString();
        mStorageRef = FirebaseStorage.getInstance().getReference("Shop Pictures").child(shopname).child(randomKey);
        if (choosePic.getText().toString().equals("Choose a shop picture from file")){
            Toast.makeText(RegisterActivityShop.this, "Choose a picture of your shop", Toast.LENGTH_SHORT).show();
        }
        else{
            progressbar.showDialog();
            mStorageRef.putFile(picURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    picURItoString = uri.toString();
                                    ShopInfo shopInfo = new ShopInfo();
                                    shopInfo.setEmail(email);
                                    shopInfo.setShopName(shopname);
                                    shopInfo.setPassword(pw);
                                    shopInfo.setRegion(region);
                                    shopInfo.setContact(contact);
                                    shopInfo.setRating(rating);
                                    shopInfo.setStreetAddress(street);
                                    shopInfo.setRatingCount(0);
                                    shopInfo.setURI(picURItoString);
                                    addShopToDatabase(shopInfo);
                                    progressbar.dismissBar();
                                }
                            });
                        }
                    });
        }
    }

    public void addShopToDatabase(ShopInfo i) {
        addRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(i.getEmail())) {
                    Toast.makeText(RegisterActivityShop.this, "This email is already registered",
                            Toast.LENGTH_SHORT).show();
                } else if (snapshot.hasChild(i.getShopName())) {
                    Toast.makeText(RegisterActivityShop.this, "This shop name is already registered",
                            Toast.LENGTH_SHORT).show();
                } else {
                    addRef.child(i.getEmail()).setValue(i);
                    String decodeEmail = DecodeString(i.getEmail());
                    registerShop(decodeEmail, i.getPassword());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void registerShop(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivityShop.this, "Registration successful",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivityShop.this, LoginActivityShop.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivityShop.this, "Registration failed",
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}