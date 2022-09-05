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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShopHistoryCommentsAdapter extends RecyclerView.Adapter<ShopHistoryCommentsAdapter.ViewHolder> {
    private Context mContext;
    private List<ShopComment> mCommentList;

    private FirebaseUser firebaseUser;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shop");

    public ShopHistoryCommentsAdapter(Context mContext, List<ShopComment> mCommentList){
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_comment, parent, false);
        return new ShopHistoryCommentsAdapter.ViewHolder(view);
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

        if (mCommentList.get(position).getIsAccepted() == 0){
            holder.selected.setVisibility(8);
        }
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopName, rating, price, contact, selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.comShopName);
            rating = itemView.findViewById(R.id.comRating);
            price = itemView.findViewById(R.id.comPrice);
            contact = itemView.findViewById(R.id.comContact);
            selected = itemView.findViewById(R.id.text_comment_selected);
        }
    }
}
