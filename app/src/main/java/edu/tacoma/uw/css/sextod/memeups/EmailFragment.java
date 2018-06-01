/**
 * Handles sending emails to a matched user. Called from ProfileView, includes one TextEdit field
 * to accept the body of the message, and a button that once pressed will prompt you to open the email
 * application, autofilling the recipient and subject fields.
 *
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */

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
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

import static android.content.ContentValues.TAG;

/**
 * This class is opened up from ProfileViewFragment. Main purpose is for the users to be able to
 * send emails to each other.
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class EmailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public final static String COURSE_ITEM_SELECTED = "course_selected";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Match mUser;
    private String mRecipient;
    private EditText mMessageText;
    private EmailListener mListener;
    private String mUserEmail;

    public EmailFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileEditFragment.
     */
    public static EmailFragment newInstance(String param1, String param2) {
        EmailFragment fragment = new EmailFragment();
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
        View v = inflater.inflate(R.layout.fragment_email, container, false);

        SharedPreferences mUser = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        mUserEmail = mUser.getString("email", "");

        mMessageText = (EditText) v.findViewById(R.id.email_body);

        Button addCourseButton = (Button) v.findViewById(R.id.button_send_email);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO);

                //Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mUserEmail, null));

                //Only email apps should handle this
//                Log.i(TAG, "Email being used in EmailFragment:");
//                Log.i(TAG, mRecipient);

                String[] addresses = new String[1];
                addresses[0] = mRecipient;

                email.setData(Uri.parse("mailto:"));

                email.putExtra(Intent.EXTRA_EMAIL, addresses);
                email.putExtra(Intent.EXTRA_SUBJECT, "MemeUps Message!");
                email.putExtra(Intent.EXTRA_TEXT,
                        "Sender:"+mUserEmail+'\n'+'\n'+"Message: "+ mMessageText.getText().toString()+'\n'+'\n'+"Sent from MemeUps app.");
                /* Send it off to the Activity-Chooser */
                startActivity(email);

            }
        });

        return v;
    }


    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }


//    /**
//     * Called on attach. Context is the activity starts that starts EmailFragment.
//     * @param context Context Activity that EmailFragment is attached to.
//     */
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EmailListener) {
            mListener = (EmailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    /**
     * Handles detaching from the activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * Private class called to retrieve a user's data from the database in the background.
     */
    private class UserAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Runs in the background to execute a php URL and retrieve the results
         * @param urls URL to be executed in the background
         * @return Returns the response from the URL after executing
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

        /**
         * After executing the URL, attempt to parse the JSON string for the user data or display an error.
         * @param result Result to check for success/failure and error messages
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            //Display error
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                mUser = Match.parseUserJSON(result); //Parse the JSON file for Match fields
            }
            catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

    }

    /**
     * On resume attempt to reset the current user being emailed to, if this is null then destroy the fragment.
     */
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            // Set course information based on argument passed
            mRecipient = (String) args.getSerializable("sendEmail");
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
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
    public interface EmailListener {
        void onEmailListener(String email);
    }
}
