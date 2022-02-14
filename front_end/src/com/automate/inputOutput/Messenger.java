package com.automate.inputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Messenger implements Runnable {
    private String receptionPath;//le chemin vers lequel sera envoyé la reponse
    private String sendingPath;//le chemo
    private String dataPathResponse;

   

    private int previousId;
    private long delay;
    private long begin;
    private boolean response;

    private static Messenger messenger;

    private Messenger(){
        this.delay = 300000;
        this.previousId = -1;
    }

    /******************* getter********************/
    public String getDataPathResponse() {
        return this.dataPathResponse;
    }

    public boolean isResponse() {
        return this.response;
    }
    

    /************* methods **********************/
    public static Messenger getMessenger(){
        if(Messenger.messenger == null){
            Messenger.messenger = new Messenger();
        }

        return Messenger.messenger;
    }

    public boolean sendInstruction(Instruction instruction, String sendingPath, String receptionPath, long delay)
            throws FileNotFoundException {
        Scheduler.DOWNS1();
        this.sendingPath = sendingPath;
        this.receptionPath = receptionPath;
        this.delay = delay;

        JSONObject obj = new JSONObject();
        obj.put("id", instruction.getID());
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
        run();
        return true;

        // faire un up lorsqu'on recoit la reponse a l'instruction envoyé
    }


    public boolean sendInstruction(Instruction instruction, String sendingPath, String receptionPath)
            throws FileNotFoundException {
                return this.sendInstruction(instruction, sendingPath, receptionPath , 300000);

    }

    @Override
    public void run() {
        boolean rep = false;

        while (rep == false && System.currentTimeMillis() - this.begin <= this.delay) {
            File file = new File(this.receptionPath);
            if (file.exists()) {
                try (Scanner reader = new Scanner(new File(this.receptionPath)).useDelimiter("\\Z")) {
                    String content = reader.next();
                    reader.close();
                    JSONObject obj = new JSONObject(content);
                    if(this.previousId != obj.getInt("id")){
                        this.dataPathResponse = obj.getString("data path");
                    }
                } catch (FileNotFoundException | JSONException e) {
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
                    e.printStackTrace();
                }
            }
        }

        if (rep == true) {
            this.response = true;
            Scheduler.UPS1();
        } else {
            this.response = false;
        }
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
