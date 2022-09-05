package android.example.pitlaneuser.User;

public class UserInfo {
    private String email, username, password, car;

    public UserInfo(){
    }

    public UserInfo(String email, String username, String password, String car){
        this.email = email;
        this.username = username;
        this.password = password;
        this.car = car;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
