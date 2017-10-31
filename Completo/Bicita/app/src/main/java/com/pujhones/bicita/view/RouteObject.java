package com.pujhones.bicita.view;

import java.util.List;

/**
 * Created by Tefa on 30/10/2017.
 */

public class RouteObject {
    private List<LegsObject> legs;
    public RouteObject(List<LegsObject> legs) {
        this.legs = legs;
    }
    public List<LegsObject> getLegs() {
        return legs;
    }
}
