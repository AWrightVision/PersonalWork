package edu.gatech.seclass.gobowl;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import edu.gatech.seclass.services.ServicesUtils;

public class Utils {
    /**
     * show a toast
     * @param activity
     * @param msg
     */
    public static String showToast(Activity activity, String msg) {
        // - should stop using getApplicationContext here
        //   http://stackoverflow.com/questions/7298731/when-to-call-activity-context-or-application-context
        Context context = activity.getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
        return msg;
    }

    /**
     * hide keyboard
     * Note that the parent element of the activity must have: android:clickable="true" & android:focusableInTouchMode="true"
     * http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
     *
     * @param activity
     * @param view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * show a popup message on the screen
     *
     * @param title
     * @param message
     */
    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /**
     * convert integer to a 4-digit hexadecimal
     */
    public static String intToHex(int i) {
        return String.format("%04x", i);
    }


    public static void addCustomerToServiceUtils(Customer customer) {
        ServicesUtils.addCustomer(
            customer.getFirstName(),
            customer.getLastName(),
            String.valueOf((long) (Math.random() * 10000000000000000L)),
            "12312020",
            String.valueOf((int) (Math.random() * 1000)),
            customer.getCustomerID()
        );
    }
}
