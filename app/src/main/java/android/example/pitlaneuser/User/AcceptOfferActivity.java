package android.example.pitlaneuser.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptOfferActivity extends AppCompatActivity {
    private String shopname, postID, shopemail, street, region, contact, url;
    private double rating;
    private TextView acceptoffer;
    private Button confirm;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comments");
    DatabaseReference estRef = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");
    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_offer);

        Intent intent = getIntent();
        shopname = intent.getStringExtra("shopname");
        postID = intent.getStringExtra("postID");
        shopemail = intent.getStringExtra("shopemail");

        confirm = findViewById(R.id.btn_acceptoffer);
        acceptoffer = findViewById(R.id.text_acceptoffer);
        acceptoffer.setText("I agree to accept the offer from " + shopname + ".");



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(postID).child(shopemail).child("isAccepted").setValue(1);
                estRef.child(postID).child("accepted").setValue(1);

                Intent intent1 = new Intent(AcceptOfferActivity.this, ShopInfoIntroActivity.class);
                intent1.putExtra("shopname1", shopname);
                intent1.putExtra("shopemail1", shopemail);
                startActivity(intent1);
                finish();
            }
        });
    }
}