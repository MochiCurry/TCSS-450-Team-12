package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import edu.tacoma.uw.css.sextod.memeups.database.CourseDB;
import edu.tacoma.uw.css.sextod.memeups.database.Match;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * This fragment is called by the MatchActivity to display an updated list of potential matches.
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class MatchListFragment extends Fragment {

    private static final String COURSE_URL = "http://kferg9.000webhostapp.com/android/list.php?cmd=";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Match> mCourseList;
    private RecyclerView mRecyclerView;
    private CourseDB mCourseDB;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchListFragment() {
    }

    @SuppressWarnings("unused")
    public static MatchListFragment newInstance(int columnCount) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));


        }


            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try
                {
                    StringBuilder sb = new StringBuilder(COURSE_URL);

                    SharedPreferences mLoginEmail = this.getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                            , Context.MODE_PRIVATE);

                    String mode = mLoginEmail.getString("listmode", "");
                    sb.append(URLEncoder.encode(mode, "UTF-8"));

                    String email = mLoginEmail.getString("email", "");
                    sb.append("&useremail=");
                    sb.append(URLEncoder.encode(email, "UTF-8"));

                    Log.i(TAG, sb.toString());

                    CourseAsyncTask courseAsyncTask = new CourseAsyncTask();
                    courseAsyncTask.execute(new String[]{sb.toString()});
                }catch(Exception e) {
                    Toast.makeText(view.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }

            }
            else {
                Toast.makeText(view.getContext(),
                        "No network connection available. Displaying locally stored data",
                        Toast.LENGTH_SHORT).show();

                if (mCourseDB == null) {
                    mCourseDB = new CourseDB(getActivity());
                }
                if (mCourseList == null) {
                    mCourseList = mCourseDB.getCourses();
                }

                mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));
            }










//            try
//            {
//                StringBuilder sb = new StringBuilder(COURSE_URL);
//
//                SharedPreferences mLoginEmail = this.getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
//                        , Context.MODE_PRIVATE);
//
//                String mode = mLoginEmail.getString("listmode", "");
//                sb.append(URLEncoder.encode(mode, "UTF-8"));
//
//                String email = mLoginEmail.getString("email", "");
//                sb.append("&useremail=");
//                sb.append(URLEncoder.encode(email, "UTF-8"));
//
//                Log.i(TAG, sb.toString());
//
//                CourseAsyncTask courseAsyncTask = new CourseAsyncTask();
//                courseAsyncTask.execute(new String[]{sb.toString()});
//            }catch(Exception e) {
//                Toast.makeText(view.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
//                        .show();
//            }

            /*FloatingActionButton floatingActionButton = (FloatingActionButton)
                    getActivity().findViewById(R.id.fab);
            floatingActionButton.show();*/



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Match item);
    }

    private class CourseAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of user matches, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            //Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                mCourseList = Match.parseCourseJSON(result);
            }
            catch (JSONException e) {
                if (e.getMessage().contains("End of input at character 0 of")) {
                    Toast.makeText(getActivity().getApplicationContext(), "No suitable matches found.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }


// Everything is good, show the list of courses.
            if (!mCourseList.isEmpty()) {

                if (mCourseDB == null) {
                    mCourseDB = new CourseDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mCourseDB.deleteCourses();

                // Also, add to the local database
                for (int i=0; i<mCourseList.size(); i++) {
                    Match course = mCourseList.get(i);
                    mCourseDB.insertCourse(course.getmEmail(),
                            course.getmFirst(),
                            course.getmLongDescription(),
                            course.getmUsername(),
                            course.getmBio(),
                            course.getDISPLAY(),
                            course.getMEME(),
                            course.getmScore()


                    );
                }
                mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));
            }


            if (!mCourseList.isEmpty()) {
                mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));
            }
            }
        }


}
