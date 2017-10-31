package com.pujhones.bicita.view;

import java.util.List;

/**
 * Created by Tefa on 30/10/2017.
 */


public class LegsObject {
    private List<StepsObject> steps;
    public LegsObject(List<StepsObject> steps) {
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }
}