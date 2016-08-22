package edu.gatech.seclass.gobowl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.gatech.seclass.services.PrintingService;
import edu.gatech.seclass.services.ServicesUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by jake on 7/8/16.
 */
public class CustomerActivityTest {
    private Activity activity;
    private static DatabaseHelper db;
    // - Activity needs to be set to not automatically call the Activity, so intent values can be passed
    // - http://blog.xebia.com/android-intent-extras-espresso-rules/
    @Rule
    public ActivityTestRule<CustomerActivity> mActivityRule =
            new ActivityTestRule<>(CustomerActivity.class, true, false);






    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }


    @Before
    public void setUp() throws Exception {
        //set up mock user
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        db = DatabaseHelper.getInstance(targetContext);
        db.deleteAllCustomerTableRecords();
        db.deleteAllLanesCustomers();
        db.deleteAllLanesPlayers();
        ServicesUtils.resetCustomers();

        addCustomerTest("a","b","a");
        addCustomerTest("a","b","b");
        addCustomerTest("a","b","c");
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        db = DatabaseHelper.getInstance(targetContext);
        db.deleteAllCustomerTableRecords();
        db.deleteAllLanesCustomers();
        db.deleteAllLanesPlayers();
        ServicesUtils.resetCustomers();
    }

    @Test
    public void testRequestLane_Show_Fragment() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        Intent intent = new Intent(targetContext, CustomerActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USER_ID, db.getCustomerByEmail("a").getCustomerID());
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.buttonRequestLane)).perform(click());
        onView(withId(R.id.lane_request_players_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.playersNumBox)).check(matches(isDisplayed()));
        onView(withId(R.id.player_submit_button)).check(matches(isDisplayed()));
        onView(withId(R.id.players_cancel_button)).check(matches(isDisplayed()));

    }

    /**
     * test valid request one one player
     *
     */
    //TODO QR button has a chance of returning an error, not sure how to handle that

    @Test
    public void testAddNewLane_success() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();


        Intent intent = new Intent(targetContext, CustomerActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USER_ID, db.getCustomerByEmail("a").getCustomerID());
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.buttonRequestLane)).perform(click());

        onView(withId(R.id.playersNumBox)).perform(typeText("1"));
        onView(withId(R.id.playersNumBox)).perform(closeSoftKeyboard());
        onView(withId(R.id.player_submit_button)).perform(click());



        onView(withId(R.id.qr_scanner_button)).perform(click());

        onView(withId(R.id.customer_lanes_text_view)).check(matches(withText(containsString("1"))));

    }
    @Test
    public void testAddNewLane_cancel() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();


        Intent intent = new Intent(targetContext, CustomerActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USER_ID, db.getCustomerByEmail("a").getCustomerID());
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.buttonRequestLane)).perform(click());

        onView(withId(R.id.playersNumBox)).perform(typeText("1"));
        onView(withId(R.id.playersNumBox)).perform(closeSoftKeyboard());
        onView(withId(R.id.players_cancel_button)).perform(click());


        onView(withId(R.id.active_game_customer_text_set)).check(matches(withText(containsString("No"))));

    }


    public void addCustomerTest(String firstName, String lastName, String email){
        // - attempt to write to the database
        db = DatabaseHelper.getInstance(InstrumentationRegistry.getInstrumentation()
                .getTargetContext());
        DatabaseHelper.CustomerTable.Status stat = db.insertCustomerData(firstName, lastName, email);

        Customer customer = db.getCustomerByEmail(email);
        if (stat.getCode() == DatabaseHelper.CustomerTable.Status.SUCCESS.getCode() && customer != null) {
            // - add to the libs

            Utils.addCustomerToServiceUtils(customer);


        }
    }


}
