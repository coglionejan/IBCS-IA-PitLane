package android.example.pitlaneuser.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.example.pitlaneuser.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostFragment2 extends Fragment {

    private RecyclerView recyclerView;
    private RequestPostAdapter postAdapter;
    private List<UserRequest> requestPostList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");

    String useremail = user.getEmail();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.post_fragment2, viewGroup, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestPostList = new ArrayList<>();
        postAdapter = new RequestPostAdapter(getContext(), requestPostList);
        recyclerView.setAdapter(postAdapter);

        readPosts();
        return view;
    }

    public View onCreateViewRecentToOld(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.post_fragment2, viewGroup, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestPostList = new ArrayList<>();
        postAdapter = new RequestPostAdapter(getContext(), requestPostList);
        recyclerView.setAdapter(postAdapter);

        readPosts();
        return view;
    }

    private void readPosts(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestPostList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserRequest post = dataSnapshot.getValue(UserRequest.class);
                    if (post.getUsername().equals(LoginActivity.finalUsername)){
                        requestPostList.add(post);
                    }
                }
                postAdapter = new RequestPostAdapter(getActivity(), requestPostList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}