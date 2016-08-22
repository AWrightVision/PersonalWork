package edu.gatech.seclass.gobowl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gatech.seclass.gobowl.R;

/**
 * Created by jake on 7/7/16.
 */
public class QRScanFrag  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_qr_scanner, null);

        return view;
    }
}
