package android.example.pitlaneuser.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.Shop.LoginActivityShop;
import android.example.pitlaneuser.Shop.SettingActivityShop;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RatingShopActivity extends AppCompatActivity {
    TextView shopname;
    ImageView star1, star2, star3, star4, star5, shopImage;
    Button submit;
    int curCount, newCount;
    double newRating;
    String postID, URL;
    ShopInfo shopInfo = new ShopInfo();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");
    DatabaseReference comRef = FirebaseDatabase.getInstance().getReference().child("Comments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_shop);

        Toolbar toolbar = findViewById(R.id.toolbar_rating);
        getSupportActionBar().setTitle("Shop Rating");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("shopname");
        String shopemail = intent.getStringExtra("shopemail");
        postID = intent.getStringExtra("postID");
        double curRating = intent.getDoubleExtra("rating", 0.0);
        getCurrentCount(shopemail);

        shopname = findViewById(R.id.rating_shopname);
        shopname.setText(name);
        shopImage = findViewById(R.id.image_rating);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        submit = findViewById(R.id.btn_rating);
        Drawable yellowStar = getResources().getDrawable(R.drawable.yellow_star_icon);
        Drawable whiteStar = getResources().getDrawable(R.drawable.star_icon);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(shopemail)){
                    URL = snapshot.child(shopemail).child("uri").getValue(String.class);
                    Glide.with(RatingShopActivity.this).load(URL).into(shopImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(yellowStar);
                star2.setImageDrawable(whiteStar);
                star3.setImageDrawable(whiteStar);
                star4.setImageDrawable(whiteStar);
                star5.setImageDrawable(whiteStar);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(yellowStar);
                star2.setImageDrawable(yellowStar);
                star3.setImageDrawable(whiteStar);
                star4.setImageDrawable(whiteStar);
                star5.setImageDrawable(whiteStar);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(yellowStar);
                star2.setImageDrawable(yellowStar);
                star3.setImageDrawable(yellowStar);
                star4.setImageDrawable(whiteStar);
                star5.setImageDrawable(whiteStar);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(yellowStar);
                star2.setImageDrawable(yellowStar);
                star3.setImageDrawable(yellowStar);
                star4.setImageDrawable(yellowStar);
                star5.setImageDrawable(whiteStar);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(yellowStar);
                star2.setImageDrawable(yellowStar);
                star3.setImageDrawable(yellowStar);
                star4.setImageDrawable(yellowStar);
                star5.setImageDrawable(yellowStar);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star5.getDrawable().equals(yellowStar)&&star4.getDrawable().equals(yellowStar)
                &&star3.getDrawable().equals(yellowStar)&&star2.getDrawable().equals(yellowStar)){
                    if (curCount == 0){
                        newRating = 5;
                    }
                    else{
                        newRating = ((curRating*curCount)+5)/(curCount+1);
                        newRating = Math.round(newRating * 100.0) / 100.0;
                    }
                }
                else if (star4.getDrawable().equals(yellowStar)&&star3.getDrawable().equals(yellowStar)){
                    if (curCount == 0){
                        newRating = 4;
                    }
                    else{
                        newRating = ((curRating*curCount)+4)/(curCount+1);
                        newRating = Math.round(newRating * 100.0) / 100.0;
                    }
                }
                else if (star3.getDrawable().equals(yellowStar)&&star2.getDrawable().equals(yellowStar)){
                    if (curCount == 0){
                        newRating = 3;
                    }
                    else{
                        newRating = ((curRating*curCount)+3)/(curCount+1);
                        newRating = Math.round(newRating * 100.0) / 100.0;
                    }
                }
                else if (star2.getDrawable().equals(yellowStar)&&star1.getDrawable().equals(yellowStar)){
                    if (curCount == 0){
                        newRating = 2;
                    }
                    else{
                        newRating = ((curRating*curCount)+2)/(curCount+1);
                        newRating = Math.round(newRating * 100.0) / 100.0;
                    }
                }
                else if (star1.getDrawable().equals(yellowStar)&&star2.getDrawable().equals(whiteStar)){
                    if (curCount == 0){
                        newRating = 1;
                    }
                    else{
                        newRating = ((curRating*curCount)+1)/(curCount+1);
                        newRating = Math.round(newRating * 100.0) / 100.0;
                    }
                }
                else{
                    Toast.makeText(RatingShopActivity.this, "You cannot give 0", Toast.LENGTH_SHORT).show();
                }
                newCount = curCount+1;
                saveNewRating(newRating, newCount, shopemail);
                Intent intent = new Intent(RatingShopActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void getCurrentCount(String e){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(e)){
                    curCount = snapshot.child(e).child("ratingCount").getValue(int.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveNewRating(double r, int c, String e){
        ref.child(e).child("rating").setValue(r);
        ref.child(e).child("ratingCount").setValue(c);
        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String id = dataSnapshot.getKey();
                    for (DataSnapshot s : dataSnapshot.getChildren()){
                        android.example.pitlaneuser.User.ShopComment comment = s.getValue(ShopComment.class);
                        if (comment.getShopemail().equals(e)){
                            comRef.child(id).child(e).child("rating").setValue(r);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}