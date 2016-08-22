package edu.gatech.seclass.gobowl;

/**
 * Created by athan_000 on 7/8/2016.
 */
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
    private boolean selected = false;

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        selected = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        selected = false;
    }

    public boolean getSelected () {
        return selected;
    }

}
