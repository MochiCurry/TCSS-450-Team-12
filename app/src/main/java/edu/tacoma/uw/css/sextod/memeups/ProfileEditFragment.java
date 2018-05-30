package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Match mUser;

    private CourseAddListener mListener;
    private final static String COURSE_ADD_URL
            = "http://kferg9.000webhostapp.com/android/updateProfile.php?cmd=update";
    private final static String GET_USER_URL
            = "http://kferg9.000webhostapp.com/android/list.php?cmd=singleuser";

    private EditText mUserNameEditText;
    private EditText mCatchPhraseEditText;
    private EditText mBiographyEditText;
    private EditText mPreferenceEditText;
    private EditText mDisplayEditText;
    private EditText mMemeEditText;



   // private OnFragmentInteractionListener mListener;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        try
        {
            StringBuilder sb = new StringBuilder(GET_USER_URL);

            SharedPreferences mLoginEmail = this.getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);

            String email = mLoginEmail.getString("email", "");
            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            Log.i(TAG, sb.toString());

            CourseAsyncTask courseAsyncTask = new CourseAsyncTask();
            courseAsyncTask.execute(new String[]{sb.toString()});


        }catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        mUserNameEditText = (EditText) v.findViewById(R.id.user_name);
        mUserNameEditText.setText("", TextView.BufferType.EDITABLE);
      //  mCatchPhraseEditText = (EditText) v.findViewById(R.id.catch_phrase);
        mBiographyEditText = (EditText) v.findViewById(R.id.biography);
       // mPreferenceEditText = (EditText) v.findViewById(R.id.preference);
        mDisplayEditText = (EditText) v.findViewById(R.id.display_url);
        mMemeEditText = (EditText) v.findViewById(R.id.meme_url);


        Button addCourseButton = (Button) v.findViewById(R.id.btnCourse);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addCourse(url);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseAddListener) {
            mListener = (CourseAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseAddListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public interface CourseAddListener {
        public void addCourse(String url);
    }

    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);

        try {
            //To use getSharedPreferences in a fragment, use this code.
            SharedPreferences mLoginEmail = this.getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);

            String email = mLoginEmail.getString("email", "");
            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            String courseId = mUserNameEditText.getText().toString();
            sb.append("&username=");
            sb.append(URLEncoder.encode(courseId, "UTF-8"));

            String courseLongDesc = mBiographyEditText.getText().toString();
            sb.append("&bio=");
            sb.append(URLEncoder.encode(courseLongDesc, "UTF-8"));

            String displayPicture = mDisplayEditText.getText().toString();
            sb.append("&display=");
            sb.append(URLEncoder.encode(displayPicture, "UTF-8"));

            String memePicture = mMemeEditText.getText().toString();
            sb.append("&meme=");
            sb.append(URLEncoder.encode(memePicture, "UTF-8"));

            Log.i(TAG, sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        //should have string now, try updating result
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{sb.toString()});


        return sb.toString();
    }

    /**
     * Private class to handle asynchronous loading of data
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {
        /**
         * PreExecute
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * This function reads and returns the response from the webpage.
         *
         * @param urls url of the webpage
         * @return Returns the output from the webpage
         */
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
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
//        @Override
//        protected void onPostExecute(String result) {
//            // Something wrong with the network or the URL.
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String status = (String) jsonObject.get("result");
//                if (status.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Success!"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                    //On successful login, go to main page
//                    //openMainPage();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error: "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
//                        e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }

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
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                mUser = Match.parseUserJSON(result);
                updateView();
            }
            catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

    }

    public void updateView() {
        Log.i(TAG, "In updateView()");
        if (mUser != null) {
            Log.i(TAG, "mUser not null");
//            mCourseIdTextView.setText(course.getCourseId());
//            mCourseShortDescTextView.setText(course.getShortDescription());
            mUserNameEditText.setText(mUser.getmUsername(), TextView.BufferType.EDITABLE);
            mBiographyEditText.setText(mUser.getmBio(), TextView.BufferType.EDITABLE);
            mDisplayEditText.setText(mUser.getDISPLAY(), TextView.BufferType.EDITABLE);
            mMemeEditText.setText(mUser.getMEME(), TextView.BufferType.EDITABLE);
        }
    }


}
