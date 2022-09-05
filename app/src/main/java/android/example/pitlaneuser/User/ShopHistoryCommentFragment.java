package android.example.pitlaneuser.User;

import android.example.pitlaneuser.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopHistoryCommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private List<ShopComment> shopCommentsList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.post_fragment, viewGroup, false);
        recyclerView = view.findViewById(R.id.shopCommentRecyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        shopCommentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(getContext(), shopCommentsList);
        recyclerView.setAdapter(commentsAdapter);

        return view;
    }

    private void readComments(String postID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(postID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shopCommentsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopComment comment = dataSnapshot.getValue(ShopComment.class);
                    shopCommentsList.add(comment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
