package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.seclass.services.ServicesUtils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ManagerActivityTest {

    // mock customer detials
    private String FIRST_NAME = "Joe";
    private String LAST_NAME = "Bloggs";
    private String EMAIL = "joe.bloggs@gatech.edu";

    // start the login activity before any tests and tear it down after
    @Rule
    public ActivityTestRule<ManagerActivity> mActivityRule =
        new ActivityTestRule<>(ManagerActivity.class);

    // database
    private static DatabaseHelper db;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        db = DatabaseHelper.getInstance(mActivityRule.getActivity());
        db.deleteAllCustomerTableRecords();
        ServicesUtils.resetCustomers();
    }

    @After
    public void tearDown() throws Exception {
        db.deleteAllCustomerTableRecords();
        ServicesUtils.resetCustomers();
    }

    /**********************************************************************************************/

    @Test
    public void testMainTitle_isDisplayed() throws Exception {
        onView(withId(R.id.tvManagerMainTitle)).check(matches(isDisplayed()));
    }

    /**
     * test that the total customers title is displayed, that the text view is present
     * and that the text view is populated with zero (based on setUp)
     */
    @Test
    public void testTotalCustomers_idDisplayed() throws Exception {
        onView(withId(R.id.tvTitleTotalCustomers)).check(matches(withText(R.string.mngrTotalCustomers)));
        onView(withId(R.id.tvTotalCustomers)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("0"))));
    }

    /**
     * test that the total VIP customers title is displayed, that the text view is present
     * and that the text view is populated with zero (based on setUp)
     */
    @Test
    public void testVipCustomers_idDisplayed() throws Exception {
        onView(withId(R.id.tvTitleVipCustomers)).check(matches(withText(R.string.mngrVipCustomers)));
        onView(withId(R.id.tvVipCustomers)).check(matches(isDisplayed()));
        onView(withId(R.id.tvVipCustomers)).check(matches(withText(containsString("0"))));
    }


    /***********************************************************************************************
     * New Customer
     **********************************************************************************************/

    @Test
    public void testNewCustomerButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonNewCustomer)).check(matches(isClickable()));
    }

    /**
     * test that new customer dialog is present after clicking the new customer button
     */
    @Test
    public void testAddNewCustomer_dialog() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.tvNewCustomerMainTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.etFirstName)).check(matches(isDisplayed()));
        onView(withId(R.id.etFirstName)).check(matches(withHint(R.string.mngrNewCustomerHintFirstName)));

        onView(withId(R.id.etLastName)).check(matches(isDisplayed()));
        onView(withId(R.id.etLastName)).check(matches(withHint(R.string.mngrNewCustomerHintLastName)));

        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(withHint(R.string.mngrNewCustomerHintEmail)));
    }

    /**
     * test valid customer details are successful in inserting into the database
     */
    @Test
    public void testAddNewCustomer_success() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.submit)).perform(click());
        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("1"))));
    }

    /**
     * test that canceling in the new customer dialog does not add any customers
     */
    @Test
    public void testAddNewCustomer_cancel() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("0"))));
    }

    /**
     * Test that invalid entry (eg missing email value) will not add a customer
     */
    @Test
    public void testAddNewCustomer_fail() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withText(R.string.submit)).perform(click());
        onView(withText(R.string.mngrNewCustomerMissingEmail))
            .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()));
        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("0"))));
    }


    /***********************************************************************************************
     * Print Card
     **********************************************************************************************/

    @Test
    public void testPrintCustomerButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonPrintCard)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPrintCard)).check(matches(isClickable()));
    }

    /**
     * test that new customer dialog is present after clicking the new customer button
     */
    @Test
    public void testPrintCard_dialog() throws Exception {
        onView(withId(R.id.buttonPrintCard)).perform(click());
        onView(withId(R.id.tvPrintCardMainTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerCustomer)).check(matches(isDisplayed()));
    }

    /**
     * test that nothing selected will not print anything
     */
    @Test
    public void testPrintCard_nothingPrinted() throws Exception {
        onView(withId(R.id.buttonPrintCard)).perform(click());
        onView(withText(R.string.submit)).perform(click());
        onView(withText(R.string.mngrPrintCardNothingSelectedToastMsg))
            .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()));
    }

    /**
     * test that card is printed
     */
    @Test
    public void testPrintCard_success() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.submit)).perform(click());

        Customer customer = db.getCustomerByEmail(EMAIL);
        String spinnerText = customer.uiString();

        onView(withId(R.id.buttonPrintCard)).perform(click());
        onView(withId(R.id.spinnerCustomer)).perform(click());
        onView(withText(spinnerText)).inRoot(isPlatformPopup()).perform(click());
        onView(withText(R.string.submit)).perform(click());

        onView(withText(R.string.mngrPrintCardSuccessToastMsg))
            .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()));
    }

    /**
     * test that back on manager main activity after pressing cancel
     */
    @Test
    public void testPrintCard_cancel() throws Exception {
        onView(withId(R.id.buttonPrintCard)).perform(click());
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.tvManagerMainTitle)).check(matches(isDisplayed()));
    }


    /***********************************************************************************************
     * Edit Customer
     **********************************************************************************************/

    @Test
    public void testEditCustomerButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonEditCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonEditCustomer)).check(matches(isClickable()));
    }

    /**
     * test that edit customer dialog is present after clicking the new customer button
     */
    @Test
    public void testEditCustomer_dialog() throws Exception {
        onView(withId(R.id.buttonEditCustomer)).perform(click());
        onView(withId(R.id.tvEditCustomerMainTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerCustomer)).check(matches(isDisplayed()));

        onView(withId(R.id.etFirstName)).check(matches(isDisplayed()));
        onView(withId(R.id.etFirstName)).check(matches(withHint(R.string.mngrEditCustomerHintFirstName)));

        onView(withId(R.id.etLastName)).check(matches(isDisplayed()));
        onView(withId(R.id.etLastName)).check(matches(withHint(R.string.mngrEditCustomerHintLastName)));

        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(withHint(R.string.mngrEditCustomerHintEmail)));
    }

    /**
     * test that customer is edited
     */
    @Test
    public void testEditCustomer_success() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.submit)).perform(click());

        Customer customer = db.getCustomerByEmail(EMAIL);
        String spinnerText = customer.uiString();

        onView(withId(R.id.buttonEditCustomer)).perform(click());
        onView(withId(R.id.spinnerCustomer)).perform(click());
        onView(withText(spinnerText)).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.etFirstName)).perform(replaceText(FIRST_NAME.toUpperCase()));
        onView(withText(R.string.submit)).perform(click());

        onView(withText(R.string.mngrEditCustomerUpdatedToastMsg))
            .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()));
    }

    /**
     * test that back on manager main activity after pressing cancel
     */
    @Test
    public void testEditCustomer_cancel() throws Exception {
        onView(withId(R.id.buttonEditCustomer)).perform(click());
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.tvManagerMainTitle)).check(matches(isDisplayed()));
    }


    /***********************************************************************************************
     * Delete Customer
     **********************************************************************************************/

    /**
     * test that delete customer button is present and clickable
     */
    @Test
    public void testDeleteCustomerButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonDeleteCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDeleteCustomer)).check(matches(isClickable()));
    }

    /**
     * test that delete customer dialog is present after clicking the new customer button
     */
    @Test
    public void testDeleteCustomer_dialog() throws Exception {
        onView(withId(R.id.buttonDeleteCustomer)).perform(click());
        onView(withId(R.id.tvDeleteCustomerMainTitle)).check(matches(isDisplayed()));
    }

    /**
     * test that customer is deleted
     */
    @Test
    public void testDeleteCustomer_success() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.submit)).perform(click());
        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("1"))));

        Customer customer = db.getCustomerByEmail(EMAIL);
        String spinnerText = customer.uiString();

        onView(withId(R.id.buttonDeleteCustomer)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(spinnerText))).perform(click());
        onView(withText(R.string.submit)).perform(click());

        onView(withId(R.id.tvTotalCustomers)).check(matches(withText(containsString("0"))));
    }

    /**
     * test that back on manager main activity after pressing cancel
     */
    @Test
    public void testDeleteCustomer_cancel() throws Exception {
        onView(withId(R.id.buttonDeleteCustomer)).perform(click());
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.tvManagerMainTitle)).check(matches(isDisplayed()));
    }


    /***********************************************************************************************
     * Show All Customer
     **********************************************************************************************/

    /**
     * test that show all customers button is present and clickable
     */
    @Test
    public void testAllCustomerButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonAllCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonAllCustomer)).check(matches(isClickable()));
    }

    /**
     * test that show all customers button is present and clickable
     */
    @Test
    public void testAllCustomerButton_empty() throws Exception {
        onView(withId(R.id.buttonAllCustomer)).perform(click());
        onView(withText(R.string.mngrShowAllCustomerTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.mngrShowAllCustomerEmpty)).check(matches(isDisplayed()));
    }

    /**
     * test that show all customers button is present and clickable
     */
    @Test
    public void testAllCustomerButton_listed() throws Exception {
        onView(withId(R.id.buttonNewCustomer)).perform(click());
        onView(withId(R.id.etFirstName)).perform(typeText(FIRST_NAME));
        onView(withId(R.id.etLastName)).perform(typeText(LAST_NAME));
        onView(withId(R.id.etEmail)).perform(typeText(EMAIL));
        onView(withText(R.string.submit)).perform(click());


        onView(withId(R.id.buttonAllCustomer)).perform(click());
        onView(withText(R.string.mngrShowAllCustomerTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.mngrShowAllCustomerEmpty)).check(doesNotExist());
    }

    /***********************************************************************************************
     * Delete All Customer
     **********************************************************************************************/

    /**
     * test that delete all customers button is present and clickable
     */
    @Test
    public void testDeleteAllButton_isDisplayed() throws Exception {
        onView(withId(R.id.buttonDeleteAllCustomer)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDeleteAllCustomer)).check(matches(isClickable()));
    }
}