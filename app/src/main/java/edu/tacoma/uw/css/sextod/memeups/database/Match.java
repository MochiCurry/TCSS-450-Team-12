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
public class Match implements Serializable{
    public static final String EMAIL = "email";
    public static final String FIRST = "first";
    public static final String LAST = "last";
    public static final String USERNAME = "username";
    public static final String SCORE = "score_category";
    public static final String BIO = "bio";
    public static final String DISPLAY = "display_url";
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

    public Match()
    {

    }

    public Match(String email, String shortDesc, String longDesc, String prereqs, String bio, String display, String meme, int score) {
        mEmail = email;
        mFirst = shortDesc;
        mLast = longDesc;
        mUsername = prereqs;

        mBio = bio;
        mDisplay = display;
        mMeme = meme;
        mScore = score;
    }



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