package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.to;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

public class AFN extends Automate {
    private LinkedList<Transition>matTrans;
    private String initialStateTab[];
    private String epsilone;

    /**************************************
     * constructor
     *****************************************/
    public AFN(String tabLabel[], String epsilone ,int nbrState, String finalStateTab[],
            String initialStateTab[],String name, String description) {
        super(tabLabel, nbrState, finalStateTab , name , description);
        this.matTrans = new LinkedList<>();
        this.epsilone = epsilone;
        this.initialStateTab = initialStateTab;
    }

    public AFN(String tabLabel[] , String epsilone, int nbrState , String finalStateTab[], String initialStateTab[])
    {
        super(tabLabel, nbrState, finalStateTab);
        this.epsilone = epsilone;
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
    public void addTransition(State begin, String label, State end) {
        Transition trans = new Transition(begin, label, end);
        this.matTrans.add(trans);
    }

    public static AFN jsonToAFN(String filePath){
        AFN afn  = null;
        String alphabet[] = null;
        String initialStates[] = null;
        int nbr_state = 0;
        String epsilone = null;
        String finalStates[] = null;
        Object transitions[][] = null;
        try {
            Scanner reader = new Scanner(new File(filePath)).useDelimiter("\\Z");
            String content = reader.next();
            reader.close();
            JSONObject obj = new JSONObject(content);

            epsilone = obj.getString("epsilone");

            JSONArray JTabAlphabet = obj.getJSONArray("alphabet");
            alphabet = new String[JTabAlphabet.length()];
            for (int i = 0; i < JTabAlphabet.length(); i++) {
                alphabet[i] = JTabAlphabet.getString(i);
            }
            nbr_state = obj.getInt("number state");

            JSONArray JTabInitialState = obj.getJSONArray("initial states");
            initialStates = new String[JTabInitialState.length()];
            for (int i = 0; i < JTabInitialState.length(); i++) {
                initialStates[i] = JTabInitialState.getString(i);
            }

            JSONArray JTabFinalState = obj.getJSONArray("final states");
            finalStates = new String[JTabFinalState.length()];
            for (int i = 0; i < JTabFinalState.length(); i++) {
                finalStates[i] = JTabFinalState.getString(i);
            }

            JSONArray JTabTransition = obj.getJSONArray("transitions");
            transitions = new Object[JTabTransition.length()][3];
            for (int i = 0; i < JTabTransition.length(); i++) {
                JSONArray Jtemp = JTabTransition.getJSONArray(i);
                for (int j = 0; j < Jtemp.length(); j++) {
                    if (j != 1) {
                        transitions[i][j] = new State(Jtemp.getString(j));
                    } else {
                        transitions[i][j] = Jtemp.getString(j);
                    }
                }
            }


            for (int i = 0; i < finalStates.length; i++) {
                for (int j = 0; j < transitions.length; j++) {
                    State tempState1 = (State) transitions[j][0];
                    State tempState2 = (State) transitions[j][2];

                    if (finalStates[i].equalsIgnoreCase(tempState1.getValue()) == true) {
                        tempState1.setType(StateType.FINAL);
                    }

                    if (finalStates[i].equalsIgnoreCase(tempState2.getValue()) == true) {
                        tempState2.setType(StateType.FINAL);
                    }
                }
            }

            for (int i = 0; i < initialStates.length; i++) {
                for (int j = 0; j < transitions.length; j++) {
                    State tempState1 = (State) transitions[j][0];
                    State tempState2 = (State) transitions[j][2];

                    if (initialStates[i].equalsIgnoreCase(tempState1.getValue()) == true) {
                        if (tempState1.getType() == StateType.FINAL || tempState1
                                .getType() == StateType.FINAL_INITIAL) {
                            tempState1.setType(StateType.FINAL_INITIAL);
                        } else {
                            tempState1.setType(StateType.INITIAL);
                        }
                    }

                    if (initialStates[i].equalsIgnoreCase(tempState2.getValue()) == true) {
                        if (tempState2.getType() == StateType.FINAL || tempState1
                                .getType() == StateType.FINAL_INITIAL) {
                            tempState2.setType(StateType.FINAL_INITIAL);
                        } else {
                            tempState2.setType(StateType.INITIAL);
                        }
                    }
                }
            }

            afn = new AFN(alphabet , epsilone , nbr_state, finalStates, initialStates);

            for (int i = 0; i < transitions.length; i++) {
                afn.addTransition((State) transitions[i][0], (String) transitions[i][1], (State) transitions[i][2]);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return afn;
    }


    @Override
    public String toString(){
        String ch = "l'alphabet est : {";
        for (int i = 0; i < super.tabLabel.length; i++) {
            ch += super.tabLabel[i] + ";";
        }
        ch += "}\nepsilone est : " + this.epsilone + "\n";
        ch += "l'ensemble des etats initiaux est : {";
        for (int i = 0; i < this.initialStateTab.length; i++) {
            ch += this.initialStateTab[i] + ";";
        }

        ch += "}\nl'ensemble des etats finaux est : {";

        for (int i = 0; i < super.finalStateTab.length; i++) {
            ch += super.finalStateTab[i] + ";";
        }
        ch += "}\nles differentes transitions sont :\n";
        for (int i = 0; i < this.matTrans.size(); i++) {
            ch += matTrans.get(i).toString() + "\n";
        }
        return ch;
    }


    @Override
    public void save(String path) {
        
    }

    @Override
    public void makeImage(String path) {
        Graph g = graph("test").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < this.matTrans.size(); i++) {
            if(this.matTrans.get(i).label.equalsIgnoreCase(this.epsilone) == false){
                g = g.with(super.addState(this.matTrans.get(i).begin)
                        .link(to(super.addState(this.matTrans.get(i).end))
                                .with(Label.of(this.matTrans.get(i).label))));
            }else{
                g = g.with(super.addState(this.matTrans.get(i).begin)
                        .link(to(super.addState(this.matTrans.get(i).end))
                                .with(Label.of("\u03B5"))));
            }
        }
        try {
            Graphviz.fromGraph(g).width(1500).render(Format.PNG).toFile(new File(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("test");
    }

    public static void main(String[] args) {
        AFN afn = AFN.jsonToAFN("/home/dimitri/automate_manip/back_end/afn.json");
        afn.makeImage("./test_afn.png");
        System.out.println(afn);
    }

}
