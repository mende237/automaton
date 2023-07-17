package com.automate.structure;

public class State {
    private String name;
    private boolean finalState = true;
    private boolean initial = false;

    private StateType type;
    
    public State(String name , StateType type){
        this.name = name;
        this.type = type;
        if(type == StateType.FINAL){
            initial = false;
            finalState = true;
        }else if(type == StateType.FINAL_INITIAL){
            initial = true;
            finalState = true;
        }else if(type == StateType.INITIAL){
            initial = true;
            finalState = false;
        }else{
            initial = false;
            finalState = false;
        }
    }

    public State(String name) {
        this.name = name;
        this.type = StateType.NORMAL;
    }


    

    public String getName(){
        return this.name;
    }



    public boolean equalState(State state){
        return this.name.equals(state.getName());
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
    
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) 
            return true;
        
        if (!(obj instanceof State)) 
            return false;
        
        State state = (State) obj;
        return this.name.equalsIgnoreCase(state.getName());
    }
}
