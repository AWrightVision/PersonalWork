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
import android.widget.Spinner;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.services.PrintingService;

public class PrintCardDialog extends DialogFragment {

    private Spinner spinner;
    private Customer selectedCustomer = null;

    private DatabaseHelper db;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_print_card, null);
        builder.setView(view);

        // - database
        db = DatabaseHelper.getInstance(view.getContext());

        // - populate spinner
        buildSpinner(view);

        // - register buttons
        setBuilderButtons(builder);

        return builder.create();
    }

    private void buildSpinner(View view) {
        final ArrayList<Customer> customers = db.getAllCustomerData();

        List<String> labels = new ArrayList<String>();
        labels.add(getString(R.string.mngrPrintCardSpinnerHint));
        for (Customer c : customers) {
            labels.add(c.uiString());
        }

        spinner = (Spinner) view.findViewById(R.id.spinnerCustomer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinCustomer = (String) parent.getItemAtPosition(position);
                if (!spinCustomer.equals(getString(R.string.mngrPrintCardSpinnerHint))) {
                    // - cache the selected customer
                    selectedCustomer = customers.get(position-1);
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
                PrintCardDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedCustomer != null) {
                    boolean stat = PrintingService.printCard(
                        selectedCustomer.getFirstName(),
                        selectedCustomer.getLastName(),
                        selectedCustomer.getCustomerID());
                    if (stat) {
                        Utils.showToast(getActivity(), getString(R.string.mngrPrintCardSuccessToastMsg));
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.mngrPrintCardFailToastMsg));
                    }
                } else {
                    Utils.showToast(getActivity(), getString(R.string.mngrPrintCardNothingSelectedToastMsg));
                }
            }
        });
    }
}
