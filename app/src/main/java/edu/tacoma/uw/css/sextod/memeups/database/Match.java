package edu.tacoma.uw.css.sextod.memeups.database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable{
    public static final String ID = "id";
    public static final String SHORT_DESC = "shortDesc";
    public static final String LONG_DESC = "longDesc";
    public static final String PRE_REQS = "prereqs";

    private String mCourseId;
    private String mShortDescription;
    private String mLongDescription;
    private String mPrereqs;

    public Match(String courseId,
                  String shortDesc,
                  String longDesc,
                  String prereqs) {
        mCourseId = courseId;
        mShortDescription = shortDesc;
        mLongDescription = longDesc;
        mPrereqs = prereqs;
    }

    public String getCourseId() {
        return mCourseId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String getPrereqs() {
        return mPrereqs;
    }

    public void setCourseId(String id) {
        mCourseId = id;
    }

    public void setShortDescription(String shortDesc) {
        mShortDescription = shortDesc;
    }

    public void setLongDescription(String longDesc) {
        mLongDescription = longDesc;
    }

    public void setPreReqs(String prereqs) {
        mPrereqs = prereqs;
    }

    public static List<Match> parseCourseJSON(String courseJSON) throws JSONException {
        List<Match> matchList = new ArrayList<Match>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Match match = new Match(obj.getString(Match.ID),
                        obj.getString(Match.SHORT_DESC) ,
                        obj.getString(Match.LONG_DESC),
                        obj.getString(Match.PRE_REQS));
                matchList.add(match);
            }
        }
        return matchList;
    }

}