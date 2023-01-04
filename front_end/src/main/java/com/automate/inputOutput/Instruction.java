package com.automate.inputOutput;

public class Instruction {
    private String name;
    private String dataPath;
    private static int cmpt = 0;
    private int id = 0;

    public Instruction(String name , String dataPath){
        this.id = cmpt;
        this.name = name;
        this.dataPath = dataPath;
        Instruction.cmpt += 1;
    }

    public int getId(){
        return this.id;
    }


    public String getName(){
        return this.name;
    }

    public String getDataPath(){
        return this.dataPath;
    }


    public void setDataPath(String path){
        this.dataPath = path;
    }

    public void setName(String name){
        this.name = name;
    }
    
}
