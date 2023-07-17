package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.to;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import guru.nidi.graphviz.model.Graph;

public class AFN extends Automaton {
    private LinkedList<Transition> matTrans;
    private String initialStateTab[];
    // protected ArrayList<String> dinamicInitialState;
    private String epsilone;


    /**************************************
     * constructor
     *****************************************/
    public AFN(){
        super();
    }

   

    public AFN(String tabLabel[], String epsilone, int nbrState, String finalStateTab[],
            String initialStateTab[], String name, String description) {
        super(tabLabel, nbrState, finalStateTab, name, description);
        this.init(epsilone, initialStateTab);
    }

    public AFN(String tabLabel[], String epsilone, int nbrState, String finalStateTab[],
            String initialStateTab[], String name, String description , String path) {
        super(tabLabel, nbrState, finalStateTab, name, description , path);
        this.init(epsilone, initialStateTab);
    }

    public AFN(String tabLabel[], String epsilone, int nbrState, String finalStateTab[], String initialStateTab[] , String path) {
        super(tabLabel, nbrState, finalStateTab , path);
        this.init(epsilone, initialStateTab);
    }

    public AFN(String tabLabel[], String epsilone, int nbrState, String finalStateTab[], String initialStateTab[]) {
        super(tabLabel, nbrState, finalStateTab);
        this.init(epsilone, initialStateTab);
    }

    private void init(String epsilone , String initialStateTab[]){
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

    // @Override
    // public void addTransitionToGraph(Transition transition) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'addTransitionToGraph'");
    // }

    public static AFN jsonToAFN(String filePath, boolean readInfo) throws FileNotFoundException {
        AFN afn = null;
        String alphabet[] = null;
        String initialStates[] = null;
        int nbr_state = 0;
        String epsilone = null;
        String name = null;
        String description = null;
        String finalStates[] = null;
        Object transitions[][] = null;
        Scanner reader = new Scanner(new File(filePath)).useDelimiter("\\Z");
        String content = reader.next();
        reader.close();
        JSONObject obj = new JSONObject(content);

        if (readInfo == true) {
            name = obj.getString("name");
            description = obj.getString("description");
        }

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

                if (finalStates[i].equalsIgnoreCase(tempState1.getName()) == true) {
                    tempState1.setType((tempState1.getType() == StateType.INITIAL || tempState2.getType() == StateType.FINAL_INITIAL)? StateType.FINAL_INITIAL : StateType.FINAL);
                }

                if (finalStates[i].equalsIgnoreCase(tempState2.getName()) == true) {
                    tempState2.setType((tempState2.getType() == StateType.INITIAL || tempState2.getType() == StateType.FINAL_INITIAL)? StateType.FINAL_INITIAL : StateType.FINAL);
                }
            }
        }

        for (int i = 0; i < initialStates.length; i++) {
            for (int j = 0; j < transitions.length; j++) {
                State tempState1 = (State) transitions[j][0];
                State tempState2 = (State) transitions[j][2];

                if (initialStates[i].equalsIgnoreCase(tempState1.getName()) == true) {
                    tempState1.setType((tempState1.getType() == StateType.FINAL || tempState1
                            .getType() == StateType.FINAL_INITIAL) ? StateType.FINAL_INITIAL : StateType.INITIAL);
                }

                if (initialStates[i].equalsIgnoreCase(tempState2.getName()) == true) {
                    tempState2.setType((tempState2.getType() == StateType.FINAL || tempState2
                            .getType() == StateType.FINAL_INITIAL) ? StateType.FINAL_INITIAL : StateType.INITIAL);

                }
            }
        }
        if (readInfo == true) {
            afn = new AFN(alphabet, epsilone, nbr_state, finalStates, initialStates, name, description);
        } else {
            afn = new AFN(alphabet, epsilone, nbr_state, finalStates, initialStates);
        }

        for (int i = 0; i < transitions.length; i++) {
            afn.addTransition((State) transitions[i][0], (String) transitions[i][1], (State) transitions[i][2]);
        }

        return afn;
    }

    @Override
    public String toString() {
        String ch = "l'alphabet est : {";
        for (int i = 0; i < super.tabLabel.length - 1; i++) {
            ch += super.tabLabel[i] + ";";
        }
        ch += super.tabLabel[super.tabLabel.length - 1];
        ch += "}\nepsilone est : " + this.epsilone + "\n";
        ch += "l'ensemble des etats initiaux est : {";
        for (int i = 0; i < this.initialStateTab.length - 1; i++) {
            ch += this.initialStateTab[i] + ";";
        }
        ch += this.initialStateTab[this.initialStateTab.length - 1];
        ch += "}\nl'ensemble des etats finaux est : {";

        for (int i = 0; i < super.finalStateTab.length - 1; i++) {
            ch += super.finalStateTab[i] + ";";
        }
        ch += super.finalStateTab[super.finalStateTab.length - 1];
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
    public Graph markeGraph() {
        
        Graph g = graph("afn").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < this.matTrans.size(); i++) {
            if (this.matTrans.get(i).label.equalsIgnoreCase(this.epsilone) == false) {
                g = g.with(Automaton.addState(this.matTrans.get(i).begin)
                        .link(to(Automaton.addState(this.matTrans.get(i).end))
                                .with(Label.of(this.matTrans.get(i).label))));
            } else {
                g = g.with(Automaton.addState(this.matTrans.get(i).begin)
                        .link(to(Automaton.addState(this.matTrans.get(i).end))
                                .with(Label.of("\u03B5"))));
            }
        }
        return g;
    }

    @Override
    public void AutomatonToJson(String filePath) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", super.name);
        jo.put("description", super.description);
        jo.put("epsilone", this.epsilone);
        jo.put("alphabet", super.tabLabel);
        jo.put("number state", super.nbrState);
        JSONArray jaInitialState = new JSONArray(this.initialStateTab);
        jo.put("initial states", jaInitialState);
        JSONArray jaFinalState = new JSONArray(super.finalStateTab);
        jo.put("final states", jaFinalState);
        JSONArray jaTransitions = new JSONArray();

        String temp[] = new String[3];
        for (Transition transition : this.matTrans){
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
