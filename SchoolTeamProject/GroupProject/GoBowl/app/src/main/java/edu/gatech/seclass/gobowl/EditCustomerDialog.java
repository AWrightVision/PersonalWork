package edu.gatech.seclass.gobowl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;
import java.util.List;

public class EditCustomerDialog extends DialogFragment {

    private DatabaseHelper db;

    private Spinner spinner;
    private Customer selectedCustomer = null;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // - instantiate a new AlertDialog which provides API for creating dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // - convert xml view to java view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // - use null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_customer, null);
        // - custom layout in the dialog
        builder.setView(view);

        // - edit customer info boxes
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);

        // - database
        db = DatabaseHelper.getInstance(view.getContext());

        // - populate spinner
        buildSpinner(view);

        // - set logic for dialog buttons
        setBuilderButtons(builder);

        // - create the AlertDialog object and return it
        return builder.create();
    }

    private void buildSpinner(View view) {
        final ArrayList<Customer> customers = db.getAllCustomerData();

        List<String> labels = new ArrayList<String>();
        labels.add(getString(R.string.mngrEditCustomerSpinnerHint));
        for (Customer c : customers) {
            labels.add(c.uiString());
        }

        spinner = (Spinner) view.findViewById(R.id.spinnerCustomer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // - update screen text with current customer data when user selects a customer
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinCustomer = (String) parent.getItemAtPosition(position);
                if (!spinCustomer.equals(getString(R.string.mngrEditCustomerSpinnerHint))) {
                    // - cache the selected customer
                    selectedCustomer = customers.get(position-1);

                    // - set the text
                    etFirstName.setText(selectedCustomer.getFirstName());
                    etLastName.setText(selectedCustomer.getLastName());
                    etEmail.setText(selectedCustomer.getEmail());
                } else {
                    selectedCustomer = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setBuilderButtons(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditCustomerDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedCustomer != null) {
                    // - edited fields
                    String fname = etFirstName.getText().toString();
                    String lname = etLastName.getText().toString();
                    String email = etEmail.getText().toString();

                    // - confirm all fields are populated
                    if (fname.isEmpty()) {
                        Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerMissingFirstName));
                        return;
                    }
                    if (lname.isEmpty()) {
                        Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerMissingLastName));
                        return;
                    }
                    if (lname.isEmpty()) {
                        Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerMissingEmail));
                        return;
                    }

                    // - update database
                    // String id, String firstName, String lastName, String email, int total, boolean vip
                    boolean statusUpdate = db.updateCustomerData(
                        selectedCustomer.getDbIDString(),
                        fname,
                        lname,
                        email,
                        selectedCustomer.getTotal(),
                        selectedCustomer.getVipStatus()
                    );

                    if (statusUpdate) {
                        Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerUpdatedToastMsg));
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerFailedToastMsg));
                    }
                } else {
                    Utils.showToast(getActivity(), getString(R.string.mngrEditCustomerNothingSelectedToastMsg));
                }
            }
        });
    }
}
