package edu.tacoma.uw.css.sextod.memeups.database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable{
    public static final String EMAIL = "email";

    private String mEmail;

    public Match(String email) {
        mEmail = email;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public static List<Match> parseCourseJSON(String courseJSON) throws JSONException {
        List<Match> matchList = new ArrayList<Match>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Match match = new Match(obj.getString(Match.EMAIL));
                matchList.add(match);
            }
        }
        return matchList;
    }

}