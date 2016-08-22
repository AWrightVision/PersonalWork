package edu.gatech.seclass.gobowl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.gatech.seclass.gobowl.R;



public class ScoreCapture extends Fragment {

    // TODO: add back the listener so I can pass back to the Check Out Activity that this activity is done

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "playerName";
    private static final String ARG_PARAM2 = "playerID";
    private Button saveScore, cancel;
    private EditText textScore;
    private TextView viewTitle;
    private OnFragmentInteractionListener mListener;


    public ScoreCapture() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoreCapture.
     */

    public static ScoreCapture newInstance(String param1, int param2) {
        ScoreCapture fragment = new ScoreCapture();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_score_capture, container, false);
        saveScore = (Button) view.findViewById(R.id.buttonSaveScore);
        cancel = (Button) view.findViewById(R.id.buttonCancel);
        textScore = (EditText) view.findViewById(R.id.editTextScore);
        viewTitle = (TextView) view.findViewById(R.id.textViewTitle);


        saveScore.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        onClickButtonHandler("save", view);
                                                                    }
                                                                });

        cancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    onClickButtonHandler("cancel", view);
                                                                }
                                                            });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewTitle.setText(bundle.getString(ARG_PARAM1));
        }
    }

    public void onClickButtonHandler(String action, View view) {

        if (action == "save") {
            int score = 0;
            DatabaseHelper db = DatabaseHelper.getInstance(view.getContext());
            if (textScore.getText().toString() != "") {
                 score = Integer.parseInt(textScore.getText().toString());
            }
            if (getArguments() != null  ) {
                db.insertScore(score,getArguments().getInt(ARG_PARAM2));
            }
            Utils.showToast(getActivity(), "Saved score " + score +" for Player " + getArguments().getInt(ARG_PARAM2) );

        }
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction( String action);
    }

}
