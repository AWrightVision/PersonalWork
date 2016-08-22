package edu.gatech.seclass.gobowl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.gatech.seclass.gobowl.R;

import edu.gatech.seclass.services.PrintingService;
import edu.gatech.seclass.services.ServicesUtils;

/**
 * from the manager screen the user can press the new customer button which will pop
 * up a dialog box -- this class is the implementation of that dialog
 */
public class NewCustomerDialog extends DialogFragment {

    public interface NewCustomerDialogListener {
        void onNewCustomerDialogPositiveClick(DialogFragment dialog);
    }

    NewCustomerDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NewCustomerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                " must implement NewCustomerDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // - instantiate a new AlertDialog which provides API for creating dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // - convert xml view to java view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // - use null as the parent view vecause its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_new_customer, null);
        // - custom layout in the dialog
        builder.setView(view);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewCustomerDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // - grab user into
                String firstName = ((EditText) view.findViewById(R.id.etFirstName)).getText().toString().trim();
                String lastName = ((EditText) view.findViewById(R.id.etLastName)).getText().toString().trim();
                String email = ((EditText) view.findViewById(R.id.etEmail)).getText().toString().trim();

                // - check that input fields are valid (catch before we get to the db)
                String emptyFieldMessage = "";
                if (firstName.isEmpty()) {
                    emptyFieldMessage = getString(R.string.mngrNewCustomerMissingFirstName) + "\n";
                }
                if (lastName.isEmpty()) {
                    emptyFieldMessage += getString(R.string.mngrNewCustomerMissingLastName) + "\n";
                }
                if (email.isEmpty()) {
                    emptyFieldMessage += getString(R.string.mngrNewCustomerMissingEmail) + "\n";
                }
                if (!emptyFieldMessage.isEmpty()) {
                    Utils.showToast(getActivity(), emptyFieldMessage.trim());
                    return;
                }

                // - attempt to write to the database
                DatabaseHelper db = DatabaseHelper.getInstance(view.getContext());
                DatabaseHelper.CustomerTable.Status stat = db.insertCustomerData(firstName, lastName, email);

                Customer customer = db.getCustomerByEmail(email);

                // - report status as a toast
                if (stat.getCode() == DatabaseHelper.CustomerTable.Status.SUCCESS.getCode() && customer != null) {
                    // - add to the libs
                    Utils.addCustomerToServiceUtils(customer);

                    // - send customer add back to manager activity
                    mListener.onNewCustomerDialogPositiveClick(NewCustomerDialog.this);

                    // - need to auto generate card
                    boolean statPrint = PrintingService.printCard(
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getCustomerID());

                    String customerAdded = "Customer Added - " + customer.getFirstName() +
                        " " + customer.getLastName() + " (id: " + customer.getCustomerID() + ")";
                    if (statPrint) {
                        Utils.showToast(getActivity(),
                            customerAdded + "\n" +
                            "Print Card - Success");
                    } else {
                        Utils.showToast(getActivity(),
                            customerAdded + "\n" +
                            "Print Card - Failed");
                    }
                } else {
                    Utils.showToast(getActivity(), "ERROR - " + stat.getMessage());
                }
            }
        });

        // - create the AlertDialog object and return it
        return builder.create();
    }
}
