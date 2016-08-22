package edu.gatech.seclass.gobowl;

import java.util.ArrayList;

/**
 * Created by jake on 7/6/16.
 *
 */
public class Lane {

    private int laneNum;
    ArrayList<String> playersIDs;
    public Lane(int laneNum, ArrayList<String> playersIDs){
        this.laneNum = laneNum;
        this.playersIDs = playersIDs;
    }

    public int getLaneNum() {
        return laneNum;
    }
    public boolean hasPlayer(String id){
       return  playersIDs.contains(id);
    }

}
