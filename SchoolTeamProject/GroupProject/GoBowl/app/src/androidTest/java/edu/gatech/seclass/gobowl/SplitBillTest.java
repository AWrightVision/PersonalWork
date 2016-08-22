package edu.gatech.seclass.gobowl;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

import junit.framework.TestCase;

import org.gatech.seclass.gobowl.R;

/**
 * Created by Amanda on 7/8/2016.
 */
public class SplitBillTest extends ActivityInstrumentationTestCase2<SplitBill> {

    private final String pkg;
    private final Class<SplitBill> activityClass;
    SplitBill activity;

    public SplitBillTest(String pkg, Class<SplitBill> activityClass) {
        super(pkg, activityClass);
        this.pkg = pkg;
        this.activityClass = activityClass;
    }

    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testWelcomeTextView() {
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        assertEquals("Split Bill", textView.getText().toString());
    }
    @SmallTest
    public void testGetSplitNumber() {
        String whatIsReturned = activity.splitBill("20.00","2");
        assertEquals("10", whatIsReturned);
    }

}