package edu.gatech.seclass.gobowl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.gatech.seclass.gobowl.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteCustomerDialog extends DialogFragment {

    public interface DeleteCustomerDialogListener {
        void onDeleteCustomerDialogPositiveClick(DialogFragment dialog);
    }

    DeleteCustomerDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeleteCustomerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DeleteCustomerDialogListener");
        }
    }

    private DatabaseHelper db;

    private ListView listView;

    private ArrayList<Customer> customers;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_delete_customer, null);
        builder.setView(view);

        db = DatabaseHelper.getInstance(getActivity());
        customers = db.getAllCustomerData();

        buildListView(view);

        setBuilderButtons(builder);

        return builder.create();
    }

    private void buildListView(View view) {
        listView = (ListView) view.findViewById(R.id.lvCustomers);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        List<String> labels = new ArrayList<String>();
        for (Customer c : customers) {
            labels.add(c.uiString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_multiple_choice, labels);
        listView.setAdapter(dataAdapter);
    }

    private void setBuilderButtons(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteCustomerDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SparseBooleanArray sp = listView.getCheckedItemPositions();
                if (sp != null) {
                    String msg = "";
                    for (int i = 0; i < sp.size(); i++) {
                        // - wrap with valueAt check to confirm the item is actually checked
                        //   surprisingly the getCheckedItemPositions returns all items that were
                        //   ever once checked (eg item checked and then unchecked)
                        if (sp.valueAt(i)) {
                            //Integer stat = db.deleteCustomerRecord(customers.get(sp.keyAt(i)).getDbIDString());
                            Integer stat = db.deleteAllCustomerRecordsAcrossTables(customers.get(sp.keyAt(i)).getDbIDString());
                            if (stat > 0) {
                                msg += "deleted: " + customers.get(sp.keyAt(i)).toString() + "\n";
                            } else {
                                msg += "failed to delete: " + customers.get(sp.keyAt(i)).toString() + "\n";
                            }
                        }
                    }

                    if (sp.size() > 0) {
                        // - send customer add back to manager activity
                        mListener.onDeleteCustomerDialogPositiveClick(DeleteCustomerDialog.this);

                        Utils.showToast(getActivity(), msg.trim());
                    }
                }
            }
        });
    }
}
