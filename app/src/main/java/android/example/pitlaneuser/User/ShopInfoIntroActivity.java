package android.example.pitlaneuser.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ShopInfoIntroActivity extends AppCompatActivity {
    private String street, region, contact, url;
    private double rating;
    static String name;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info_intro);

        Intent intent = getIntent();
        name = intent.getStringExtra("shopname1");
        String email = intent.getStringExtra("shopemail1");

        TextView shopnameText = findViewById(R.id.shopname_intro);
        TextView addressText = findViewById(R.id.shopintro_shopaddress);
        TextView contactText = findViewById(R.id.shopintro_shopContact);
        TextView emailText = findViewById(R.id.shopintro_shopEmail);
        ImageView shopImage = findViewById(R.id.image_shopIntro);
        Button viewHistory = findViewById(R.id.shopintro_button);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(email)){
                    street = snapshot.child(email).child("streetAddress").getValue(String.class);
                    region = snapshot.child(email).child("region").getValue(String.class);
                    contact = snapshot.child(email).child("contact").getValue(String.class);
                    rating = snapshot.child(email).child("rating").getValue(Double.class);
                    url = snapshot.child(email).child("uri").getValue(String.class);
                    shopnameText.setText(name + " (" + rating + ")");
                    addressText.setText("Address: " + region + " Dist. " + street);
                    contactText.setText("Contact: " + contact);
                    emailText.setText("Email: " + DecodeString(email));
                    Glide.with(ShopInfoIntroActivity.this).load(url).into(shopImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShopInfoIntroActivity.this, ViewShopHistoryActivity.class);
                intent1.putExtra("shopname1", name);
                startActivity(intent1);
                finish();
            }
        });
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}