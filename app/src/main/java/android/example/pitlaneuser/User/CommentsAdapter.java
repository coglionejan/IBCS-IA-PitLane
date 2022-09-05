package android.example.pitlaneuser.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.example.pitlaneuser.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context mContext;
    private List<android.example.pitlaneuser.User.ShopComment> mCommentList;

    private FirebaseUser firebaseUser;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");

    public CommentsAdapter (Context mContext, List<android.example.pitlaneuser.User.ShopComment> mCommentList){
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_comment_user, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ShopComment comment = mCommentList.get(position);

        String shopNameComment = mCommentList.get(position).getShopname();
        String priceComment = mCommentList.get(position).getPrice();
        String contactComment = mCommentList.get(position).getContact();
        String shopemail = mCommentList.get(position).getShopemail();
        int isAccepted = mCommentList.get(position).getIsAccepted();
        double ratingComment = mCommentList.get(position).getRating();

        holder.shopName.setText(shopNameComment);
        holder.rating.setText(String.valueOf(ratingComment));
        holder.price.setText("Price: " + priceComment);
        holder.contact.setText("Contact: " + contactComment);
        if (checkAcceptedOffer(mCommentList) == false){
            holder.acceptOffer.setText("Accept this offer");
            holder.rateShop.setVisibility(8);
            holder.selected.setVisibility(8);
        }
        else{
            if (mCommentList.get(position).getIsAccepted() == 1){
                holder.rateShop.setText("Rate this shop");
                holder.acceptOffer.setVisibility(8);
            }
            else{
                holder.acceptOffer.setVisibility(8);
                holder.rateShop.setVisibility(8);
                holder.selected.setVisibility(8);
            }
        }

        holder.rateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RatingShopActivity.class);
                intent.putExtra("rating", ratingComment);
                intent.putExtra("shopname", comment.getShopname());
                intent.putExtra("shopemail", comment.getShopemail());
                intent.putExtra("postID", CommentsActivity.postID);
                mContext.startActivity(intent);
            }
        });

        holder.acceptOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AcceptOfferActivity.class);
                intent.putExtra("rating", comment.getRating());
                intent.putExtra("shopname", comment.getShopname());
                intent.putExtra("shopemail", comment.getShopemail());
                intent.putExtra("postID", CommentsActivity.postID);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public boolean checkAcceptedOffer(List<android.example.pitlaneuser.User.ShopComment> list){
        boolean checker = false;
        for (int i=0; i < list.size(); i++){
            if (list.get(i).getIsAccepted() == 1){
                checker = true;
            }
        }
        return checker;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopName, rating, price, contact, selected;
        Button acceptOffer, rateShop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.comShopName);
            rating = itemView.findViewById(R.id.comRating);
            price = itemView.findViewById(R.id.comPrice);
            contact = itemView.findViewById(R.id.comContact);
            acceptOffer = itemView.findViewById(R.id.btn_comment_accept);
            rateShop = itemView.findViewById(R.id.btn_comment_rate);
            selected = itemView.findViewById(R.id.text_comment_selected_user);
        }
    }
}
