package com.automate.inputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import org.json.JSONObject;

public class Messenger{
    private String receptionPath;
    private String sendingPath;

    private String dataPathResponse;
    private static final long DELAY = 30000;
    
    private int previousId;
    private long delay;
    private long begin;
    private boolean response;

    private static Messenger messenger;

    private Messenger(){
        this.delay = Messenger.DELAY;
        this.previousId = -1;
    }

    /******************* getter********************/
    public String getDataPathResponse() {
        return this.dataPathResponse;
    }

    public boolean isResponse() {
        return this.response;
    }

    public String getReceptionPath() {
        return this.receptionPath;
    }

    public String getSendingPath() {
        return this.sendingPath;
    }
    
    /******************* setter ********************/
    public void setReceptionPath(String receptionPath) {
        this.receptionPath = receptionPath;
    }

    public void setSendingPath(String sendingPath) {
        this.sendingPath = sendingPath;
    }
    /************* methods **********************/
    public static Messenger getMessenger(){
        if(Messenger.messenger == null){
            Messenger.messenger = new Messenger();
        }

        return Messenger.messenger;
    }

    

    public void sendInstruction(Instruction instruction , long delay)
            throws FileNotFoundException {
        Scheduler.DOWNS1();
        this.delay = delay;

        JSONObject obj = new JSONObject();
        obj.put("id", instruction.getId());
        obj.put("name", instruction.getName());
        obj.put("data path", instruction.getDataPath());
        obj.put("reception path", this.receptionPath);

        StringWriter strW = new StringWriter();
        obj.write(strW);
        String jsonText = strW.toString();
        PrintWriter writer = new PrintWriter(this.sendingPath);
        writer.println(jsonText);
        writer.close();
        this.begin = System.currentTimeMillis();
        this.checkResponse();
    }

    public void sendInstruction(Instruction instruction)
            throws FileNotFoundException {
        this.sendInstruction(instruction , Messenger.DELAY);
    }


    public void sendInstruction(Instruction instruction, String sendingPath, String receptionPath, long delay)
            throws FileNotFoundException {
       
        this.sendingPath = sendingPath;
        this.receptionPath = receptionPath;
        this.sendInstruction(instruction, delay);
    }

    public void sendInstruction(Instruction instruction, String sendingPath, String receptionPath)
            throws FileNotFoundException {
            this.sendInstruction(instruction, sendingPath, receptionPath , Messenger.DELAY);
    }


    
    private void checkResponse() throws FileNotFoundException {
        boolean rep = false;
        while (rep == false && System.currentTimeMillis() - this.begin <= this.delay) {
            File file = new File(this.receptionPath);
            if (file.exists()) {
                //try (Scanner reader = new Scanner(new File(this.receptionPath)).useDelimiter("\\Z")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Scanner reader = new Scanner(new File(this.receptionPath)).useDelimiter("\\Z");
                    String content = reader.next();
                    reader.close();
                    JSONObject obj = new JSONObject(content);
                    if(this.previousId != obj.getInt("id")){
                        this.dataPathResponse = obj.getString("data path");
                        this.previousId = obj.getInt("id");
                        rep = true;
                        System.out.println("response");
                    }
                // } catch (FileNotFoundException | JSONException e) {
                    /*****************************************
                    ********************************************
                     ********************************************
                     ********************************************
                     ********************************************
                     ********************************************
                     *****************a traité *****************
                     ********************************************
                     ********************************************
                     ********************************************
                     ********************************************
                    *******************************************/
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                //}
                //System.out.println("waiting");
            }else{
                rep = false;
                System.out.println("le dossier contenant les reponses est vide");
            }
        }

        if (rep == true) {
            this.response = true;
        } else {
            System.out.println("pas de reponse");
            this.response = false;
        }
        Scheduler.UPS1();
    }

    public static void main(String[] args) {
        // System.out.println("enter!!!!!!!!!!!!!!!");
        // Scheduler.DOWNS1();
        // System.out.println("pass1");
        // Scheduler.DOWNS1();
        // System.out.println("pass2");
        // Scheduler.DOWNS1();
        // System.out.println("pass3");
        // System.out.println("enter!!!!!!!!!!!!!!!!!!");

    }

}
