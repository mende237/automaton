package com.automate.structure;

import static guru.nidi.graphviz.model.Factory.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import javafx.scene.image.Image;

public abstract class Automate {
    protected int cmpt;
    protected int nbrState;
    protected String name;
    protected String description;
    protected String path;
   
    protected String finalStateTab[];
    protected String tabLabel[];
    protected Graph graph;
    
    // protected ArrayList<String> dinamicFinalStateTab;
    // protected ArrayList<String> dinamicTabLabel;
     
    /*******************************************
     * constructor
     *************************************************/
    public Automate(){

    }

    public Automate(String tabLabel[] , int nbrState, String finalStateTab[] , String name, String description , String path) {
        this(tabLabel , nbrState , finalStateTab , path);
        this.name = name;
        this.description = description;
    }

    public Automate(String tabLabel[], int nbrState, String finalStateTab[], String name, String description) {
        this(tabLabel, nbrState, finalStateTab);
        this.name = name;
        this.description = description;
    }

    public Automate(String tabLabel[], int nbrState, String finalStateTab[]) {
        this.finalStateTab = finalStateTab;
        this.tabLabel = tabLabel;
        this.nbrState = nbrState;
        this.cmpt = 0;

    }

    public Automate(String tabLabel[], int nbrState, String finalStateTab[] , String path) {
        this(tabLabel , nbrState , finalStateTab);
        this.path = path;
    }

    /*************************************************
     * getter
     **************************************************/
    public String getPath() {
        return this.path;
    }

    public String[] getFinalStateTab() {
        return this.finalStateTab;
    }

    public String[] getTabLabel() {
        return this.tabLabel;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /*********************************************
     * setter
     ***********************************************/
    public void setPath(String path) {
        this.path = path;
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
    public abstract Graph markeGraph();
    public abstract void addTransition(State begin, String label, State end);
    // public abstract void addTransitionToGraph(Transition transition);


    public static Graph markeGraph(ArrayList<Transition> matTrans){
        
        Graph g = null;
        
        if(matTrans.size() > 0)
            g = graph("afd").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
            .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < matTrans.size(); i++) {
            g = g.with(Automate.addState(matTrans.get(i).begin)
                    .link(to(Automate.addState(matTrans.get(i).end))
                            .with(Label.of(matTrans.get(i).label))));
        }

        return g;
    }

    public static Graph markeGraph(ArrayList<Transition> matTrans , String epsilone){
        Graph g = null;
        
        if(matTrans.size() > 0)
            g = graph("afd").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
            .nodeAttr().with(Shape.CIRCLE);

        for (int i = 0; i < matTrans.size(); i++) {
            if (matTrans.get(i).label.equalsIgnoreCase(epsilone) == false) {
                g = g.with(Automate.addState(matTrans.get(i).begin)
                        .link(to(Automate.addState(matTrans.get(i).end))
                                .with(Label.of(matTrans.get(i).label))));
            } else {
                g = g.with(Automate.addState(matTrans.get(i).begin)
                        .link(to(Automate.addState(matTrans.get(i).end))
                                .with(Label.of("\u03B5"))));
            }
        }
        return g;
    }

    
    public void makeImage(String path) {
        Graph g = this.markeGraph();
        try {
            Graphviz.fromGraph(g).width(1500).render(Format.PNG).toFile(new File(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Image makeImage(Graph graph) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Graphviz.fromGraph(graph).render(Format.PNG).toOutputStream(outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        
        Image image = new Image(new ByteArrayInputStream(imageBytes));
        // WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());    
        return image;
    }

    
    
    protected static Node addState(State state) {
        switch (state.getType()) {
            case INITIAL:
                return node(state.getName()).with(Style.FILLED , Color.rgb("#b5fed9").fill());
            case FINAL:
                return node(state.getName()).with(Style.FILLED , Shape.DOUBLE_CIRCLE, Color.rgb("#e2cbc1").fill());
            case WELL:
                return node(state.getName()).with(Style.FILLED , Color.GRAY.fill());
            case FINAL_INITIAL:
                return node(state.getName()).with(Style.FILLED, Shape.DOUBLE_CIRCLE, Color.rgb("#b5fed9").fill());
            default:
                return node(state.getName()).with(Style.FILLED , Color.rgb("#87CEEB").fill());
        }
    }

    @Override
    public String toString(){
        return this.name;
    }
}
