package com.automate.structure;

public class State {
    private String name;
    public boolean finalState = true;
    public boolean initial = false;

    private StateType type;
    
    public State(String name , StateType type){
        this.name = name;
        this.type = type;
    }

    public State(String name) {
        this.name = name;
        this.type = StateType.NORMAL;
    }

    public String getName(){
        return this.name;
    }



    public StateType getType(){
        return this.type;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setType(StateType type){
        this.type = type;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public boolean getFinal() {
        return finalState;
    }

    @Override
    public String toString(){
        return String.format("%s %s", this.name , this.type);
    } 
}
