package com.automate.structure;

public class State {
    private String value;
    private StateType type;
    
    public State(String value , StateType type){
        this.value = value;
        this.type = type;
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

}
