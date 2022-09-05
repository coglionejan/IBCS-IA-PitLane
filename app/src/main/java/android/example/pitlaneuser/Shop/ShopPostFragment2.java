package android.example.pitlaneuser.Shop;

import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.RequestPostAdapter;
import android.example.pitlaneuser.User.UserRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class ShopPostFragment2 extends Fragment {
    private RecyclerView recyclerView;
    private ShopPostAdapter postAdapter;
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
        postAdapter = new ShopPostAdapter(getContext(), requestPostList);
        recyclerView.setAdapter(postAdapter);

        readPosts();

        return view;
    }

    private void readPosts(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");
        String curRegion = LoginActivityShop.fianlshopregion;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                requestPostList.clear();
                for (DataSnapshot ss : snapshot1.getChildren()){
                    UserRequest post = ss.getValue(UserRequest.class);
                    if (post.getRegion().equals(curRegion)){
                        requestPostList.add(post);
                    }
                }
                if (sortPostsByAvailableRequest(requestPostList).size() == requestPostList.size()){
                    requestPostList = sortPostsByAvailableRequest(requestPostList);
                }
                postAdapter = new ShopPostAdapter(getActivity(), requestPostList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<UserRequest> sortPostsByAvailableRequest(List<UserRequest> list){
        List<UserRequest> accepted = new ArrayList<>();
        List<UserRequest> unaccepted = new ArrayList<>();
        List<UserRequest> finalList = new ArrayList<>();
        int len = list.size();
        for (int i = 0; i < len; i++){
            if (list.get(i).getAccepted() == 0){
                unaccepted.add(list.get(i));
            }
            else{
                accepted.add(list.get(i));
            }
        }
        int len1 = accepted.size();
        int len2 = unaccepted.size();
        for (int j = 0; j < len1; j++){
            finalList.add(accepted.get(j));
        }
        for (int k = 0; k < len2; k++){
            finalList.add(unaccepted.get(k));
        }
        return finalList;
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}