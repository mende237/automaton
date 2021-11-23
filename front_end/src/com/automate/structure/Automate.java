package com.automate.structure;

public abstract class Automate {
    protected String description;
    protected State stateTab[];
    protected State finalStateTab[];
    protected String tabLabel[];

    /*******************************************
     * constructor
     *************************************************/
    public Automate(State stateTab[], State finalStateTab[], String tabLabel[] , String description) {
        this.stateTab = stateTab;
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
        this.description = description;
    }

    public Automate(State stateTab[], State finalStateTab[], String tabLabel[]) {
        this.stateTab = stateTab;
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
    }

    /**********************************************
     * getter
     **************************************************/

    public State[] getStateTab() {
        return this.stateTab;
    }

    public State[] getFinalStateTab() {
        return this.finalStateTab;
    }

    public String[] getTabLabel() {
        return this.tabLabel;
    }

    /*********************************************
     * setter
     ***********************************************/
    public void setStateTab(State stateTab[]) {
        this.stateTab = stateTab;
    }

    public void setFinalStateTab(State finalStateTab[]) {
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
}
