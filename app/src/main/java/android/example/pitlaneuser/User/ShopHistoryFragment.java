package android.example.pitlaneuser.User;

import android.content.Context;
import android.content.Intent;
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

public class ShopHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShopHistoryPostAdapter postAdapter;
    private List<UserRequest> requestPostList;
    String shopname = ShopInfoIntroActivity.name;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");
    DatabaseReference comRef = FirebaseDatabase.getInstance().getReference().child("Comments");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_shop_history, viewGroup, false);
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestPostList = new ArrayList<>();

        readPosts(shopname);
        return view;
    }

    private void readPosts(String sn){
        List<String> postIDList = new ArrayList<>();
        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String id = dataSnapshot.getKey();
                    for (DataSnapshot s : dataSnapshot.getChildren()){
                        android.example.pitlaneuser.User.ShopComment comment = s.getValue(ShopComment.class);
                        if (comment.getShopname().equals(sn)){
                            postIDList.add(id);
                        }
                    }
                }
                int len = postIDList.size();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestPostList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserRequest r = dataSnapshot.getValue(UserRequest.class);
                            for (int i = 0; i < len; i++){
                                if (r.getPostID().equals(postIDList.get(i)))
                                    requestPostList.add(r);
                            }
                        }
                        postAdapter = new ShopHistoryPostAdapter(getActivity(), requestPostList);
                        recyclerView.setAdapter(postAdapter);
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}