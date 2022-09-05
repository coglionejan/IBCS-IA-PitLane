package android.example.pitlaneuser.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.CommentsAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopCommentActivity extends AppCompatActivity {
    EditText addcomment;
    TextView post;
    ShopInfo info = new ShopInfo();

    private RecyclerView recyclerView;
    private ShopCommentsAdapter commentsAdapter;
    private List<ShopComment> commentList;

    String postID;
    String useremail;
    String shopEmail;
    String shopName;
    String shopContact = "";
    double rating = 0;
    int accepted;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_comment);

        Toolbar toolbar = findViewById(R.id.comment_shop_toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addcomment = findViewById(R.id.add_comment);
        post = findViewById(R.id.post);

        recyclerView = findViewById(R.id.shopCommentRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentsAdapter = new ShopCommentsAdapter(this, commentList);
        recyclerView.setAdapter(commentsAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");

        Intent intent = getIntent();
        postID = intent.getStringExtra("postid");
        useremail = intent.getStringExtra("useremail");
        accepted = intent.getIntExtra("isAccepted", 0);
        shopEmail = LoginActivityShop.finalShopEmail;
        shopName = LoginActivityShop.finalShopname;


        readComments(postID);

        getShopRatingAndContact(shopEmail);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accepted == 1){
                    Toast.makeText(ShopCommentActivity.this, "You cannot leave a comment because different shop's deal has already been accepted", Toast.LENGTH_SHORT).show();
                }
                else if (addcomment.getText().toString().equals("")){
                    Toast.makeText(ShopCommentActivity.this, "You cannot send an empty comment", Toast.LENGTH_SHORT).show();
                }
                else{
                    ShopComment shopComment = new ShopComment(shopName, rating, "$" + addcomment.getText().toString(), shopContact, shopEmail);
                    addComment(shopComment);
                    Toast.makeText(ShopCommentActivity.this, "Your comment is posted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void addComment(ShopComment c){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(postID);
        ref.child(shopEmail).setValue(c);
    }

    private void readComments(String id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopComment comment = dataSnapshot.getValue(ShopComment.class);
                    commentList.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getShopRatingAndContact(String email){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Shop");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(email)){
                    shopContact = snapshot.child(email).child("contact").getValue(String.class);
                    rating = snapshot.child(email).child("rating").getValue(int.class);
                }
                else{
                    Toast.makeText(ShopCommentActivity.this, "Who are you", Toast.LENGTH_SHORT).show();
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