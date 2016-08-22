package edu.gatech.seclass.gobowl;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gatech.seclass.gobowl.R;

/**
 * Created by jake on 7/6/16.
 */

public class RequestPlayersFrag extends Fragment {
    View view = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.view != null) {
            return this.view;
        }
        View view = inflater.inflate(R.layout.fragment_request_players, null);

        return view;
    }

}
