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

    public String getLabel() {
        return this.label;
    }

    public State getEnd() {
        return this.end;
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

    public boolean equalTransition(Transition transition){
        return this.begin.equalState(transition.getBegin()) &
               this.label.equals(transition.getLabel()) &
               this.end.equalState(transition.getEnd());
    }

    public boolean semiEqualTransition(Transition transition){
        return this.begin.equalState(transition.getBegin()) &
               this.label.equals(transition.getLabel());
    }

    @Override
    public String toString() {
        return String.format("%s --%s--> %s", begin.toString() , label , end.toString());
    }
}
