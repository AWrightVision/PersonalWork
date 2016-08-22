package edu.gatech.seclass.gobowl;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;

public class ManagerActivity
    extends
        AppCompatActivity
    implements
        NewCustomerDialog.NewCustomerDialogListener,
        DeleteCustomerDialog.DeleteCustomerDialogListener
{

    private DatabaseHelper db;

    TextView tvTotalCustomers;
    TextView tvVipCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // - screen content
        tvTotalCustomers = (TextView) findViewById(R.id.tvTotalCustomers);
        tvVipCustomers = (TextView) findViewById(R.id.tvVipCustomers);

        // - database
        db = DatabaseHelper.getInstance(this);

        updateScreenCustomerStats();
    }

    /**
     * handle all buttons on the screen
     *
     * @param view manager
     */
    public void handleClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNewCustomer:
                // - launch new customer dialogue
                NewCustomerDialog customerDialog = new NewCustomerDialog();
                customerDialog.show(getFragmentManager(), "New Customer");
                break;
            case R.id.buttonPrintCard:
                PrintCardDialog printCardDialog = new PrintCardDialog();
                printCardDialog.show(getFragmentManager(), "Print Customer Card");
                break;
            case R.id.buttonEditCustomer:
                EditCustomerDialog editCustomerDialog = new EditCustomerDialog();
                editCustomerDialog.show(getFragmentManager(), "Edit Customer");
                break;
            case R.id.buttonDeleteCustomer:
                DeleteCustomerDialog deleteCustomerDialog = new DeleteCustomerDialog();
                deleteCustomerDialog.show(getFragmentManager(), "Delete Customer");
                break;
            case R.id.buttonAllCustomer:
                showAllCustomers();
                break;
            case R.id.buttonDeleteAllCustomer:
                deleteAllCustomers();
                break;
            default:
                Utils.showToast(this, getString(R.string.errorUnhandledButton));
                break;
        }
    }

    @Override
    public void onNewCustomerDialogPositiveClick(DialogFragment dialog) {
        updateScreenCustomerStats();
    }

    @Override
    public void onDeleteCustomerDialogPositiveClick(DialogFragment dialog) {
        updateScreenCustomerStats();
    }

    private void showAllCustomers() {
        ArrayList<Customer> customers = db.getAllCustomerData();
        if (customers.isEmpty()) {
            Utils.showMessage(this, getString(R.string.mngrShowAllCustomerTitle),
                getString(R.string.mngrShowAllCustomerEmpty));
        } else {
            // - using StringBuffer rather than StringBuilder for synchronization
            StringBuffer buffer = new StringBuffer();
            for (Customer c : customers) {
                buffer.append("Id: " + c.getCustomerID() + "\n");
                buffer.append("First Name: " + c.getFirstName() + "\n");
                buffer.append("Last Name: " + c.getLastName() + "\n");
                buffer.append("Email: " + c.getEmail() + "\n");
                buffer.append("VIP: " + c.vipStatusToString() + "\n\n");
            }
            Utils.showMessage(this, getString(R.string.mngrShowAllCustomerTitle), buffer.toString());
        }
    }

    private void updateScreenCustomerStats() {
        updateScreenTotalCustomers();
        updateScreeVipCustomers();
    }

    private void updateScreenTotalCustomers() {
        tvTotalCustomers.setText(Integer.toString(db.getCustomerTableRows()));
    }

    private void updateScreeVipCustomers() {
        tvVipCustomers.setText(Integer.toString(db.getVipCustomerCount()));
    }

    private void deleteAllCustomers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.mngrDeleteAllDialogTitle));
        builder.setMessage(getString(R.string.mngrDeleteAllDialogMessage));
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteAllDatabaseRecords();
                updateScreenCustomerStats();
                Utils.showToast(ManagerActivity.this, getString(R.string.mngrDeleteAllToastMessage));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create();
        builder.show();
    }
}
