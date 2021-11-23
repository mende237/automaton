package com.automate.structure;

public class Afd extends Automate {
    private Transition matTrans[];
    private State initialState;
    private int cmpt;

    /****************************************
     * constructor
     ************************************/
    public Afd(State stateTab[], State initialState, State finalStateTab[], String tabLabel[] , String description) {
        super(stateTab, finalStateTab, tabLabel , description);
        this.matTrans = new Transition[super.stateTab.length * super.tabLabel.length];
        this.cmpt = 0;
    }

    public Afd(State stateTab[], State initialState, State finalStateTab[], String tabLabel[]) {
        super(stateTab, finalStateTab, tabLabel);
        this.matTrans = new Transition[super.stateTab.length * super.tabLabel.length];
        this.cmpt = 0;
    }

    /***************************************
     * getter
     ********************************************/
    public Transition[] getMatTrans() {
        return this.matTrans;
    }

    /********************************************
     * methods
     *************************************/
    public void addTransition(String begin, String label, String end) {
        this.matTrans[cmpt] = new Transition(begin, label, end);
        this.cmpt++;
    }

    @Override
    public void save(String path) {

    }

    

    @Override
    public void makeImage(String path) {
        
    }


    @Override
    public String toString() {
        String ch = "l'alphabet est : {";
        for (int i = 0; i < super.tabLabel.length; i++) {
            ch.concat(super.tabLabel[i] + ";");
        }

        ch.concat("}\n l'etat initiale est : " + initialState + "\n l'ensemble des etats finaux est {");
        for (int i = 0; i < super.finalStateTab.length; i++) {
            ch.concat(super.finalStateTab[i] + ";");
        }
        ch.concat("}\n les differentes transitions sont :\n");
        for (int i = 0; i < this.cmpt; i++) {
            ch.concat(matTrans[i].toString() + "\n");
        }
        return ch;
    }
}
