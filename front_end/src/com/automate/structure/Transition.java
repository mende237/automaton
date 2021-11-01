package com.automate.structure;

public class Transition {
    protected String begin;
    protected String label;
    protected String end;

    public Transition(String begin , String label , String end){
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public String getBegin(){
        return this.begin;
    }

    public String getLabel() {
        return this.begin;
    }

    public String getEnd() {
        return this.begin;
    }

    public void setBegin(String begin){
        this.begin = begin;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("%s --%s--> %s", begin , label , end);
    }
}
