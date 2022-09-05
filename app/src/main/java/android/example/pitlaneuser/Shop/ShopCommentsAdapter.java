package android.example.pitlaneuser.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.example.pitlaneuser.R;
import android.example.pitlaneuser.User.UserInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShopCommentsAdapter extends RecyclerView.Adapter<ShopCommentsAdapter.ViewHolder> {
    private Context mContext;
    private List<ShopComment> mCommentList;

    private FirebaseUser firebaseUser;

    public ShopCommentsAdapter(Context mContext, List<ShopComment> mCommentList){
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_comment, parent, false);
        return new ShopCommentsAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ShopComment comment = mCommentList.get(position);

        String shopNameComment = comment.getShopname();
        String ratingComment = String.valueOf(comment.getRating());
        String priceComment = mCommentList.get(position).getPrice();
        String contactComment = mCommentList.get(position).getContact();

        holder.shopName.setText(shopNameComment);
        holder.rating.setText(ratingComment);
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
