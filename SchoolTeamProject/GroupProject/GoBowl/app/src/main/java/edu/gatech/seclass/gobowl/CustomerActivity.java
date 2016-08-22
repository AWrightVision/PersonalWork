package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.StringTokenizer;

import edu.gatech.seclass.services.QRCodeService;

public class CustomerActivity extends AppCompatActivity {

    public static int MIN_PLAYERS_PER_LANE = 1;
    public static int MAX_PLAYERS_PER_LANE = 8;

    private ArrayList<String> QRList = new ArrayList<String>();

    private int players;
    private int checkedPlayers = 0;
    private  boolean isRequesting = false;

    DatabaseHelper db;
    Customer customer;

    // - screen elements
    private TextView tvCustomerMainTitle;
    private TextView textViewID;
    private TextView tvCustomerEmail;
    private TextView tvCustomerVipStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Intent intent = getIntent();

        // - database
        db = DatabaseHelper.getInstance(this);

        // - look up customer by ID
        customer = db.getCustomerById(intent.getStringExtra(LoginActivity.EXTRA_USER_ID));

        // - instantiate screen object
        tvCustomerMainTitle = (TextView) findViewById(R.id.tvCustomerMainTitle);
        textViewID = (TextView) findViewById(R.id.tvCustomerId);
        tvCustomerEmail = (TextView) findViewById(R.id.tvCustomerEmail);
        tvCustomerVipStatus = (TextView) findViewById(R.id.tvCustomerVipStatus);

