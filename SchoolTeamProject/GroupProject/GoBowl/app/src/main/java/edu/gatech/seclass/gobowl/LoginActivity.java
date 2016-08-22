package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;

import edu.gatech.seclass.services.ServicesUtils;

public class LoginActivity extends AppCompatActivity {
    // - generally good practice to define keys for intent extras using package name as prefix
    public final static String EXTRA_USER_ID = "LoginActivity.EXTRA_USER_ID";
    // - class attributes
    private DatabaseHelper db;

    // - screen attributes
    private RadioGroup rgRole;
    private RadioButton rbCustomer;
    private RadioButton rbManager;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // - instantiate elements from the screen
        rgRole = (RadioGroup) findViewById(R.id.rgRole);
        rbCustomer = (RadioButton) findViewById(R.id.rbCustomer);
        rbManager = (RadioButton) findViewById(R.id.rbManager);
        etEmail = (EditText) findViewById(R.id.etEmail);

        // - database
        db = DatabaseHelper.getInstance(this);

        // - data synch (this currently runs each time login is loaded)
        synchServiceUtilWithDb();

        // - set role listener
        configureRoleRadioGroup();

        // - hit the keyboard
        configureEmailEditText();
    }

    /**
     * Handle clickable items on login screen
     *
     * @param view user interface component for login activity
     */
    public void handleClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignIn:
                if (rbManager.isChecked()) {
                    Intent intent = new Intent(this, ManagerActivity.class);
                    startActivity(intent);
                } else if (rbCustomer.isChecked()) {
                    customerSignIn(etEmail.getText().toString());
                } else {
                    // - defensive programming
                    Utils.showToast(this, getString(R.string.loginErrorInvalidRole));
                }
                break;
            case R.id.forgotUsername:
                Utils.showMessage(this, getString(R.string.loginForgotEmail),
                    getString(R.string.loginForgotEmailMessage));
                break;
            case R.id.needAccount:
                Utils.showMessage(this, getString(R.string.loginNeedAccount),
                    getString(R.string.loginNeedAccountMessage));
                break;
            default:
                // - defensive programming
                Utils.showToast(this, getString(R.string.errorUnhandledButton));
                break;
        }
    }

    /**
     *
     * @param email email address entered by the customer (ie the app user)
     */
    private void customerSignIn(String email) {
        if (email == null || email.isEmpty()) {
            Utils.showToast(this, getString(R.string.loginPleaseEnterEmail));
        } else {
            // - check customer email is valid
            Customer customer = db.getCustomerByEmail(email);
            if (customer == null) {
                Utils.showMessage(this, "Invalid Email",
                    "If you are an existing customer, please enter a valid email address.\n\n" +
                    "If you are a new user, please speak with the manager to create a new account.\n");
                return;
            }

            // - forward along to customer activity screen
            Intent intent = new Intent(this, CustomerActivity.class);
            intent.putExtra(EXTRA_USER_ID, customer.getDbIDString());
            startActivity(intent);
        }
    }

    private void configureRoleRadioGroup() {
        rgRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().equals(getString(R.string.loginRbManager))) {
                    etEmail.setFocusable(false);
                    etEmail.setHint(R.string.emptyString);
                    etEmail.setText(R.string.emptyString);
                } else {
                    etEmail.setFocusableInTouchMode(true);
                    etEmail.setHint(R.string.loginHintEmail);
                }
            }
        });
    }

    private void configureEmailEditText() {
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Utils.hideKeyboard(LoginActivity.this, view);
                }
            }
        });
    }

    private void synchServiceUtilWithDb() {
        // - clears out the default customers from ServiceUtils
        ServicesUtils.resetCustomers();

        // - add all customers to Service Utils hashmap
        ArrayList<Customer> customers = db.getAllCustomerData();
        for (Customer c : customers) {
            Utils.addCustomerToServiceUtils(c);
        }
    }
}
