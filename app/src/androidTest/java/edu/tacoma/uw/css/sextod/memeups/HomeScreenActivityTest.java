/**
 * This instrumentation test is for the Home Screen Activity, which tests logging in to view the home screen,
 * the 4 buttons that open our different activities, and the logout button.
 */

package edu.tacoma.uw.css.sextod.memeups;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeScreenActivityTest
{
    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<HomeScreenActivity> mActivityRule = new ActivityTestRule<>(
            HomeScreenActivity.class);

    /**
     * Log out first incase the use is still logged in, then log in
     */
    @Before
    public void testLoginActivity() {
        onView(withId(R.id.action_logout))
                .perform(click());

        onView(withId(R.id.loginEmail))
                .perform(typeText("travis@memeups.com")); //TO DO – Change to a valid email

        onView(withId(R.id.registerPassword))
                .perform(typeText("pass123")); //TO DO – Change to a valid pwd

        onView(withId(R.id.signinbutton))
                .perform(click());
    }

    /**
     * Checks if we're on the main screen
     */
    @Test
    public void testMainText() {
        onView(allOf(withId(R.id.matchbutton)
                , withText("Find matches")))
                .check(matches(isDisplayed()));
    }

    /**
     * Checks if we're on the quiz screen
     */
    @Test
    public void testQuizButton() {
        onView(withId(R.id.quizbutton))
                .perform(click());

        onView(allOf(withId(R.id.submitbutton)
                , withText("Submit")))
                .check(matches(isDisplayed()));

    }

    /**
     * Checks if we're on the match finding screen
     */
    @Test
    public void testFindMatchButton() {
        onView(withId(R.id.matchbutton))
                .perform(click());

        onView(allOf(withId(R.id.list)))
                .check(matches(isDisplayed()));

    }

    /**
     * Checks if we're on the view match screen
     */
    @Test
    public void testViewMatchButton() {
        onView(withId(R.id.viewmatchbutton))
                .perform(click());

        onView(allOf(withId(R.id.list)))
                .check(matches(isDisplayed()));
    }

    /**
     * Checks if we're on the profile screen
     */
    @Test
    public void testProfileButton() {
        onView(withId(R.id.profilebutton))
                .perform(click());

        onView(allOf(withId(R.id.collectionbutton)
                , withText("Edit Profile")))
                .check(matches(isDisplayed()));
    }

    /**
     * Checks to see if we're on the login page
     */
    @Test
    public void testLogoutButton()
    {
        onView(withId(R.id.action_logout))
                .perform(click());
        onView(allOf(withId(R.id.signinbutton)
                , withText("Sign in")))
                .check(matches(isDisplayed()));
    }
}