        // - update customer details on screen
        updateScreenCustomerDetails(customer);
    }

    /**
     * Handle clickable items on login screen
     * This covers both the two buttons (checkout and request lane) that are show by default
     * as well as the buttons that are displayed dynamically based on user activity (eg when you
     * request a lane a cancel and submit button will appear)
     *
     * @param view user interface component for login activity
     */
    public void handleClick(View view) {
        Fragment frag;
        FragmentTransaction transaction;

        switch (view.getId()) {
            case R.id.buttonCheckOut:
                // - get all lanes that the customer has checked out
                ArrayList<Integer> customerLanes =  db.getLanesByCustomer(customer.getEmail());

                if (customerLanes.size() == 0) {
                    Utils.showToast(this, getString(R.string.custCheckoutWithoutLaneToastMsg));
                } else {
                    Intent intent = new Intent(this, CheckoutActivity.class);
                    intent.putIntegerArrayListExtra("lanes", customerLanes);
                    intent.putExtra("email", customer.getEmail());
                    startActivity(intent);
                }
                break;

            // request opens the requestPlayerFragment
            case R.id.buttonRequestLane:
                // - launch request
                if(!isRequesting) {
                    RequestPlayersFrag requestFrag = new RequestPlayersFrag();
                    isRequesting = true;
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment_container_customer, requestFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            // - submit button of request players fragment
            // Although button is on the requestPlayerFragment, player selects up to 8 players,
            // fragment is removed.
            case R.id.player_submit_button:
                // - get the number of players the customer entered for request lane
                String numPlayersText = ((EditText) findViewById(R.id.playersNumBox)).getText().toString();

                // - check that user entered a valid number of players (not missing and in range)
                if((!numPlayersText.isEmpty()) &&
                    Integer.parseInt(numPlayersText) <= MAX_PLAYERS_PER_LANE &&
                    Integer.parseInt(numPlayersText) >= MIN_PLAYERS_PER_LANE ) {

                    // - parse the number of players to int and assign to global variable
                    players =  Integer.parseInt(numPlayersText);

                    // - get the total number of customers in the database
                    int totalCustomers = db.getCustomerTableRows();
                    int totalPlayers = db.getPlayerLaneTotalRows();
                    int potentialPlayers = totalCustomers - totalPlayers;

                    if (potentialPlayers < 0 ) {
                        // - something went wrong
                        Utils.showMessage(this,
                            "ERROR - Database out of Synch",
                            "To correct this, please login as the manager and delete all customers");
                    }
                    else if (players > totalCustomers - totalPlayers) {
                        // - the different between the total customers in the system and those in the
                        //   player lane table should give us the number of customers available to play
                        Utils.showToast(this, getString(R.string.custNotEnoughPlayersToastMsg));
                    }
                    else {
                        frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container_customer);
                        getSupportFragmentManager().beginTransaction().remove(frag).commit();
                        getSupportFragmentManager().popBackStack();
                        Utils.hideKeyboard(CustomerActivity.this, ((EditText) findViewById(R.id.playersNumBox)));

                        QRScanFrag qrFrag = new QRScanFrag();

                        transaction = getSupportFragmentManager().beginTransaction();

                        transaction.add(R.id.fragment_container_customer, qrFrag);

                        transaction.addToBackStack(null);

                        transaction.commit();
                    }
                } else {
                    Utils.showToast(this, getString(R.string.custPlayerLimitErrorToastMsg));
                }
                break;

            // - cancel button of request players fragment
            case R.id.players_cancel_button:
                frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container_customer);

                isRequesting = false;
                checkedPlayers = 0;
                QRList.clear();
                if(frag != null) {
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                    getSupportFragmentManager().popBackStack();
                }
                break;

            case R.id.qr_scanner_button:
                // I've decided to do all the lifting for the scanning in the  click listener.
                String qr = QRCodeService.scanQRCode();
                TextView text = (TextView)findViewById(R.id.qr_text);

                if (qr.equals("ERR")) {
                    text.setText("Error scanning card for player "+ String.valueOf(checkedPlayers+1) + "\n" +
                        "Please keep rescanning card until it accepts");
                }
                else if (QRList.contains(qr)) {
                    text.setText("Player has already been scanned, waiting for player not already scanned");
                }
                else if ((db.getPlayerLaneFromId(Integer.parseInt(qr, 16) ) != -1)) {
                    text.setText("Player is already playing a game");
                }
                else {
                    this.QRList.add(qr);
                    checkedPlayers++;
                    text.setText("Scan complete. Please scan card for player "+String.valueOf(checkedPlayers+1)+".");

                    if (checkedPlayers == players) {
                        frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container_customer);

                        if(frag != null) {
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();
                            getSupportFragmentManager().popBackStack();
                        }
                        // request is completed, reset values for another lane request

                        Lane lane = AlleyLane.getInstance().getNextLane(QRList);
                        db = DatabaseHelper.getInstance(view.getContext());
                        Calendar timeCal = Calendar.getInstance();
                        timeCal.setTimeInMillis(System.currentTimeMillis());

                        if (db.insertLaneCustomerData(customer.getDbID(),timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE), lane.getLaneNum() ) ) {
                            ArrayList<Integer> lanes = db.getLanesByCustomer(Integer.parseInt(customer.getDbIDString(), 16));
                            int lastLaneForCustomer = lanes.get(lanes.size() - 1);
                            Utils.showToast(this, "Lane Requested - " + String.valueOf(lastLaneForCustomer));

                            for (String QRcode : this.QRList) {
                                if (!db.insertPlayerLaneData(lane.getLaneNum(), Integer.parseInt(QRcode, 16))) {
                                    Utils.showToast(this, "ERROR - failed to insert player with id " + QRcode);
                                } else {
                                    Utils.showToast(this, "Player " + QRcode + " gets lane " + lane.getLaneNum());
                                }

                            }
                            TextView lanesText = (TextView) findViewById(R.id.customer_lanes_text_view);


                            if (lanesText.getText().toString().equals(getString(R.string.custLanesTextViewNone))) {
                                /*TODO to get the lane, I simply take the lanes for the customer and
                                TODO return the last one in the list... this is probably not the
                                TODO best way to do this, should try to fix it */
                                lanesText.setText(String.valueOf(lastLaneForCustomer));
                                ((TextView) findViewById(R.id.active_game_customer_text_set)).setText(getString(R.string.custActiveGameTextViewYes));
                            } else
                                lanesText.setText(lanesText.getText().toString() + ", " + String.valueOf(lastLaneForCustomer));
                        }
                        else {
                            Utils.showToast(this, getString(R.string.custErrFailedToScan));
                        }
                        checkedPlayers = 0;
                        isRequesting = false;
                        QRList.clear();
                    }
                }
                break;

            default:
                Utils.showToast(this, getString(R.string.errorUnhandledButton));
                break;
        }

    }

    /**
     * cancel the request transaction and reset all the data necessary
     * http://stackoverflow.com/questions/2592037/is-there-a-default-back-keyon-device-listener-in-android
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && isRequesting) {
            // do something on back.
            isRequesting = false;
            checkedPlayers = 0;
            QRList.clear();
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container_customer);

            if(frag != null)
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    /**
     * update customer details on screen
     *
     * @param c customer object that will be used for updating the screen
     */
    private void updateScreenCustomerDetails(Customer c) {
        // - update main title with customers full name
        tvCustomerMainTitle.setText(customer.toString());

        // - update screen with customer ID
        textViewID.setText(customer.getCustomerID());

        // - update screen with customer email
        tvCustomerEmail.setText(customer.getEmail());

        // - update screen with customer VIP status
        tvCustomerVipStatus.setText(customer.vipStatusToString());

        // - update avctive game and lane fields
    }

}
