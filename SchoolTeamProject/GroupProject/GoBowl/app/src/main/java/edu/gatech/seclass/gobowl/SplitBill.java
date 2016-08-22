package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.gatech.seclass.services.CreditCardService;
import edu.gatech.seclass.services.PaymentService;


public class SplitBill extends AppCompatActivity{

    private TextView totalDisplay;
    private EditText splitNumber;
    private TextView splitDisplay;
    private Button splitTotal;
    private float discountedTotalCost; //Nicholas: this is the discounte total cost calculated in the previous activity
    private int custId;

    private boolean billIsSplit = false;
    private double totalSplitCost = 0.0;

    private String email;
    private Customer customer;
    private DatabaseHelper db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitbill);  //Nicholas this crashes. There is something going on the layout selection

        totalDisplay = (TextView) findViewById(R.id.totalDisplay);
        splitNumber = (EditText) findViewById(R.id.splitNumber);
        splitDisplay = (TextView) findViewById(R.id.splitDisplay);
        splitTotal = (Button) findViewById(R.id.splitTotal);

        // - set the Checkout name
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        db = DatabaseHelper.getInstance(this);
        customer = db.getCustomerByEmail(email);

        //Nicholas commented out lines 44 and 46
        //String checkoutName = intent.getStringExtra("SplitBill");
        TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText(checkoutName);

        //Date displayed
        String currentDate = DateFormat.getDateInstance().format(new Date());
        textView.setText(currentDate);

       //Nicholas
        // here the total to split
        discountedTotalCost = intent.getFloatExtra("discountedTotalCost", (float) 0.0);
        custId = intent.getIntExtra("id",-1);
        //Here the Total will be displayed on create

        totalDisplay.setText(Double.toString(discountedTotalCost));
    }

    public void handleClick(View view) {

        switch (view.getId()) {
            //happens when split button is pressed
            case R.id.splitTotal:
                //strings get total from textview by id
                String totalIs = getTotalDisplay();
                String splitNumberIs = getSplitNumber();
                //if-else statements check if totalIs and splitNumber are empty. If the are not then proceed to split bill
                if(totalIs.isEmpty()){
                    Utils.showToast(this, "You have NO Payment Available!");
                }
                else if(splitNumberIs.isEmpty()){
                    Utils.showToast(this, "Please Enter a Split Number!");
                }
                else {
                    //goes to split bill
                    String convertedBill = splitBill(totalIs, splitNumberIs);
                    splitDisplay.setText(convertedBill);
                }
                break;
            //happens when swipe button is pressed
            case R.id.swipeButton:
                if (!billIsSplit) {
                    Utils.showToast(this, "Split the bill before swiping card");
                } else {
                    // - loop over customers and scan cards
                    SimpleDateFormat ft = new SimpleDateFormat("MMddyyyy");
                    int splitWays = Integer.parseInt(getSplitNumber());
                    String toastMsg = "";
                    for (int i=0; i<splitWays; i++) {
                        // - reads a random customer (keep tryingitn
                        String cust = CreditCardService.readCreditCard();
//                        Utils.showToast(SplitBill.this, cust);
                        if (!cust.equals("ERR")) {
                            String[] parts = cust.split("#");
                            // addCustomer("Ralph", "Hapschatt", "9328895960019440", "12312016", "111", "86ff");
                            // processPayment(String firstName, String lastName, String ccNumber, Date expirationDate, String securityCode, double amount)
                            boolean stat = false;
                            try {
                                Date date = ft.parse(parts[3]);
                                stat = PaymentService.processPayment(parts[0], parts[1], parts[2], date, parts[4], totalSplitCost);
                            } catch (ParseException e) {
                                System.out.println("Unparseable using " + ft);
                                toastMsg += "> failed to processed for split # " + Integer.toString(i+1) + "\n";
                            }
                            if (stat) {
                                toastMsg += "> processed for split # " + Integer.toString(i+1) + "\n";
                            } else {
                                toastMsg += "> failed to processed for split # " + Integer.toString(i+1) + "\n";
                            }
                        } else {
                            toastMsg += "> failed to processed for split # " + Integer.toString(i+1) + "\n";
                        }
                    }
                    Utils.showToast(SplitBill.this, toastMsg.trim());

                    // *** clear customer from tables ****
                    db.deleteAllCustomerRecordsAcrossTables(customer.getDbIDString());

                    // go back to customer screen
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                Utils.showToast(this, "ERROR - unhandled button");
                break;
        }
    }

    //Calculates split
    public String splitBill(String totalIs, String splitNumberIs) {
        double total;
        int split;
        double result;

        //total = Integer.parseInt(totalIs);
        total = Double.parseDouble(totalIs);
        split = Integer.parseInt(splitNumberIs);

        //if split is zero then return an error statement informing the user to enter a valid number
        if (split == 0) {
            String Error = Utils.showToast(this, "ERROR - You Must enter a number greater than 0!");
            return Error;
        }

        //if total is zero then return an noPayment statement informing the user they have no payment
        if (total < 0.00001) {
            String noPayment = Utils.showToast(this, "You have NO Payment Available!");
            return noPayment;
        }

        result = total / split;
        totalSplitCost = result;

        String convertedTotalIs = Double.toString(result);
        billIsSplit = true;

        return convertedTotalIs;
    }

    //get split number entered by user
    public String getSplitNumber() {
        return splitNumber.getText().toString();
    }

    //get the total displayed to the user
    public String getTotalDisplay() {
        return  totalDisplay.getText().toString();
    }
}
