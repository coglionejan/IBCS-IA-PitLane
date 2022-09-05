package android.example.pitlaneuser.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopHistoryCommentActivity extends AppCompatActivity {
    ShopInfo info = new ShopInfo();

    private RecyclerView recyclerView;
    private ShopHistoryCommentsAdapter commentsAdapter;
    private List<ShopComment> commentList;

    public static String postID;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_history_comment);

        Intent intent = getIntent();
        postID = intent.getStringExtra("postid");
        String shopname = intent.getStringExtra("name");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_shophistorycomment);
        getSupportActionBar().setTitle("View History of " + shopname + " - Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        readComments(postID);

        recyclerView = findViewById(R.id.shopHistoryCommentRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentsAdapter = new ShopHistoryCommentsAdapter(this, commentList);
        recyclerView.setAdapter(commentsAdapter);
    }

    private void readComments(String id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(postID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    android.example.pitlaneuser.User.ShopComment comment = dataSnapshot.getValue(ShopComment.class);
                    commentList.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}