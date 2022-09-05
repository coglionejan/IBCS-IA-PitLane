package android.example.pitlaneuser.User;

import android.widget.ImageView;

public class UserRequest extends UserInfo{
    private String username, email, car, password, repairType, region, requirement, imageURL, date, postID;
    private int accepted;

    public UserRequest(String username, String email, String car, String password ,String repairType,String region,
                       String requirement, String imageURL, String date, String postID){
        super(email, username, "", car);
        this.username = username;
        this.email = email;
        this.car = car;
        this.password = password;
        this.repairType = repairType;
        this.region = region;
        this.requirement = requirement;
        this.imageURL = imageURL;
        this.date = date;
        this.postID = postID;
        this.accepted = 0;
    }

    public UserRequest() {
    }
    public void EncodeString() {
        username.replace(".", ",");
        repairType.replace(".","");
        requirement.replace(".", "");
        requirement.replace("$", "USD");
        requirement.replace("[", "(");
        requirement.replace("]", ")");
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURI(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
}
