package edu.tacoma.uw.css.sextod.memeups.quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable
{
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private String mEmail, mPassword;

    public User(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public static List<User> parseUserJSON(String courseJSON) throws JSONException {
        List<User> userList = new ArrayList<User>();
        if (courseJSON != null) {

            JSONArray arr = new JSONArray(courseJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                User user = new User(obj.getString(User.EMAIL), obj.getString(User.PASSWORD));
                userList.add(user);
            }
        }

        return userList;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
