package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.to;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Graph;

public class AFD extends Automaton {
    private Transition matTrans[] = null;
    private String initialState = null;

    /****************************************
     * constructor
     ************************************/
    public AFD(String tabLabel[], int nbrState, String finalStateTab[], String initialState, String name,
            String description , String path) {
        super(tabLabel, nbrState, finalStateTab, name, description , path);
        this.init(initialState);
    }

    public AFD(String tabLabel[], int nbrState, String finalStateTab[], String initialState, String name,
            String description) {
        super(tabLabel, nbrState, finalStateTab, name, description);
        this.init(initialState);
    }

    public AFD(String tabLabel[], int nbrState, String finalStateTab[], String initialState , String path) {
        super(tabLabel, nbrState, finalStateTab , path);
        this.init(initialState);
    }

    public AFD(String tabLabel[], int nbrState, String finalStateTab[], String initialState) {
        super(tabLabel, nbrState, finalStateTab);
        this.init(initialState);
    }

    private void init(String initialState){
        this.initialState = initialState;
        this.matTrans = new Transition[super.nbrState * super.tabLabel.length];
    }

    /*********************************************
     * getter
     ********************************************/
    public Transition[] getMatTrans() {
        return this.matTrans;
    }

    /********************************************
     * methods
     ********************************************/
    @Override
    public void addTransition(State begin, String label, State end) {
        this.matTrans[cmpt] = new Transition(begin, label, end);
        super.cmpt++;
    }

    // @Override
    // public void addTransitionToGraph(Transition transition) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'addTransitionToGraph'");
    // }

