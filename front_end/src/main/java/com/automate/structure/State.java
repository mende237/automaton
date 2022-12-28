package com.automate.structure;

public class State {
    private String value;
    private StateType type;
    
    public State(String value , StateType type){
        this.value = value;
        this.type = type;
    }

    public State(String value) {
        this.value = value;
        this.type = StateType.NORMAL;
    }

    public String getValue(){
        return this.value;
    }

    public StateType getType(){
        return this.type;
    }

    public void setValue(String value){
        this.value = value;
    }

    public void setType(StateType type){
        this.type = type;
    }

    @Override
    public String toString(){
        return String.format("%s %s", this.value , this.type);
    } 
}
