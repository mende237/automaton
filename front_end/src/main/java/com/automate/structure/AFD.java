package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.to;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

public class AFD extends Automate {
    private Transition matTrans[] = null;
    private LinkedList<Transition> dinimicMatTrans;
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

    /***************************************
     * getter
     ********************************************/
    public Transition[] getMatTrans() {
        return this.matTrans;
    }

    /********************************************
     * methods
     *************************************/
    public void addTransition(State begin, String label, State end) {
        this.matTrans[cmpt] = new Transition(begin, label, end);
        super.cmpt++;
    }

    @Override
    public void addTransitionToGraph(Transition transition) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTransitionToGraph'");
    }

    @Override
    public void save(String path) {

    }

    @Override
    public Graph markeGraph() {
        Graph g = graph("afd").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < this.matTrans.length; i++) {
            g = g.with(super.addState(this.matTrans[i].begin)
                    .link(to(super.addState(this.matTrans[i].end))
                            .with(Label.of(this.matTrans[i].label))));
        }
        return g;
    }

    @Override
    public void makeImage(String path) {
        Graph g = markeGraph();
        try {
            path = path.replace("\\", "/");
            Graphviz.fromGraph(g).width(1500).render(Format.PNG).toFile(new File(path));
       } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

                if (initialState.equalsIgnoreCase(tempState1.getValue()) == true) {
                    if (tempState1.getType() == StateType.FINAL || tempState1
                            .getType() == StateType.FINAL_INITIAL) {
                        tempState1.setType(StateType.FINAL_INITIAL);
                    } else {
                        tempState1.setType(StateType.INITIAL);
                    }
                }

                if (initialState.equalsIgnoreCase(tempState2.getValue()) == true) {
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
            if (begin.getValue().equalsIgnoreCase(end.getValue()) == false) {
                isWellState = false;
                break;
            }
        }

        State wellState = (State) transitions[transitions.length - 1][0];
        if (wellState.getValue().equalsIgnoreCase(initialState) == true) {
            isWellState = false;
        }

        for (int i = 0; i < finalStates.length; i++) {
            if (wellState.getValue().equalsIgnoreCase(finalStates[i]) == true) {
                isWellState = false;
                break;
            }
        }

        if (isWellState == true) {
            for (int i = 0; i < transitions.length; i++) {
                State begin = (State) transitions[i][0];
                State end = (State) transitions[i][2];
                if (begin.getValue().equalsIgnoreCase(wellState.getValue()) == true) {
                    // if(begin.getType() == StateType.NORMAL){
                    begin.setType(StateType.WELL);
                    // }
                }

                if (end.getValue().equalsIgnoreCase(wellState.getValue()) == true) {
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
        for (int i = 0; i < super.tabLabel.length; i++) {
            ch += super.tabLabel[i] + ";";
        }

        ch += "}\nl'etat initiale est : " + this.initialState + "\nl'ensemble des etats finaux est {";
        for (int i = 0; i < super.finalStateTab.length; i++) {
            ch += super.finalStateTab[i].toString() + ";";
        }
        ch += "}\nles differentes transitions sont :\n";
        for (int i = 0; i < this.cmpt; i++) {
            ch += matTrans[i].toString() + "\n";
        }
        return ch;
    }

    public static void main(String[] args) {
        // AFD afd = null;
        // try {
        //     afd = AFD.jsonToAFD("/home/dimitri/automate_manip/back_end/afd.json", false);
        // } catch (FileNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // afd.makeImage("./test.png");
        // System.out.println(afd);

        String test = "tata\\titi\\toto";
        test = test.replace("\\", "/");
        System.out.println(test);
    }



   
}
