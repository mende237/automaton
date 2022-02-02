package com.automate.inputOutput;

public class Instruction {
    private String name;
    private String dataPath;
    private static int ID = 0;

    public Instruction(String name , String dataPath){
        Instruction.ID += 1;
        this.name = name;
        this.dataPath = dataPath;
    }

    public int getID(){
        return Instruction.ID;
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
