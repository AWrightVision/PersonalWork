package edu.gatech.seclass.gobowl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jake on 7/6/16.
 */
public class AlleyLane {
    private static AlleyLane ourInstance = new AlleyLane();
    private Map<Integer, Lane> occupiedLanes = new HashMap<Integer, Lane>();

    public static AlleyLane getInstance() {
        return ourInstance;
    }

    private AtomicInteger laneLaneNumber;

    private AlleyLane() {
         laneLaneNumber = new AtomicInteger(1);

    }
    public Lane getNextLane(ArrayList<String> ids){
         Lane lane = new Lane(this.laneLaneNumber.getAndIncrement(),ids);
         occupiedLanes.put(lane.getLaneNum(),lane);
         return lane;
    }
    public Lane getLane(int laneNum) { return occupiedLanes.get(laneNum);}
}
