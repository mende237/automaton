package com.automate.structure;

public class Transition {
    protected State begin;
    protected String label;
    protected State end;

    public Transition(State begin , String label , State end){
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public State getBegin(){
        return this.begin;
    }

    public State getLabel() {
        return this.begin;
    }

    public State getEnd() {
        return this.begin;
    }

    public void setBegin(State begin){
        this.begin = begin;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEnd(State end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("%s --%s--> %s", begin.toString() , label , end.toString());
    }
}
