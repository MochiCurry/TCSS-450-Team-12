/**
 * Match, similarly to User, holds the data from User profiles taken from the database.
 * It allows for storing of extra fields including score, bio, display picture url, and meme url,
 * to be displayed on the matching list as well as viewing the profiles of other users
 */

package edu.tacoma.uw.css.sextod.memeups.database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Match
 *
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class Match implements Serializable{
    /**
     * Constant with email string for url
     */
    public static final String EMAIL = "email";
    /**
     * Constant with first name string for url
     */
    public static final String FIRST = "first";
    /**
     * Constant with last name string for url
     */
    public static final String LAST = "last";
    /**
     * Constant with username string for url
     */
    public static final String USERNAME = "username";
    /**
     * Constant with score string for url
     */
    public static final String SCORE = "score_category";
    /**
     * Constant with bio string for url
     */
    public static final String BIO = "bio";
    /**
     * Constant with display url string for url
     */
    public static final String DISPLAY = "display_url";
    /**
     * Constant with meme url string for url
     */
    public static final String MEME = "meme_url";

    private String mEmail;
    private String mFirst;
    private String mLast;
    private String mUsername;
    private String mBio;
    private String mDisplay;
    private String mMeme;
    private int mScore;


    public String getmFirst() {
        return mFirst;
    }

    public void setmFirst(String mShortDescription) {
        this.mFirst = mShortDescription;
    }

    public String getmLongDescription() {
        return mLast;
    }

    public void setmLast(String mLongDescription) {
        this.mLast = mLongDescription;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmBio() {
        return mBio;
    }

    public void setmBio(String mBio) {
        this.mBio = mBio;
    }

    public String getDISPLAY() {
        return mDisplay;
    }

    public void setDisplay(String display)
    {
        this.mDisplay = display;
    }

    public String getMEME() {
        return mMeme;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getmScore()
    {
        return mScore;
    }

    public void setmScore(int score)
    {
        mScore = score;
    }

    /**
     * Default constructor for match class
     */
    public Match()
    {

    }

    /**
     * Parameterized constructor
     * @param email Email field
     * @param first First name field
     * @param last Last name field
     * @param username Username field
     * @param bio Bio field
     * @param display Display pic field
     * @param meme Meme pic field
     * @param score Score field
     */
    public Match(String email, String first, String last, String username, String bio, String display, String meme, int score) {
        mEmail = email;
        mFirst = first;
        mLast = last;
        mUsername = username;

        mBio = bio;
        mDisplay = display;
        mMeme = meme;
        mScore = score;
    }

    /**
     * Parses a json string to extract the elements
     * @param courseJSON JSON String to be parsed
     * @return List of all the Match objects extracted
     * @throws JSONException
     */
    public static List<Match> parseCourseJSON(String courseJSON) throws JSONException {
        List<Match> matchList = new ArrayList<Match>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Match match = new Match(obj.getString(Match.EMAIL),
                        obj.getString(Match.FIRST),
                        obj.getString(Match.LAST),
                        obj.getString(Match.USERNAME),
                        obj.getString(Match.BIO),
                        obj.getString(Match.DISPLAY),
                        obj.getString(Match.MEME),
                        obj.getInt(Match.SCORE));
                matchList.add(match);
            }
        }
        return matchList;
    }

    /**
     * Parses JSON String for the data of a single user
     * @param courseJSON JSON String to be parsed
     * @return Returns data for a single user
     * @throws JSONException
     */
    public static Match parseUserJSON(String courseJSON) throws JSONException
    {
        Match match = null;
        if (courseJSON != null)
        {
            JSONArray arr = new JSONArray(courseJSON);
            JSONObject obj = arr.getJSONObject(0);

            match = new Match(obj.getString(Match.EMAIL),
                    obj.getString(Match.FIRST),
                    obj.getString(Match.LAST),
                    obj.getString(Match.USERNAME),
                    obj.getString(Match.BIO),
                    obj.getString(Match.DISPLAY),
                    obj.getString(Match.MEME),
                    obj.getInt(Match.SCORE));
        }
        return match;
    }
}