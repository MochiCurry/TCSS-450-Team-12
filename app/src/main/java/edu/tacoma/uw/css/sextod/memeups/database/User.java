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
import java.util.regex.Pattern;

/**
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class User implements Serializable
{
    /**
     * Constant for Email string
     */
    public static final String EMAIL = "email";
    /**
     * Constant for Password string
     */
    public static final String PASSWORD = "password";

    private String mEmail, mPassword;

    /**
     * Constructor
     * @param email the provided email
     * @param password the provided password
     */
    public User(String email, String password) {
        if(isValidEmail(email))
        {
            if(isValidPassword(password))
            {
                this.mEmail = email;
                this.mPassword = password;
            }
            else
            {
                throw new IllegalArgumentException("Invalid password");
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    /**
     * Parses a JSON string to retrieve email and password
     * @param courseJSON the JSON string.
     */
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

    /**
     * Getter method for email.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Setter method for email.
     *
     * @param mEmail the email to set the variable to.
     */
    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }


    /**
     * Getter method for password.
     */
    public String getPassword() {
        return mPassword;
    }


    /**
     * Setter method for password.
     *
     * @param mPassword the password to set the variable to.
     */
    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Validates if the given input is a valid email address.
     *
     * @param email        The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private final static int PASSWORD_LEN = 6;
    /**
     * Validates if the given password is valid.
     * Valid password must be at last 6 characters long
     * with at least one digit and one symbol.
     *
     * @param password        The password to validate.
     * @return {@code true} if the input is a valid password.
     * {@code false} otherwise.
     */
    public static boolean isValidPassword(String password) {
        boolean foundDigit = false;
        if  (password == null ||
                password.length() < PASSWORD_LEN)
            return false;
        for (int i=0; i<password.length(); i++) {
            if (Character.isDigit(password.charAt(i)))
                foundDigit = true;
        }
        return foundDigit;
    }

}
