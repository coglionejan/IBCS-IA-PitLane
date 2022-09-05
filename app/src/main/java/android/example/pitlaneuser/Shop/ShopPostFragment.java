package android.example.pitlaneuser.Shop;

import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.RequestPostAdapter;
import android.example.pitlaneuser.User.UserRequest;
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

public class ShopPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShopPostAdapter postAdapter;
    private List<UserRequest> requestPostList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String shopemail = user.getEmail();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.post_fragment, viewGroup, false);
        recyclerView = view.findViewById(R.id.recyclerView);
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
        String curRegion = LoginActivityShop.fianlshopregion;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Estimate Requests");

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
                postAdapter = new ShopPostAdapter(getActivity(), requestPostList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sortPostsFromOldToRecent(List<UserRequest> list){
        int length = list.size();
        int min, minIndex;
        for (int i=0; i<length; ++i){
            UserRequest curPost = list.get(i);
            minIndex = i;
            min = getYear(curPost);
            for (int j = i+1; j<length; ++j){
                int yr = getYear(list.get(j));
                if (yr>min){
                    list.set(minIndex, list.get(j));
                    list.set(j, curPost);
                }
            }
        }

        for (int i=0; i<length; ++i){
            UserRequest curPost = list.get(i);
            minIndex = i;
            min = getMonth(curPost);
            for (int j = i+1; j<length; ++j){
                int mm = getMonth(list.get(j));
                if (getYear(list.get(i)) == getYear(list.get(j))){
                    if (mm>min){
                        list.set(minIndex, list.get(j));
                        list.set(j, curPost);
                    }
                }
            }
        }

        for (int i=0; i<length; ++i){
            UserRequest curPost = list.get(i);
            minIndex = i;
            min = getDay(curPost);
            for (int j = i+1; j<length; ++j){
                int day = getDay(list.get(j));
                if (getYear(list.get(i)) == getYear(list.get(j)) && getMonth(list.get(i)) == getMonth(list.get(j))){
                    if (day>min){
                        list.set(minIndex, list.get(j));
                        list.set(j, curPost);
                    }
                }
            }
        }

        for (int i=0; i<length; ++i){
            UserRequest curPost = list.get(i);
            minIndex = i;
            min = getHour(curPost);
            for (int j = i+1; j<length; ++j){
                int hr = getHour(list.get(j));
                if (getYear(list.get(i)) == getYear(list.get(j)) && getMonth(list.get(i)) == getMonth(list.get(j))
                    && getDay(list.get(i))==getDay(list.get(j))){
                    if (hr>min){
                        list.set(minIndex, list.get(j));
                        list.set(j, curPost);
                    }
                }
            }
        }

        for (int i=0; i<length; ++i){
            UserRequest curPost = list.get(i);
            minIndex = i;
            min = getHour(curPost);
            for (int j = i+1; j<length; ++j){
                int minute = getMinute(list.get(j));
                if (getYear(list.get(i)) == getYear(list.get(j)) && getMonth(list.get(i)) == getMonth(list.get(j))
                        && getDay(list.get(i))==getDay(list.get(j)) && getHour(list.get(i))==getHour(list.get(j))){
                    if (minute<min){
                        list.set(minIndex, list.get(j));
                        list.set(j, curPost);
                    }
                }
            }
        }
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    private int getYear(UserRequest r){
        String date = r.getDate();
        return Integer.parseInt(date.substring(0,4));
    }

    private int getMonth(UserRequest r){
        String date = r.getDate();
        return Integer.parseInt(date.substring(5,7));
    }

    private int getDay(UserRequest r){
        String date = r.getDate();
        return Integer.parseInt(date.substring(8,10));
    }

    private int getHour(UserRequest r){
        String date = r.getDate();
        return Integer.parseInt(date.substring(11,13));
    }

    private int getMinute(UserRequest r){
        String date = r.getDate();
        return Integer.parseInt(date.substring(14));
    }
}
