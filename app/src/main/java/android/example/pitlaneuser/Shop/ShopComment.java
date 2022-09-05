package android.example.pitlaneuser.Shop;

public class ShopComment {
    private String shopname, price, contact, shopemail;
    private int isAccepted;
    private double rating;

    public ShopComment (String shopname, double rating, String price, String contact, String shopemail){
        this.shopname = shopname;
        this.rating = rating;
        this.price = price;
        this.contact = contact;
        this.shopemail = shopemail;
        this.isAccepted = 0;
    }

    public ShopComment (){

    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getShopemail() {
        return shopemail;
    }

    public void setShopemail(String shopemail) {
        this.shopemail = shopemail;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }
}
