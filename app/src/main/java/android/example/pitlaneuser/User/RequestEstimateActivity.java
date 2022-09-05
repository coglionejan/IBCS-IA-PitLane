package android.example.pitlaneuser.User;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.Shop.LoginActivityShop;
import android.example.pitlaneuser.Shop.Progressbar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RequestEstimateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int PICK_IMAGE = 1;
    private ImageView carPic;
    private Button choosePic, request;
    private Spinner region;
    private TextInputEditText inputRepairType, inputRequirement;
    private Date date;
    private String formattedDate;
    private Uri pictureUri;
    private StorageTask task;
    final Progressbar progressbar = new Progressbar(RequestEstimateActivity.this);
    int SELECT_IMAGE_CODE = 1;
    DatabaseReference ref;
    StorageReference mStorageRef;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("User Request Images");
        ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_estimate);

        Toolbar toolbar = findViewById(R.id.toolbar_request);
        getSupportActionBar().setTitle("Request Estimate Cost");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Spinner selectRegion = findViewById(R.id.spinner_Region);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRegion.setAdapter(adapter);
        selectRegion.setOnItemSelectedListener(this);

        FirebaseUser user = mAuth.getCurrentUser();
        String useremail = user.getEmail();
        String username = LoginActivity.finalUsername;
        String car = LoginActivity.finalUsercar;
        String password = "";

        date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-hh:mm");
        formattedDate = dateFormat.format(date);

        choosePic = (Button) findViewById(R.id.btn_choosePic);
        carPic = (ImageView) findViewById(R.id.carImage);
        region = findViewById(R.id.spinner_Region);
        request = (Button) findViewById(R.id.btn_request);
        inputRepairType = findViewById(R.id.inputRepairType);
        inputRequirement = findViewById(R.id.inputRequirements);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 1){
                    Uri uri = result.getData().getData();
                    carPic.setImageURI(uri);
                }
            }
        });

        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_IMAGE_CODE);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regionToString = region.getSelectedItem().toString();
                String repairType = inputRepairType.getText().toString();
                String requirement = inputRequirement.getText().toString();
                if (repairType.equals("") || requirement.equals("")){
                    Toast.makeText(RequestEstimateActivity.this, "Please fill in all the text boxes",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    if (pictureUri != null){
                        addPicture(username, useremail, car, password, repairType, regionToString, requirement, pictureUri.toString(), formattedDate);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE){
            pictureUri = data.getData();
            carPic.setImageURI(pictureUri);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver c = getContentResolver();
        MimeTypeMap m = MimeTypeMap.getSingleton();
        return m.getExtensionFromMimeType(c.getType(uri));
    }

    private void addPicture (String username, String email, String car, String password, String repairType,
                             String regionToString, String requirement, String pictureURI, String date){
        FirebaseUser user = mAuth.getCurrentUser();
        String useremail = user.getEmail();
        progressbar.showDialog();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = mStorageRef.child(useremail.toString()).child("images/" + randomKey);

        riversRef.putFile(pictureUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageURL = uri.toString();
                                String id = ref.push().getKey();
                                UserRequest requestInfo = new UserRequest(username, email, car, password, repairType,
                                        regionToString, requirement, imageURL, date, id);
                                requestInfo.EncodeString();
                                ref.child(id).setValue(requestInfo);
                                progressbar.dismissBar();
                                Toast.makeText(RequestEstimateActivity.this, "Your Request is Posted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        startActivity(new Intent(RequestEstimateActivity.this, MainActivity.class));
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}