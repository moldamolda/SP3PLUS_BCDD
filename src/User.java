

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userName;
    private String passWord;


    Login log = new Login();

    public User(String userName, String passWord){
        this.userName = userName;
        this.passWord = passWord;

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }



}
