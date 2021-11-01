package com.automate.structure;

public abstract class Automate {
    protected String stateTab[];
    protected String finalStateTab[];
    protected String tabLabel[];

    /*******************************************
     * constructor
     *************************************************/
    public Automate(String stateTab[], String finalStateTab[], String tabLabel[]) {
        this.stateTab = stateTab;
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
    }

    /**********************************************
     * getter
     **************************************************/

    public String[] getStateTab() {
        return this.stateTab;
    }

    public String[] getFinalStateTab() {
        return this.finalStateTab;
    }

    public String[] getTabLabel() {
        return this.tabLabel;
    }

    /*********************************************
     * setter
     ***********************************************/
    public void setStateTab(String stateTab[]) {
        this.stateTab = stateTab;
    }

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
}
