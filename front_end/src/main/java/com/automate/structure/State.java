package com.automate.structure;

public class State {
    private String name;
    private boolean finalState = true;
    private boolean initial = false;

    private StateType type;
    
    public State(String name , StateType type){
        this.name = name;
        this.type = type;
        this.setType(type);
    }

    public State(String name) {
        this.name = name;
        this.type = StateType.NORMAL;
        this.setType(this.type);
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
        if(type == StateType.FINAL){
            this.initial = false;
            this.finalState = true;
        }else if(type == StateType.FINAL_INITIAL){
            this.initial = true;
            this.finalState = true;
        }else if(type == StateType.INITIAL){
            this.initial = true;
            this.finalState = false;
        }else{
            this.initial = false;
            this.finalState = false;
        }
    }

    public boolean isInitial() {
        return this.initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public boolean isFinalState() {
        return this.finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public boolean getFinal() {
        return this.finalState;
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
