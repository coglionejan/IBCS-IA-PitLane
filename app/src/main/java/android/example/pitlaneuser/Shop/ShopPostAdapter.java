package android.example.pitlaneuser.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.UserRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShopPostAdapter extends RecyclerView.Adapter<ShopPostAdapter.ViewHolder>{
    private Context mContext;
    private List<UserRequest> mPostList;
    private FirebaseUser user;
    FirebaseAuth mAuth;

    public ShopPostAdapter(Context mContext, List<UserRequest> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.request_post, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserRequest post = mPostList.get(i);

        String usernamePost = mPostList.get(i).getUsername();
        String emailPost = mPostList.get(i).getEmail();
        String repairTypePost = mPostList.get(i).getRepairType();
        String requirementPost = mPostList.get(i).getRequirement();
        String imageURL = mPostList.get(i).getImageURL();
        String postDate = mPostList.get(i).getDate();
        int accepted = mPostList.get(i).getAccepted();

        Glide.with(mContext).load(imageURL).into(holder.carImage);
        holder.username.setText("Username: " +usernamePost);
        holder.email.setText("Email: " + emailPost);
        holder.repairType.setText("Repair Type: " + repairTypePost);
        holder.requirement.setText("Other Requirements: " + requirementPost);
        holder.date.setText("Date: " + postDate);

        if (accepted == 0){
            holder.doneDeal.setVisibility(8);
        }

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopCommentActivity.class);
                intent.putExtra("postid", post.getPostID());
                intent.putExtra("useremail", post.getEmail());
                intent.putExtra("isAccepted", post.getAccepted());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView carImage;
        TextView username, email, repairType, requirement, date, doneDeal;
        Button comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carImage = itemView.findViewById(R.id.carImage);
            username = itemView.findViewById(R.id.usernamePost);
            email = itemView.findViewById(R.id.userEmailPost);
            repairType = itemView.findViewById(R.id.repairTypePost);
            requirement = itemView.findViewById(R.id.repairRequirmentPost);
            comments = itemView.findViewById(R.id.btn_comments);
            date = itemView.findViewById(R.id.date);
            doneDeal = itemView.findViewById(R.id.text_post_donedeal);
            comments.setText("View all Comments");
        }
    }
}
