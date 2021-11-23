package com.automate.structure;

import java.util.LinkedList;

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;

public class Afn extends Automate {
    private LinkedList<Transition> matTrans;
    private State initialStateTab[];

    /**************************************
     * constructor
     *****************************************/
    public Afn(State stateTab[] , State initialStateTab[] , State finalStateTab[], String tabLabel[] , String description) {
        super(stateTab, finalStateTab, tabLabel , description);
        this.matTrans = new LinkedList<>();
        this.initialStateTab = initialStateTab;
    }

    public Afn(State stateTab[], State initialStateTab[], State finalStateTab[], String tabLabel[])
    {
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

    public State[] getInitialStateTab() {
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

    @Override
    public void makeImage(String path) {
        // Graph g = Factory.graph("afn");
        // g.directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT));
        // for (int i = 0; i < matTrans.size(); i++) {
        //     g.with()
        // }
    }

}
