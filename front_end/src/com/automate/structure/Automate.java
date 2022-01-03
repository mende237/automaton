package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.*;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Node;

public abstract class Automate {
    protected int cmpt;
    protected int nbrState;
    protected String name;
    protected String description;
    protected String finalStateTab[];
    protected String tabLabel[];

    /*******************************************
     * constructor
     *************************************************/
    public Automate(String tabLabel[] , int nbrState, String finalStateTab[] , String name, String description) {
        this.name = name;
        this.description = description;
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
        this.nbrState = nbrState;
        this.cmpt = 0;
    }

    public Automate(String tabLabel[], int nbrState, String finalStateTab[]) {
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
        this.nbrState = nbrState;
        this.cmpt = 0;
    }

    /**********************************************
     * getter
     **************************************************/


    public String[] getFinalStateTab() {
        return this.finalStateTab;
    }

    public String[] getTabLabel() {
        return this.tabLabel;
    }

    /*********************************************
     * setter
     ***********************************************/

    public void setFinalStateTab(String finalStateTab[]) {
        this.finalStateTab = finalStateTab;
    }

    public void setTabLabel(String tabLabel[]) {
        this.tabLabel = tabLabel;
    }

    /*****************************************
     * methods
     **************************************************/

    public abstract void save(String path);

    public abstract void makeImage(String path);

    protected Node addState(State state) {
        switch (state.getType()) {
            case INITIAL:
                return node(state.getValue()).with(Style.FILLED , Color.rgb("#b5fed9").fill());
            case FINAL:
                return node(state.getValue()).with(Style.FILLED , Shape.DOUBLE_CIRCLE, Color.rgb("#e2cbc1").fill());
            case WELL:
                return node(state.getValue()).with(Style.FILLED , Color.GRAY.fill());
            case FINAL_INITIAL:
                return node(state.getValue()).with(Style.FILLED, Shape.DOUBLE_CIRCLE, Color.rgb("#b5fed9").fill());
            default:
                return node(state.getValue()).with(Style.FILLED , Color.rgb("#87CEEB").fill());
        }
    }
}
