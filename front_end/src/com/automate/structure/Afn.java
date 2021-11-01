package com.automate.structure;

import java.util.LinkedList;

public class Afn extends Automate {
    private LinkedList<Transition> matTrans;
    private String initialStateTab[];

    /**************************************
     * constructor
     *****************************************/
    public Afn(String stateTab[] , String initialStateTab[] , String finalStateTab[], String tabLabel[]) {
        super(stateTab, finalStateTab, tabLabel);
        this.matTrans = new LinkedList<>();
        this.initialStateTab = initialStateTab;
    }

    /****************************************
     * getter
     ********************************************/
    public LinkedList<Transition> getMatTrans() {
        return this.matTrans;
    }

    public String[] getInitialStateTab() {
        return this.initialStateTab;
    }

    /********************************************
     * methods
     ************************************/
    public void addTransition(String begin, String label, String end) {
        Transition trans = new Transition(begin, label, end);
        this.matTrans.add(trans);
    }

    @Override
    public String toString(){
        String ch = "l'alphabet est : {";
        for (int i = 0; i < super.tabLabel.length; i++) {
            ch.concat(super.tabLabel[i] + ";");
        }
        ch.concat("l'ensemble des etats initiaux est : {");
        for (int i = 0; i < this.initialStateTab.length; i++) {
            ch.concat(this.initialStateTab[i] + ";");
        }

        ch.concat("}\n l'ensemble des etats finaux est : {");

        for (int i = 0; i < super.finalStateTab.length; i++) {
            ch.concat(super.finalStateTab[i] + ";");
        }
        ch.concat("}\n les differentes transitions sont :\n");
        for (int i = 0; i < this.matTrans.size(); i++) {
            ch.concat(matTrans.get(i).toString() + "\n");
        }

        return ch;
    }


    @Override
    public void save(String path) {
        
    }

}
