package android.example.pitlaneuser.User;

public class ShopInfo {
    private String shopName, email, password, region, streetAddress, contact, URI;
    private int ratingCount;
    private double rating;

    public ShopInfo(){
    }

    public ShopInfo(String shopName, String email, String password, String region, String streetAddress, String contact, String URI, double rating, int ratingCount){
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.region = region;
        this.streetAddress = streetAddress;
        this.contact = contact;
        this.URI = URI;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void EncodeString() {
        email.replace(".", ",");
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
