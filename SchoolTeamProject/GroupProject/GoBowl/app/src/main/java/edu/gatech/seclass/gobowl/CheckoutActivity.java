package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import edu.gatech.seclass.gobowl.DatabaseHelper;
import edu.gatech.seclass.gobowl.LoginActivity;
import edu.gatech.seclass.gobowl.Utils;

public class CheckoutActivity extends AppCompatActivity  implements ScoreCapture.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener  {

    private ArrayList<Integer> lanes;
    private Spinner laneSpinner;
    private Button saveScores, splitBill;
    private TextView totalBill;
    private EditText discounts;
    private CustomOnItemSelectedListener spinned;
    private float totalCost, discount, discountedTotalCost;
    private String email;
    private  boolean isRequesting = false;
    private TextView tv;
    private AtomicInteger p = new AtomicInteger(0);
    private ArrayList<Integer> pl;
    private int ln ;

    private FragmentTransaction transaction;

    private DatabaseHelper db;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        totalCost = 0;
        discount = 0;
        discountedTotalCost = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        db = DatabaseHelper.getInstance(this);

        // get the number of lanes
        Intent intent = getIntent();
        lanes = intent.getIntegerArrayListExtra("lanes");
        email = intent.getStringExtra("email");

        customer = db.getCustomerByEmail(email);

        //Loading the spinner with the lane values
        laneSpinner = (Spinner) findViewById(R.id.laneSpinner);
        // Application of the Array to the Spinner
        ArrayAdapter<Integer> laneSpinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lanes);
        laneSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        laneSpinner.setAdapter(laneSpinnerArrayAdapter);

        // add end event listener to know when the spinner is selected
        //spinned = new CustomOnItemSelectedListener();
        //laneSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        laneSpinner.setOnItemSelectedListener(this);

        saveScores = (Button) findViewById(R.id.saveScores);
        splitBill = (Button) findViewById(R.id.splitBill);

        discounts = (EditText) findViewById(R.id.discounts);
        totalBill = (TextView) findViewById(R.id.totalBill);
    }


    public void hClick(View view) {

        switch (view.getId()) {
            case R.id.saveScores:
                p.set(0);
                String playerName = db.getCustomerNameById(pl.get(p.get()));
                ScoreCapture sc = ScoreCapture.newInstance(playerName, pl.get(p.get()));
                // configure link
                // Bundle bundle = new Bundle();
                //bundle.putString("playerName", playerName);
                //bundle.putInt("playerID", p.get());
                //sc.setArguments(bundle);

                transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(  R.id.fragment_container_score, sc);

                transaction.addToBackStack(null);

                transaction.commit();
                break;
            case R.id.splitBill:
                if(totalBill.equals("--")) {
                    Utils.showToast(this, "Please select a lane!");

                }

                if(totalBill.getText().equals(0)) {
                    Utils.showToast(this, "You have no payments!");
                } else {
                    Intent intent = new Intent(this, SplitBill.class);
                    intent.putExtra("discountedTotalCost", discountedTotalCost);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                break;

            default:
                Utils.showToast(this, "ERROR - unhandled button");
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //if (spinned.getSelectedItem()) {
        long endTime = System.currentTimeMillis();
        ln = (int) parent.getItemAtPosition(pos);
        pl = db.getPlayersByLane(ln);
        totalCost = (float) 0.0;

        int numPlayers = pl.size();
        Customer cust = db.getCustomerByEmail(email);
        int strHr =  db.getRequestHourByLane(ln,cust.getDbID());
        float strMin = (float) db.getRequestMiniteByLane(ln,cust.getDbID())/60;

        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTimeInMillis(endTime);
        int endHr = endTimeCal.get(Calendar.HOUR_OF_DAY);
        float endMin = (float) endTimeCal.get(Calendar.MINUTE) / 60;

        for (int i = strHr; i < endHr; i++) {
            totalCost = totalCost + numPlayers * db.getRate(endTimeCal.get(Calendar.DAY_OF_WEEK), i);
        }
        if ( strHr < endHr) {
            totalCost = totalCost + numPlayers * db.getRate(endTimeCal.get(Calendar.DAY_OF_WEEK), strHr) * (1 - strMin);
            totalCost = totalCost + numPlayers * db.getRate(endTimeCal.get(Calendar.DAY_OF_WEEK), endHr) * endMin;
        } else if (strHr == endHr) {
            totalCost = totalCost + numPlayers * db.getRate(endTimeCal.get(Calendar.DAY_OF_WEEK), endHr) * (endMin - strMin);
        }

        discount = totalCost * (float) 0.1 * cust.getVipStatus();
        discountedTotalCost = totalCost - discount;

        totalBill.setText(String.format("%.2f", discountedTotalCost));
        discounts.setText(String.format ("%.2f", discount));

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        totalBill.setText("--");
        discounts.setText("--");
    }

    @Override
    public void onFragmentInteraction(String action) {
        int inx = p.incrementAndGet();
        if ( inx < pl.size() && action == "save") {
            String playerName = db.getCustomerNameById(pl.get(inx));
            ScoreCapture sc = ScoreCapture.newInstance(playerName, pl.get(inx));
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(  R.id.fragment_container_score, sc);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container_score);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(frag);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

}
