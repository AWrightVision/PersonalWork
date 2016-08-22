package edu.gatech.seclass.gobowl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.gatech.seclass.gobowl.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    // start the login activity before any tests and tear it down after
    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityActivityTestRule =
        new ActivityTestRule<>(LoginActivity.class);

    /**********************************************************************************************/

    @Test
    public void testMainTitle_isDisplayed() throws Exception {
        onView(withId(R.id.tvLoginMainTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogo_isDisplayed() throws Exception {
        onView(withId(R.id.ivLogo)).check(matches(isDisplayed()));
    }

    /**********************************************************************************************/

    @Test
    public void testRole_isDisplayed() throws Exception {
        onView(withId(R.id.tvRole)).check(matches(isDisplayed()));
    }

    /**
     * test that customer role radio button is clickable and default to checked
     */
    @Test
    public void testRoleCustomer_isDisplayed() throws Exception {
        // Customer role defaults to on
        onView(withId(R.id.rbCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.rbCustomer)).check(matches(isClickable()));
        onView(withId(R.id.rbCustomer)).check(matches(isChecked()));
    }

    /**
     * test that manager role radio button is clickable and defaults to unchecked
     */
    @Test
    public void testRoleManager_isDisplayed() throws Exception {
        // Manager role defaults to off
        onView(withId(R.id.rbManager)).check(matches(isDisplayed()));
        onView(withId(R.id.rbManager)).check(matches(isClickable()));
        onView(withId(R.id.rbManager)).check(matches(isNotChecked()));
    }

    @Test
    public void testEmail_isDisplayed() throws Exception {
        onView(withId(R.id.tvEmail)).check(matches(isDisplayed()));
    }

    /**
     * test that email email text view is focused and hint is shows by default (this
     * is valid since the customer radio button is checked by default)
     */
    @Test
    public void testEmailInput_isDisplayed() throws Exception {
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(withHint(R.string.loginHintEmail)));
        onView(withId(R.id.etEmail)).check(matches(isFocusable()));
    }

    /**
     * test that when user clicks the manager the email address input is disabled and when
     * the user then clicks back to the customer the email address field is re-enabled
     */
    @Test
    public void testClickRoleRadioButton_emailInputOnOff() throws Exception {
        onView(withId(R.id.rbManager)).perform(click());
        onView(withId(R.id.etEmail)).check(matches(not(isFocusable())));
        onView(withId(R.id.etEmail)).check(matches(withHint(R.string.emptyString)));
        onView(withId(R.id.etEmail)).check(matches(withText(R.string.emptyString)));

        onView(withId(R.id.rbCustomer)).perform(click());
        onView(withId(R.id.etEmail)).check(matches(isFocusable()));
        onView(withId(R.id.etEmail)).check(matches(withHint(R.string.loginHintEmail)));
    }

    /**********************************************************************************************/

    @Test
    public void testSignInButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonSignIn)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSignIn)).check(matches(isClickable()));
    }

    /**
     * tests that when the user clicks the manager button they are redirected to
     * the manager screen
     */
    @Test
    public void testClickRoleManagerSignIn_launchManager() throws Exception {
        onView(withId(R.id.rbManager)).perform(click());
        onView(withId(R.id.buttonSignIn)).perform(click());
        onView(withId(R.id.tvManagerMainTitle)).check(matches(isDisplayed()));
    }

    /**
     * test that if the user enters a blank Email address that a toast message is shown
     * and the activity stays on the main login page
     */
    @Test
    public void testClickRoleCustomerSignIn_failToLaunch() throws Exception {
        onView(withId(R.id.rbCustomer)).perform(click());
        // empty values are explicitly not allowed
        onView(withId(R.id.etEmail)).perform(typeText(""));
        onView(withId(R.id.buttonSignIn)).perform(click());
        onView(withText(R.string.loginPleaseEnterEmail))
            .inRoot(withDecorView(not(mLoginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()));
        onView(withId(R.id.tvLoginMainTitle)).check(matches(isDisplayed()));
    }

    /**
     * test that when user attempt to sign in as the customer with a valid email that they
     * are redirected to the customer activity
     */
    @Test
    public void testClickRoleCustomerSignIn_launchManager() throws Exception {
        // - navigate to the new customer dialog
        onView(withId(R.id.rbManager)).perform(click());
        onView(withId(R.id.buttonSignIn)).perform(click());
        onView(withId(R.id.buttonNewCustomer)).perform(click());

        // - define credentials
        String inFname = "Joe";
        String inLname = "Bloggs";
        String inEmail = "joe.bloggs@gatech.edu";

        // - enter customer
        onView(withId(R.id.etFirstName)).perform(typeText(inFname));
        onView(withId(R.id.etLastName)).perform(typeText(inLname));
        onView(withId(R.id.etEmail)).perform(typeText(inEmail));
        onView(withText(R.string.submit)).perform(click());

        // - return to the login screen
        onView(withText(R.string.mngrMainTitle)).perform(pressBack());

        // - login in as the customer
        onView(withId(R.id.rbCustomer)).perform(click());
        onView(withId(R.id.etEmail)).perform(typeText(inEmail));
        onView(withId(R.id.etEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.buttonSignIn)).perform(click());

        // - check on the customer activity
        onView(withId(R.id.tvCustomerMainTitle)).check(matches(isDisplayed()));
    }


    /**********************************************************************************************/

    @Test
    public void testForgotYourEmail_isDisplayed() throws Exception {
        onView(withId(R.id.forgotUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.forgotUsername)).check(matches(isClickable()));
    }

    @Test
    public void clickForgotYourEmail_showsAlert() throws Exception {
        onView(withId(R.id.forgotUsername)).perform(click());
        onView(withText(R.string.loginForgotEmail)).check(matches(isDisplayed()));
        onView(withText(R.string.loginForgotEmailMessage)).check(matches(isDisplayed()));
    }

    @Test
    public void testNeedAnAccount_isDisplayed() throws Exception {
        onView(withId(R.id.needAccount)).check(matches(isDisplayed()));
        onView(withId(R.id.needAccount)).check(matches(isClickable()));
    }

    @Test
    public void clickNeedAnAccount_showsAlert() throws Exception {
        onView(withId(R.id.needAccount)).perform(click());
        onView(withText(R.string.loginNeedAccount)).check(matches(isDisplayed()));
        onView(withText(R.string.loginNeedAccountMessage)).check(matches(isDisplayed()));
    }
}