    @Override
    public Graph markeGraph() {
        Graph g = graph("afd").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < this.matTrans.length; i++) {
            g = g.with(Automaton.addState(this.matTrans[i].begin)
                    .link(to(Automaton.addState(this.matTrans[i].end))
                            .with(Label.of(this.matTrans[i].label))));
        }
        return g;
    }

    

    public static AFD jsonToAFD(String filePath, boolean readInfo) throws FileNotFoundException , JSONException{
        AFD afd = null;
        String alphabet[] = null;
        String initialState = null;
        int nbr_state = 0;
        String finalStates[] = null;
        Object transitions[][] = null;
        String name = null;
        String description = null;
        try (Scanner reader = new Scanner(new File(filePath)).useDelimiter("\\Z")) {
            String content = reader.next();
            reader.close();
            JSONObject obj = new JSONObject(content);

            if (readInfo == true) {
                name = obj.getString("name");
                description = obj.getString("description");
            }
            JSONArray JTabAlphabet = obj.getJSONArray("alphabet");
            alphabet = new String[JTabAlphabet.length()];
            for (int i = 0; i < JTabAlphabet.length(); i++) {
                alphabet[i] = JTabAlphabet.getString(i);
            }

            nbr_state = obj.getInt("number state");
            initialState = obj.getString("initial state");

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
            // System.out.println("name : " + name + " nbr trans " + transitions.length);
        }
        for (int i = 0; i < finalStates.length; i++) {
            for (int j = 0; j < transitions.length; j++) {
                State tempState1 = (State) transitions[j][0];
                State tempState2 = (State) transitions[j][2];

                if (finalStates[i].equalsIgnoreCase(tempState1.getName()) == true) {
                    tempState1.setType((tempState1.getType() == StateType.INITIAL || tempState1.getType() == StateType.FINAL_INITIAL)? StateType.FINAL_INITIAL : StateType.FINAL);
                    // tempState1.setType(StateType.FINAL);
                }

                if (finalStates[i].equalsIgnoreCase(tempState2.getName()) == true) {
                    tempState2.setType((tempState2.getType() == StateType.INITIAL || tempState2.getType() == StateType.FINAL_INITIAL)? StateType.FINAL_INITIAL : StateType.FINAL);
                    // tempState2.setType(StateType.FINAL);
                }

                if (initialState.equalsIgnoreCase(tempState1.getName()) == true) {
                    if (tempState1.getType() == StateType.FINAL || tempState1
                            .getType() == StateType.FINAL_INITIAL) {
                        tempState1.setType(StateType.FINAL_INITIAL);
                    } else {
                        tempState1.setType(StateType.INITIAL);
                    }
                }

                if (initialState.equalsIgnoreCase(tempState2.getName()) == true) {
                    if (tempState2.getType() == StateType.FINAL || tempState1
                            .getType() == StateType.FINAL_INITIAL) {
                        tempState2.setType(StateType.FINAL_INITIAL);
                    } else {
                        tempState2.setType(StateType.INITIAL);
                    }
                }
            }
        }

        boolean isWellState = true;
        for (int i = transitions.length - alphabet.length; i < transitions.length; i++) {
            State begin = (State) transitions[i][0];
            State end = (State) transitions[i][2];
            if (begin.getName().equalsIgnoreCase(end.getName()) == false) {
                isWellState = false;
                break;
            }
        }

        State wellState = (State) transitions[transitions.length - 1][0];
        if (wellState.getName().equalsIgnoreCase(initialState) == true) {
            isWellState = false;
        }

        for (int i = 0; i < finalStates.length; i++) {
            if (wellState.getName().equalsIgnoreCase(finalStates[i]) == true) {
                isWellState = false;
                break;
            }
        }

        if (isWellState == true) {
            for (int i = 0; i < transitions.length; i++) {
                State begin = (State) transitions[i][0];
                State end = (State) transitions[i][2];
                if (begin.getName().equalsIgnoreCase(wellState.getName()) == true) {
                    // if(begin.getType() == StateType.NORMAL){
                    begin.setType(StateType.WELL);
                    // }
                }

                if (end.getName().equalsIgnoreCase(wellState.getName()) == true) {
                    // if(end.getType() == StateType.NORMAL){
                    end.setType(StateType.WELL);
                    // }
                }
            }
        }
        if (readInfo == true) {
            afd = new AFD(alphabet, nbr_state, finalStates, initialState, name, description);
        } else {
            afd = new AFD(alphabet, nbr_state, finalStates, initialState);
        }

        for (int i = 0; i < transitions.length; i++) {
            afd.addTransition((State) transitions[i][0], (String) transitions[i][1], (State) transitions[i][2]);
        }

        // System.out.printf("l'alphabet est : \n[");
        // for (int i = 0; i < alphabet.length; i++) {
        // System.out.printf("%s ", alphabet[i]);
        // }
        // System.out.println("]");
        // System.out.printf("l'etat initiale est : %s\n", initialState);

        // System.out.println("les differentes transitions sont :");
        // for (int i = 0; i < transitions.length; i++) {
        // System.out.printf("%s ---- %s ----- %s\n", transitions[i][0].toString(),
        // transitions[i][1].toString() , transitions[i][2].toString());
        //

        return afd;
    }

    @Override
    public String toString() {
        String ch = "l'alphabet est : {";
        for (int i = 0; i < super.tabLabel.length - 1; i++) {
            ch += super.tabLabel[i] + ";";
        }
        ch += super.tabLabel[super.tabLabel.length - 1];
        ch += "}\nl'etat initiale est : " + this.initialState + "\nl'ensemble des etats finaux est {";
        for (int i = 0; i < super.finalStateTab.length - 1; i++) {
            ch += super.finalStateTab[i].toString() + ";";
        }
        ch += super.finalStateTab[super.finalStateTab.length - 1];
        ch += "}\nles differentes transitions sont :\n";
        for (int i = 0; i < this.cmpt; i++) {
            ch += matTrans[i].toString() + "\n";
        }
        return ch;
    }

   
    @Override
    public void automatonToJson(String filePath) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", super.name);
        jo.put("description", super.description);
        jo.put("alphabet", super.tabLabel);
        jo.put("number state", super.nbrState);
        jo.put("initial state", this.initialState);
        JSONArray jaFinalState = new JSONArray(super.finalStateTab);
        jo.put("final states", jaFinalState);
        JSONArray jaTransitions = new JSONArray();

        // for (Transition transition : this.matTrans)
        //     jaTransitions.put(new JSONArray(String.format("[%s , %s , %s]",
        //         transition.getBegin().getName() , transition.getLabel() , transition.getEnd().getName())));
        String temp[] = new String[3];
        for (int i = 0; i < this.cmpt; i++) {
            Transition transition = this.matTrans[i];
            temp[0] = transition.getBegin().getName();
            temp[1] = transition.getLabel();
            temp[2] = transition.getEnd().getName();
            jaTransitions.put(new JSONArray(temp));

        }
        jo.put("transitions", jaTransitions);
        
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jo.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
