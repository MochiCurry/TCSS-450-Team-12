/**
 * User class that stores an email and a password. Also contains a function to parse JSON files to
 * retrieve a username and password.
 *
 * @author Travis Bain
 * @version 1.0
 * @since 1.0
 */

package edu.tacoma.uw.css.sextod.memeups.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class User implements Serializable
{
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private String mEmail, mPassword;

    /**
     * Constructor
     * @param mEmail
     * @param mPassword
     */
    public User(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    //Function parses a JSON string to retrieve email and password
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
